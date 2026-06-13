package crm.service;

import com.azure.messaging.servicebus.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;

/**
 * Cloud-ready Azure Service Bus Scheduler Service.
 * Replaces java.util.Timer and local scheduling with distributed, timezone-agnostic
 * Azure Service Bus scheduled message delivery.
 * 
 * This service provides:
 * - Distributed task scheduling across multiple instances
 * - Timezone-agnostic execution (all times in UTC)
 * - Reliable message delivery with retry policies
 * - Scalability without server-local dependencies
 */
@Service
@Slf4j
public class AzureServiceBusSchedulerService {

    @Value("${azure.servicebus.connection-string:#{null}}")
    private String connectionString;

    @Value("${azure.servicebus.queue-name:scheduled-tasks}")
    private String queueName;

    private ServiceBusSenderClient senderClient;
    private ServiceBusProcessorClient processorClient;

    /**
     * Initializes the Azure Service Bus clients.
     * Creates sender for scheduling messages and processor for receiving them.
     */
    @PostConstruct
    public void initialize() {
        if (connectionString == null || connectionString.isEmpty()) {
            log.warn("Azure Service Bus connection string is not configured. " +
                    "Scheduled messaging will not be available. " +
                    "Set AZURE_SERVICEBUS_CONNECTION_STRING environment variable.");
            return;
        }

        try {
            // Create sender client for scheduling messages
            senderClient = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .sender()
                    .queueName(queueName)
                    .buildClient();

            log.info("Azure Service Bus Scheduler initialized successfully for queue: {}", queueName);
        } catch (Exception e) {
            log.error("Failed to initialize Azure Service Bus Scheduler", e);
        }
    }

    /**
     * Schedules a message for future delivery.
     * Replaces java.util.Timer.schedule() with cloud-native scheduling.
     * 
     * @param messageBody The message content to schedule
     * @param scheduledTime The UTC time when the message should be delivered
     * @return The sequence number of the scheduled message
     */
    public Long scheduleMessage(String messageBody, OffsetDateTime scheduledTime) {
        if (senderClient == null) {
            throw new IllegalStateException("Azure Service Bus Scheduler is not initialized. " +
                    "Check connection string configuration.");
        }

        try {
            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            message.setMessageId(java.util.UUID.randomUUID().toString());
            
            Long sequenceNumber = senderClient.scheduleMessage(message, scheduledTime);
            
            log.info("Message scheduled successfully. Sequence: {}, Scheduled Time: {}", 
                    sequenceNumber, scheduledTime);
            
            return sequenceNumber;
        } catch (Exception e) {
            log.error("Failed to schedule message", e);
            throw new RuntimeException("Failed to schedule message via Azure Service Bus", e);
        }
    }

    /**
     * Schedules a message for delivery after a specified delay.
     * 
     * @param messageBody The message content to schedule
     * @param delaySeconds The delay in seconds before message delivery
     * @return The sequence number of the scheduled message
     */
    public Long scheduleMessageWithDelay(String messageBody, long delaySeconds) {
        OffsetDateTime scheduledTime = OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(delaySeconds);
        return scheduleMessage(messageBody, scheduledTime);
    }

    /**
     * Cancels a scheduled message.
     * 
     * @param sequenceNumber The sequence number of the message to cancel
     */
    public void cancelScheduledMessage(Long sequenceNumber) {
        if (senderClient == null) {
            throw new IllegalStateException("Azure Service Bus Scheduler is not initialized.");
        }

        try {
            senderClient.cancelScheduledMessage(sequenceNumber);
            log.info("Scheduled message cancelled. Sequence: {}", sequenceNumber);
        } catch (Exception e) {
            log.error("Failed to cancel scheduled message", e);
            throw new RuntimeException("Failed to cancel scheduled message", e);
        }
    }

    /**
     * Starts a processor to receive and handle scheduled messages.
     * This replaces the TimerTask execution logic.
     * 
     * @param messageHandler The consumer function to process received messages
     */
    public void startMessageProcessor(Consumer<ServiceBusReceivedMessageContext> messageHandler) {
        if (connectionString == null || connectionString.isEmpty()) {
            log.warn("Cannot start message processor: Azure Service Bus not configured");
            return;
        }

        try {
            processorClient = new ServiceBusClientBuilder()
                    .connectionString(connectionString)
                    .processor()
                    .queueName(queueName)
                    .processMessage(messageHandler)
                    .processError(context -> {
                        log.error("Error processing message: {}", context.getException().getMessage());
                    })
                    .buildProcessorClient();

            processorClient.start();
            log.info("Azure Service Bus message processor started for queue: {}", queueName);
        } catch (Exception e) {
            log.error("Failed to start message processor", e);
        }
    }

    /**
     * Stops the message processor.
     */
    public void stopMessageProcessor() {
        if (processorClient != null) {
            processorClient.stop();
            log.info("Azure Service Bus message processor stopped");
        }
    }

    /**
     * Cleanup method to close Azure Service Bus clients.
     */
    @PreDestroy
    public void cleanup() {
        if (senderClient != null) {
            senderClient.close();
            log.info("Azure Service Bus sender client closed");
        }
        if (processorClient != null) {
            processorClient.close();
            log.info("Azure Service Bus processor client closed");
        }
    }

    /**
     * Example: Schedule a recurring task by sending a message that reschedules itself.
     * This pattern replaces java.util.Timer.scheduleAtFixedRate().
     * 
     * @param messageBody The task message
     * @param intervalSeconds The interval between executions
     */
    public void scheduleRecurringTask(String messageBody, long intervalSeconds) {
        // Initial schedule
        scheduleMessageWithDelay(messageBody, intervalSeconds);
        
        // The message handler should reschedule the next execution after processing
        log.info("Recurring task scheduled with interval: {} seconds", intervalSeconds);
    }
}

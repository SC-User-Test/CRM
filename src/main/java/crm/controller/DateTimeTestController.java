package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Cloud-ready DateTime Test Controller that uses timezone-agnostic date/time handling.
 * All timestamps are normalized to UTC to ensure consistency across distributed cloud environments.
 * 
 * For scheduled operations, use Azure Service Bus Scheduled Messages instead of java.util.Timer.
 * Configuration example in application.properties:
 * - azure.servicebus.connection-string=<your-connection-string>
 * - azure.servicebus.queue-name=<your-queue-name>
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    /**
     * Displays date/time information using cloud-native, timezone-agnostic patterns.
     * All dates are normalized to UTC to prevent timezone-related issues in distributed environments.
     * 
     * @param model Spring MVC model
     * @return View name
     */
    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based timestamps for cloud environments
        Instant utcTimestamp = Instant.now();
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        
        // For display purposes, convert to local time if needed
        // But always store/process in UTC in cloud environments
        model.addAttribute("utcTimestamp", utcTimestamp);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        
        // Add timezone information for debugging
        model.addAttribute("systemTimezone", ZoneOffset.UTC.toString());
        model.addAttribute("epochMillis", utcTimestamp.toEpochMilli());
        
        return "date/test";
    }

    /**
     * Example method showing how to schedule tasks using Azure Service Bus instead of java.util.Timer.
     * 
     * Note: This is a placeholder to demonstrate the pattern. Actual implementation requires:
     * 1. Azure Service Bus dependency in pom.xml
     * 2. Configuration in application.properties
     * 3. Service Bus client initialization
     * 
     * Example usage:
     * <pre>
     * {@code
     * @Autowired
     * private ServiceBusSenderClient senderClient;
     * 
     * public void scheduleTask(String taskData, Duration delay) {
     *     ServiceBusMessage message = new ServiceBusMessage(taskData);
     *     message.setScheduledEnqueueTime(OffsetDateTime.now().plus(delay));
     *     senderClient.scheduleMessage(message, message.getScheduledEnqueueTime());
     * }
     * }
     * </pre>
     * 
     * Benefits over java.util.Timer:
     * - Distributed: Works across multiple instances
     * - Persistent: Survives container restarts
     * - Scalable: Handles high volumes
     * - Timezone-agnostic: Uses UTC timestamps
     */
    private void scheduleTaskExample() {
        // This method serves as documentation for the migration pattern
        // Actual implementation should be in a dedicated service class
        
        // OLD PATTERN (NOT CLOUD-READY):
        // Timer timer = new Timer();
        // timer.schedule(new TimerTask() { ... }, delay);
        
        // NEW PATTERN (CLOUD-READY):
        // Use Azure Service Bus Scheduled Messages
        // See method documentation above for implementation details
    }

}

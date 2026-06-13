package crm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Cloud-ready DateTime Test Controller with timezone-aware operations.
 * Uses UTC for all time operations to ensure consistency across distributed cloud environments.
 * For scheduled operations, integrate with Azure Service Bus Scheduled Messages instead of local timers.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @Value("${app.timezone:UTC}")
    private String applicationTimezone;

    /**
     * Displays various date/time formats with cloud-ready, timezone-aware handling.
     * All times are normalized to UTC to prevent timezone inconsistencies in distributed deployments.
     * 
     * Note: For scheduled task execution in cloud environments, use Azure Service Bus
     * scheduled message delivery instead of java.util.Timer or server-local scheduling.
     * 
     * Azure Service Bus Scheduled Messages provide:
     * - Distributed, timezone-agnostic task execution
     * - Reliable message delivery with retry policies
     * - Scalability across multiple instances
     * - No dependency on server-local clock or timezone
     * 
     * Example Azure Service Bus integration:
     * - Use ServiceBusSenderClient.scheduleMessage() for future task execution
     * - Set message enqueue time using OffsetDateTime in UTC
     * - Process scheduled messages via ServiceBusProcessorClient
     */
    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based timestamps for cloud consistency
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId appZone = ZoneId.of(applicationTimezone);
        
        // Current time in UTC (recommended for cloud applications)
        Instant utcInstant = Instant.now();
        ZonedDateTime utcDateTime = ZonedDateTime.now(utcZone);
        
        // Application timezone (configurable via environment variable)
        ZonedDateTime appDateTime = ZonedDateTime.now(appZone);
        
        // Legacy Date object (converted from UTC Instant)
        Date standardDate = Date.from(utcInstant);
        
        // Local date/time (should be used with explicit timezone context)
        LocalDateTime localDateTime = LocalDateTime.now(utcZone);
        LocalDate localDate = LocalDate.now(utcZone);
        
        // Add attributes to model
        model.addAttribute("standardDate", standardDate);
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("timestamp", utcInstant);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("appDateTime", appDateTime);
        model.addAttribute("applicationTimezone", applicationTimezone);
        model.addAttribute("utcTimezone", "UTC");
        
        // Add scheduling guidance
        model.addAttribute("schedulingNote", 
            "For scheduled tasks in cloud environments, use Azure Service Bus Scheduled Messages " +
            "instead of java.util.Timer or local scheduling mechanisms.");
        
        return "date/test";
    }

    /**
     * Example method showing how to calculate a future scheduled time for Azure Service Bus.
     * This replaces the need for java.util.Timer or java.util.TimerTask.
     * 
     * @param delaySeconds Number of seconds in the future to schedule
     * @return ZonedDateTime in UTC for Azure Service Bus scheduling
     */
    public ZonedDateTime calculateScheduledTime(long delaySeconds) {
        return ZonedDateTime.now(ZoneId.of("UTC")).plusSeconds(delaySeconds);
    }

    /**
     * Example method to demonstrate timezone conversion for distributed systems.
     * 
     * @param instant The instant to convert
     * @param targetTimezone The target timezone
     * @return ZonedDateTime in the target timezone
     */
    public ZonedDateTime convertToTimezone(Instant instant, String targetTimezone) {
        return instant.atZone(ZoneId.of(targetTimezone));
    }
}

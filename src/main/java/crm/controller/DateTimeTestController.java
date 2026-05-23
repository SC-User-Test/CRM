package crm.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Cloud-ready DateTime controller with timezone-aware operations.
 * 
 * This controller demonstrates proper time handling for distributed cloud environments:
 * 1. Uses UTC as the standard timezone for all server-side operations
 * 2. Supports configurable timezone via application properties
 * 3. Uses Clock abstraction for testability and consistency
 * 4. Avoids local system clock dependencies
 * 
 * For scheduled operations, use Azure Logic Apps or Azure Container Apps Jobs instead of
 * in-application scheduling (@Scheduled, Quartz) to ensure distributed-safe execution.
 * 
 * Configuration:
 * - app.timezone: Application timezone (default: UTC)
 * - app.display-timezone: Display timezone for UI (default: UTC)
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    private final Clock clock;
    private final ZoneId displayZoneId;

    public DateTimeTestController(
            @Value("${app.timezone:UTC}") String timezone,
            @Value("${app.display-timezone:UTC}") String displayTimezone) {
        
        // Use Clock abstraction for consistent, testable time operations
        // In production, this uses the system clock with configured timezone
        // In tests, this can be replaced with a fixed clock
        ZoneId zoneId = ZoneId.of(timezone);
        this.clock = Clock.system(zoneId);
        this.displayZoneId = ZoneId.of(displayTimezone);
    }

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Get current time using Clock abstraction (cloud-safe, testable)
        Instant now = clock.instant();
        
        // Convert to various formats for display
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, displayZoneId);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, displayZoneId);
        LocalDate localDate = LocalDate.ofInstant(now, displayZoneId);
        
        // Add timezone-aware timestamps to model
        model.addAttribute("standardDate", Date.from(now));
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("timestamp", now);
        model.addAttribute("zonedDateTime", zonedDateTime);
        model.addAttribute("timezone", displayZoneId.getId());
        model.addAttribute("utcTimestamp", now.toString());
        
        return "date/test";
    }

    /**
     * Example endpoint for scheduled task execution.
     * 
     * NOTE: For production cloud deployments, scheduled tasks should be externalized to:
     * - Azure Logic Apps with Recurrence triggers
     * - Azure Container Apps scheduled jobs
     * - Azure Functions with Timer triggers
     * 
     * This ensures:
     * - Distributed-safe execution (no duplicate runs across instances)
     * - Centralized scheduling management
     * - Better observability and monitoring
     * - Automatic retry and error handling
     * 
     * If you must use in-application scheduling, ensure:
     * - Use distributed locks (Redis, Azure Cosmos DB)
     * - Configure timezone explicitly (avoid system default)
     * - Use Clock abstraction for testability
     */
    @GetMapping("/scheduled-task-info")
    public String scheduledTaskInfo(Model model) {
        model.addAttribute("message", 
            "Scheduled tasks should be externalized to Azure Logic Apps or Container Apps Jobs. " +
            "See controller documentation for details.");
        model.addAttribute("currentTime", clock.instant());
        model.addAttribute("timezone", clock.getZone().getId());
        
        return "date/scheduled-info";
    }

}

package crm.controller;

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

/**
 * Cloud-native date/time controller using java.time API with UTC standardization.
 * This eliminates timezone inconsistencies in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    // Use UTC clock for all time operations to ensure consistency across cloud regions
    private static final Clock UTC_CLOCK = Clock.systemUTC();
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use java.time API with UTC standardization instead of java.util.Date
        // This ensures consistent behavior across all cloud regions and containers
        
        // Instant represents a point in time in UTC (always timezone-independent)
        Instant currentInstant = Instant.now(UTC_CLOCK);
        model.addAttribute("timestamp", currentInstant);
        
        // ZonedDateTime with explicit UTC timezone for display purposes
        ZonedDateTime utcDateTime = ZonedDateTime.now(UTC_CLOCK);
        model.addAttribute("utcDateTime", utcDateTime);
        
        // LocalDateTime in UTC context (for database storage and logging)
        LocalDateTime localDateTime = LocalDateTime.now(UTC_CLOCK);
        model.addAttribute("localDateTime", localDateTime);
        
        // LocalDate in UTC context
        LocalDate localDate = LocalDate.now(UTC_CLOCK);
        model.addAttribute("localDate", localDate);
        
        // ISO-8601 formatted timestamp for APIs and inter-service communication
        String isoTimestamp = currentInstant.toString();
        model.addAttribute("isoTimestamp", isoTimestamp);
        
        // Unix epoch milliseconds (useful for compatibility)
        long epochMilli = currentInstant.toEpochMilli();
        model.addAttribute("epochMilli", epochMilli);
        
        // Add timezone information for display
        model.addAttribute("timezone", UTC_ZONE.getId());
        
        return "date/test";
    }

    /**
     * Example method showing how to convert between timezones if needed for display.
     * Always store and process in UTC, only convert for user display.
     */
    public ZonedDateTime convertToUserTimezone(Instant utcInstant, String userTimezoneId) {
        ZoneId userZone = ZoneId.of(userTimezoneId);
        return utcInstant.atZone(userZone);
    }

    /**
     * Example method showing how to parse ISO-8601 timestamps from external systems.
     */
    public Instant parseIsoTimestamp(String isoTimestamp) {
        return Instant.parse(isoTimestamp);
    }
}

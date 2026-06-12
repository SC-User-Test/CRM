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
 * Cloud-ready date/time controller that uses java.time API and standardizes on UTC.
 * This eliminates timezone and clock synchronization issues in distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    // Use UTC clock for all time operations to ensure consistency across distributed services
    private static final Clock UTC_CLOCK = Clock.systemUTC();
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use java.time API with UTC standardization instead of java.util.Date
        // This ensures consistent time handling across all cloud regions and containers
        
        // Instant represents a point in time in UTC (recommended for timestamps)
        Instant utcInstant = Instant.now(UTC_CLOCK);
        model.addAttribute("timestamp", utcInstant);
        
        // ZonedDateTime with explicit UTC zone for timezone-aware operations
        ZonedDateTime utcZonedDateTime = ZonedDateTime.now(UTC_CLOCK);
        model.addAttribute("utcZonedDateTime", utcZonedDateTime);
        
        // LocalDateTime in UTC context (no timezone info, but derived from UTC clock)
        LocalDateTime utcLocalDateTime = LocalDateTime.now(UTC_CLOCK);
        model.addAttribute("localDateTime", utcLocalDateTime);
        
        // LocalDate in UTC context
        LocalDate utcLocalDate = LocalDate.now(UTC_CLOCK);
        model.addAttribute("localDate", utcLocalDate);
        
        // Add ISO-8601 formatted strings for API/logging consistency
        model.addAttribute("isoTimestamp", utcInstant.toString());
        model.addAttribute("isoZonedDateTime", utcZonedDateTime.toString());
        
        return "date/test";
    }

}

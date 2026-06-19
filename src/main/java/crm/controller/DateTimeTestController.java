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
 * Cloud-native date/time controller using java.time API.
 * All timestamps are standardized to UTC for cloud consistency.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    // Use UTC clock for cloud-native consistency across distributed systems
    private final Clock utcClock = Clock.systemUTC();

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use java.time API with UTC standardization for cloud environments
        // This ensures consistent time handling across multiple regions and containers
        
        // Current instant in UTC (recommended for all cloud storage and logging)
        Instant currentInstant = Instant.now(utcClock);
        model.addAttribute("timestamp", currentInstant);
        
        // Current date/time in UTC zone
        ZonedDateTime utcDateTime = ZonedDateTime.now(utcClock);
        model.addAttribute("utcDateTime", utcDateTime);
        
        // Local date/time (for display purposes, but stored as UTC in backend)
        LocalDateTime localDateTime = LocalDateTime.now(utcClock);
        model.addAttribute("localDateTime", localDateTime);
        
        // Local date
        LocalDate localDate = LocalDate.now(utcClock);
        model.addAttribute("localDate", localDate);
        
        // For comparison: current time in different zones (if needed for display)
        ZonedDateTime systemZoneDateTime = ZonedDateTime.now(Clock.system(ZoneId.systemDefault()));
        model.addAttribute("systemZoneDateTime", systemZoneDateTime);
        
        // ISO-8601 formatted timestamp (standard for APIs and inter-service communication)
        String isoTimestamp = currentInstant.toString();
        model.addAttribute("isoTimestamp", isoTimestamp);
        
        return "date/test";
    }
}

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
 * Cloud-ready DateTime Test Controller.
 * Uses UTC timezone for all time operations to ensure consistency across distributed cloud environments.
 * Replaces java.util.Date and server-local timezone dependencies with timezone-aware alternatives.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC timezone for all date/time operations to ensure consistency in cloud environments
        Instant now = Instant.now();
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        
        // Store UTC-based timestamps
        model.addAttribute("standardDate", java.util.Date.from(now));
        model.addAttribute("localDateTime", LocalDateTime.now(ZoneOffset.UTC));
        model.addAttribute("localDate", LocalDate.now(ZoneOffset.UTC));
        model.addAttribute("timestamp", now);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("timezone", "UTC");
        
        // Add ISO-8601 formatted timestamps for API compatibility
        model.addAttribute("isoTimestamp", now.toString());
        
        return "date/test";
    }
}

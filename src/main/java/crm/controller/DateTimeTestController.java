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
 * DateTime Test Controller with cloud-native time handling.
 * Uses java.time API with UTC standardization for distributed cloud environments.
 * Eliminates timezone inconsistencies and local timer dependencies.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use Instant for UTC-based timestamps (cloud-native best practice)
        Instant utcTimestamp = Instant.now();
        
        // Convert to ZonedDateTime with explicit UTC timezone for consistency
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        
        // LocalDateTime in UTC context for display purposes
        LocalDateTime utcLocalDateTime = LocalDateTime.now(ZoneOffset.UTC);
        
        // LocalDate in UTC context
        LocalDate utcLocalDate = LocalDate.now(ZoneOffset.UTC);
        
        // Add UTC-based time attributes to model
        model.addAttribute("utcTimestamp", utcTimestamp);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("utcLocalDateTime", utcLocalDateTime);
        model.addAttribute("utcLocalDate", utcLocalDate);
        
        // Add formatted ISO-8601 strings for logging and inter-service communication
        model.addAttribute("iso8601Timestamp", utcTimestamp.toString());
        model.addAttribute("epochMillis", utcTimestamp.toEpochMilli());
        
        return "date/test";
    }

}

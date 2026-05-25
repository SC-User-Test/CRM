package crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Cloud-ready date/time controller using UTC timestamps.
 * Eliminates server-local timezone dependencies for distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use UTC-based timestamps for cloud-native consistency across regions
        Instant utcTimestamp = Instant.now();
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        
        // All timestamps standardized to UTC to avoid timezone inconsistencies
        model.addAttribute("utcTimestamp", utcTimestamp);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("utcDate", utcDateTime.toLocalDate());
        model.addAttribute("epochMillis", utcTimestamp.toEpochMilli());
        
        // Store timezone context separately if needed for display purposes
        model.addAttribute("timezone", "UTC");
        
        return "date/test";
    }

}

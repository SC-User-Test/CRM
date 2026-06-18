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
 * All timestamps are standardized to UTC to ensure consistency across distributed cloud environments.
 */
@Controller
@RequestMapping("/date")
public class DateTimeTestController {

    // Use UTC clock for all time operations to ensure consistency across cloud regions
    private static final Clock UTC_CLOCK = Clock.systemUTC();
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    @GetMapping("/test")
    public String dateTimeTest(Model model) {
        // Use java.time API with UTC standardization for cloud-native time handling
        Instant currentInstant = Instant.now(UTC_CLOCK);
        ZonedDateTime utcDateTime = ZonedDateTime.now(UTC_CLOCK);
        LocalDateTime localDateTime = LocalDateTime.now(UTC_CLOCK);
        LocalDate localDate = LocalDate.now(UTC_CLOCK);
        
        // Add UTC-based timestamps to model
        model.addAttribute("utcInstant", currentInstant);
        model.addAttribute("utcDateTime", utcDateTime);
        model.addAttribute("localDateTime", localDateTime);
        model.addAttribute("localDate", localDate);
        model.addAttribute("timestamp", currentInstant.toEpochMilli());
        model.addAttribute("isoTimestamp", currentInstant.toString());
        
        return "date/test";
    }

}

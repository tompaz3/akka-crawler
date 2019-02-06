package com.tp.samples.akkacrawler.crawler.rm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RmNewsDateParser {

  private final DateTimeFormatter publishedDayFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
  private final DateTimeFormatter publishedHourFormatter = DateTimeFormatter.ofPattern("HH:mm");
  private final Pattern hrPartsPattern = Pattern.compile("\\d{2}:\\d{2}");

  /**
   * Parses news' publish date as read from realmadrid.pl.
   *
   * @param dateText news' publish date read from the web page.
   * @return news' publish date
   */
  public LocalDateTime parse(String dateText) {
    String[] parts = dateText.split(",");
    LocalDate day = LocalDate.from(publishedDayFormatter.parse(parts[0].trim()));
    Matcher matcher = hrPartsPattern.matcher(parts[1].trim());
    LocalTime hour = LocalTime.of(0, 0);
    if (matcher.find()) {
      hour = LocalTime.from(publishedHourFormatter.parse(matcher.group()));
    }
    return LocalDateTime.of(day, hour);
  }

}

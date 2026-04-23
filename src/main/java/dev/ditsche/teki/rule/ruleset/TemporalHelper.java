package dev.ditsche.teki.rule.ruleset;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

final class TemporalHelper {

  private TemporalHelper() {}

  static Instant toInstant(Object value) {
    if (value instanceof Instant i) return i;
    if (value instanceof ZonedDateTime zdt) return zdt.toInstant();
    if (value instanceof OffsetDateTime odt) return odt.toInstant();
    if (value instanceof LocalDateTime ldt)
      return ldt.atZone(ZoneId.systemDefault()).toInstant();
    if (value instanceof LocalDate ld)
      return ld.atStartOfDay(ZoneId.systemDefault()).toInstant();
    if (value instanceof Date d) return d.toInstant();
    if (value instanceof Calendar c) return c.toInstant();
    return null;
  }

  static Instant parseIso(String value) {
    try {
      return Instant.parse(value);
    } catch (DateTimeParseException ignored) {
    }
    try {
      return LocalDateTime.parse(value).atZone(ZoneId.systemDefault()).toInstant();
    } catch (DateTimeParseException ignored) {
    }
    try {
      return LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault()).toInstant();
    } catch (DateTimeParseException ignored) {
    }
    throw new IllegalArgumentException("Cannot parse temporal value: " + value);
  }
}

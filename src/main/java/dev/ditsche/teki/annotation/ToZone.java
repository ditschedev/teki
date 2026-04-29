package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Converts a temporal field to a {@link java.time.ZonedDateTime} in the given timezone.
 *
 * <p>The value string must be a valid {@link java.time.ZoneId} identifier (e.g. {@code
 * "Europe/Berlin"}, {@code "America/New_York"}, {@code "UTC"}).
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ToZone {
  /**
   * IANA timezone identifier the field value should be converted to.
   *
   * @return zone id string
   */
  String value();
}

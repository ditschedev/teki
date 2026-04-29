package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

/**
 * Truncates a temporal field to the given {@link ChronoUnit} and normalizes it to an {@link
 * java.time.Instant}.
 *
 * <p>Supported units are {@code NANOS} through {@code DAYS}. Larger units are not supported by
 * {@link java.time.Instant#truncatedTo} and will cause a validation failure.
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TruncateTo {
  /**
   * The granularity to truncate to (e.g. {@code ChronoUnit.DAYS} for midnight UTC).
   *
   * @return chrono unit
   */
  ChronoUnit value();
}

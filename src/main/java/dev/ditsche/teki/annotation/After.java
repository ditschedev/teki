package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the @After validation annotation.
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
  /**
   * ISO-8601 date/time string the value must be strictly after (e.g. {@code "2025-01-01"} or
   * {@code "2025-01-01T00:00:00Z"}).
   *
   * @return boundary value
   */
  String value();
}

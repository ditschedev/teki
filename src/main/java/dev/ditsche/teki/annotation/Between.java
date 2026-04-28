package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the @Between validation annotation.
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Between {
  /**
   * Minimum allowed value (inclusive).
   *
   * @return minimum
   */
  long min();

  /**
   * Maximum allowed value (inclusive).
   *
   * @return maximum
   */
  long max();
}

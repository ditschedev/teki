package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the @Uuid validation annotation.
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Uuid {

  /** Required UUID version (1–5). Use 0 to accept any version (default). */
  int version() default 0;
}

package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documents that a field is intentionally optional.
 *
 * <p>Fields without {@code @Required} are already optional by default. This annotation is a no-op
 * at runtime — it exists purely to make the intent explicit in the schema definition.
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {}

package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Normalizes a temporal field to its {@link java.time.Instant} representation in UTC.
 *
 * <p>Accepts any temporal type supported by Teki and resolves the field as an {@link
 * java.time.Instant}.
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ToUtc {}

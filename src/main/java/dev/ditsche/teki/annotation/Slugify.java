package dev.ditsche.teki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Transforms a string field into a URL-safe slug.
 *
 * <p>Lowercases the value, strips diacritics, and replaces runs of non-alphanumeric characters with
 * hyphens. An input that produces an empty slug is rejected.
 *
 * @author Tobias Dittmann
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Slugify {}

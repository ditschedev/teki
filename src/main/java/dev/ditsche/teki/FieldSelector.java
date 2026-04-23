package dev.ditsche.teki;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Serializable method-reference selector used to derive a validated field name.
 *
 * @param <T> declaring type for the selected field accessor
 * @param <R> selected field value type
 * @author Tobias Dittmann
 */
@FunctionalInterface
public interface FieldSelector<T, R> extends Function<T, R>, Serializable {}

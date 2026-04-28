package dev.ditsche.teki;

import java.util.Map;

/**
 * Resolves a human-readable validation message for a given field, error type, and rule params.
 *
 * <p>Set a global resolver via {@link Teki#setGlobalResolver(MessageResolver)} to override built-in
 * English messages for all schemas — for example to plug in an i18n bundle:
 *
 * <pre>{@code
 * Teki.setGlobalResolver((field, type, params) -> bundle.getString(type));
 * }</pre>
 *
 * <p>Return {@code null} to fall back to the next resolver in the chain or the rule default.
 *
 * @author Tobias Dittmann
 */
@FunctionalInterface
public interface MessageResolver {

  /**
   * Returns a message for the given field, error type, and rule params, or {@code null} to delegate
   * to the next resolver in the chain.
   *
   * @param field field name the error is attached to
   * @param type stable error type key (e.g. {@code "format.email"})
   * @param params rule-specific parameters (e.g. {@code {"min": 5}} for {@code size.min})
   * @return custom message, or {@code null} to fall back
   */
  String resolve(String field, String type, Map<String, Object> params);
}

package dev.ditsche.teki;

/**
 * Resolves a human-readable validation message for a given field and error type.
 *
 * <p>Register a resolver on a schema via {@link Teki#messages(MessageResolver)} to override the
 * built-in English messages — for example to plug in an i18n bundle:
 *
 * <pre>{@code
 * Teki schema = Teki.fromRules(...)
 *     .messages((field, type) -> bundle.getString(type));
 * }</pre>
 *
 * <p>Return {@code null} to fall back to the rule's built-in message for that entry.
 *
 * @author Tobias Dittmann
 */
@FunctionalInterface
public interface MessageResolver {

  /**
   * Returns a message for the given field and error type, or {@code null} to use the default.
   *
   * @param field field name the error is attached to
   * @param type stable error type key (e.g. {@code "validation.error.format.email"})
   * @return custom message, or {@code null} to fall back to the rule's built-in message
   */
  String resolve(String field, String type);
}

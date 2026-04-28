package dev.ditsche.teki.rule;

import dev.ditsche.teki.MessageResolver;
import dev.ditsche.teki.Teki;
import java.util.Map;

/**
 * Defines a validation rule that can be used by Teki schemas.
 *
 * @author Tobias Dittmann
 */
public interface Rule {

  /**
   * Tests whether the supplied value satisfies this rule.
   *
   * @param value value to test
   * @return rule result describing success, failure, or a transformed value
   */
  RuleResult test(Object value);

  /**
   * Returns rule-specific parameters included in the error message (e.g. {@code {"min": 5}}).
   *
   * <p>Override in parameterized rules to expose runtime values to the {@link MessageResolver}.
   *
   * @return immutable map of parameter names to values; empty by default
   */
  default Map<String, Object> params() {
    return Map.of();
  }

  /**
   * Builds the error message for a failed validation by consulting the global {@link
   * MessageResolver}.
   *
   * <p>Custom rules may override this method to provide a fallback message used when the global
   * resolver returns {@code null} for their type key.
   *
   * @param field validated field name
   * @return human-readable validation message
   */
  default String message(String field) {
    MessageResolver resolver = Teki.getGlobalResolver();
    String msg = resolver != null ? resolver.resolve(field, getType(), params()) : null;
    return msg != null ? msg : String.format("Validation failed for field \"%s\"", field);
  }

  /**
   * Returns the stable error type for this rule.
   *
   * @return rule error type
   */
  String getType();
}

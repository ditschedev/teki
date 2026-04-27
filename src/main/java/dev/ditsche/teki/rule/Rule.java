package dev.ditsche.teki.rule;

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
   * Builds the error message for a failed validation.
   *
   * @param field validated field name
   * @return human-readable validation message
   */
  String message(String field);

  /**
   * Returns the stable error type for this rule.
   *
   * @return rule error type
   */
  String getType();
}

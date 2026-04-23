package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates values with the String rule.
 *
 * @author Tobias Dittmann
 */
public final class StringRule implements Rule {

  /** Creates a rule instance. */
  public StringRule() {}

  @Override
  public RuleResult test(Object value) {
    return RuleResult.passes(value instanceof String);
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" needs to be a string", field);
  }

  @Override
  public String getType() {
    return RULE_TYPE_PREFIX + "type.string";
  }
}

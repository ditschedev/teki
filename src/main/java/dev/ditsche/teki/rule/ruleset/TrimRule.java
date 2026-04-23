package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates values with the Trim rule.
 *
 * @author Tobias Dittmann
 */
public final class TrimRule implements Rule {

  /** Creates a rule instance. */
  public TrimRule() {}

  @Override
  public RuleResult test(Object value) {
    if (!(value instanceof String)) return RuleResult.reject();
    return RuleResult.resolve(((String) value).trim());
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" needs to be a string to be able to trim it", field);
  }

  @Override
  public String getType() {
    return RULE_TYPE_PREFIX + "format.trim";
  }
}

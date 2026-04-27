package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates that the value is a recognised date/time type.
 *
 * @author Tobias Dittmann
 */
public final class TemporalRule implements Rule {

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    return RuleResult.passes(TemporalHelper.toInstant(value) != null);
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" must be a valid date/time value", field);
  }

  @Override
  public String getType() {
    return "temporal";
  }
}

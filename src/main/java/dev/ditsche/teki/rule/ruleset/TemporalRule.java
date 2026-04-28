package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates that the value is a recognised date/time type.
 *
 * @author Tobias Dittmann
 */
public final class TemporalRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.TEMPORAL;

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    return RuleResult.passes(TemporalHelper.toInstant(value) != null);
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

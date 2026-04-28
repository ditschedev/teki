package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;

/**
 * Validates that the value is a date/time in the past or present.
 *
 * @author Tobias Dittmann
 */
public final class PastOrPresentRule implements Rule {

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    Instant instant = TemporalHelper.toInstant(value);
    if (instant == null) return RuleResult.reject();
    return RuleResult.passes(!instant.isAfter(Instant.now()));
  }

  @Override
  public String getType() {
    return TekiErrors.PAST_OR_PRESENT;
  }
}

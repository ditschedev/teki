package dev.ditsche.teki.rule.ruleset;

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
  public String message(String field) {
    return String.format("The field \"%s\" must be a date in the past or present", field);
  }

  @Override
  public String getType() {
    return "temporal.past_or_present";
  }
}

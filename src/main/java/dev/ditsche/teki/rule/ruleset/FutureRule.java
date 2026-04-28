package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;

/**
 * Validates that the value is a date/time in the future.
 *
 * @author Tobias Dittmann
 */
public final class FutureRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.FUTURE;

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    Instant instant = TemporalHelper.toInstant(value);
    if (instant == null) return RuleResult.reject();
    return RuleResult.passes(instant.isAfter(Instant.now()));
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

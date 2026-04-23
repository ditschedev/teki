package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.annotation.After;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;

/**
 * Validates that the value is strictly after a fixed date/time boundary.
 *
 * @author Tobias Dittmann
 */
public final class AfterRule implements Rule {

  private final Instant boundary;

  public AfterRule(Instant boundary) {
    this.boundary = boundary;
  }

  public AfterRule(After annotation) {
    this.boundary = TemporalHelper.parseIso(annotation.value());
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    Instant instant = TemporalHelper.toInstant(value);
    if (instant == null) return RuleResult.reject();
    return RuleResult.passes(instant.isAfter(boundary));
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" must be after %s", field, boundary);
  }

  @Override
  public String getType() {
    return RULE_TYPE_PREFIX + "temporal.after";
  }
}

package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.annotation.Before;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;

/**
 * Validates that the value is strictly before a fixed date/time boundary.
 *
 * @author Tobias Dittmann
 */
public final class BeforeRule implements Rule {

  private final Instant boundary;

  public BeforeRule(Instant boundary) {
    this.boundary = boundary;
  }

  public BeforeRule(Before annotation) {
    this.boundary = TemporalHelper.parseIso(annotation.value());
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    Instant instant = TemporalHelper.toInstant(value);
    if (instant == null) return RuleResult.reject();
    return RuleResult.passes(instant.isBefore(boundary));
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" must be before %s", field, boundary);
  }

  @Override
  public String getType() {
    return "temporal.before";
  }
}

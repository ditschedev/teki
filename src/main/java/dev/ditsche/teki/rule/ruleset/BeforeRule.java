package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.annotation.Before;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;
import java.util.Map;

/**
 * Validates that the value is strictly before a fixed date/time boundary.
 *
 * @author Tobias Dittmann
 */
public final class BeforeRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.BEFORE;

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
  public Map<String, Object> params() {
    return Map.of("boundary", boundary.toString());
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

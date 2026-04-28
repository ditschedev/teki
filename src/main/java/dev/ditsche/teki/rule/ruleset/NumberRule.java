package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates values with the Number rule.
 *
 * @author Tobias Dittmann
 */
public final class NumberRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.NUMBER;

  /** Creates a rule instance. */
  public NumberRule() {}

  @Override
  public RuleResult test(Object value) {
    if (!(value instanceof Number)) return RuleResult.passes(false);
    if (value instanceof Double d) return RuleResult.passes(!d.isNaN() && !d.isInfinite());
    if (value instanceof Float f) return RuleResult.passes(!f.isNaN() && !f.isInfinite());
    return RuleResult.passes(true);
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

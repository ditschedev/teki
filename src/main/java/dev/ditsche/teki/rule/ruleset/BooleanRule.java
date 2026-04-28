package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.Map;

/**
 * Validates values with the Boolean rule.
 *
 * @author Tobias Dittmann
 */
public final class BooleanRule implements Rule {

  private final boolean val;

  /**
   * Creates a BooleanRule.
   *
   * @param val expected boolean value
   */
  public BooleanRule(boolean val) {
    this.val = val;
  }

  @Override
  public RuleResult test(Object value) {
    if (!(value instanceof Boolean)) return RuleResult.reject();
    return RuleResult.passes((boolean) value == val);
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("expected", val);
  }

  @Override
  public String getType() {
    return TekiErrors.BOOLEAN;
  }
}

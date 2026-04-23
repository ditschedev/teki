package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigDecimal;

/**
 * Validates that the value is less than or equal to zero.
 *
 * @author Tobias Dittmann
 */
public final class NegativeOrZeroRule implements Rule {

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (value instanceof BigDecimal bd)
      return RuleResult.passes(bd.compareTo(BigDecimal.ZERO) <= 0);
    if (value instanceof Number n) return RuleResult.passes(n.doubleValue() <= 0);
    return RuleResult.reject();
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" must be negative or zero", field);
  }

  @Override
  public String getType() {
    return RULE_TYPE_PREFIX + "number.negative_or_zero";
  }
}

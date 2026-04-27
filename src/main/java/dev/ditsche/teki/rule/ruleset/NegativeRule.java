package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigDecimal;

/**
 * Validates that the value is strictly less than zero.
 *
 * @author Tobias Dittmann
 */
public final class NegativeRule implements Rule {

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (value instanceof BigDecimal bd)
      return RuleResult.passes(bd.compareTo(BigDecimal.ZERO) < 0);
    if (value instanceof Number n) return RuleResult.passes(n.doubleValue() < 0);
    return RuleResult.reject();
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" must be negative", field);
  }

  @Override
  public String getType() {
    return "number.negative";
  }
}

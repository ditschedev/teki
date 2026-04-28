package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigDecimal;

/**
 * Validates that the value is greater than or equal to zero.
 *
 * @author Tobias Dittmann
 */
public final class PositiveOrZeroRule implements Rule {

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (value instanceof BigDecimal bd)
      return RuleResult.passes(bd.compareTo(BigDecimal.ZERO) >= 0);
    if (value instanceof Number n) return RuleResult.passes(n.doubleValue() >= 0);
    return RuleResult.reject();
  }

  @Override
  public String getType() {
    return TekiErrors.POSITIVE_OR_ZERO;
  }
}

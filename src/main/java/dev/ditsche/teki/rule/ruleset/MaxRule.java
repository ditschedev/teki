package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * Validates values with the Max rule.
 *
 * @author Tobias Dittmann
 */
public final class MaxRule implements Rule {

  private final long max;

  /**
   * Creates a MaxRule.
   *
   * @param max maximum allowed value
   */
  public MaxRule(long max) {
    this.max = max;
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (value instanceof String) return RuleResult.passes(((String) value).length() <= max);
    if (value instanceof Collection)
      return RuleResult.passes(((Collection<?>) value).size() <= max);
    if (value instanceof Map) return RuleResult.passes(((Map<?, ?>) value).size() <= max);
    if (value instanceof boolean[]) return RuleResult.passes(((boolean[]) value).length <= max);
    if (value instanceof byte[]) return RuleResult.passes(((byte[]) value).length <= max);
    if (value instanceof short[]) return RuleResult.passes(((short[]) value).length <= max);
    if (value instanceof char[]) return RuleResult.passes(((char[]) value).length <= max);
    if (value instanceof int[]) return RuleResult.passes(((int[]) value).length <= max);
    if (value instanceof long[]) return RuleResult.passes(((long[]) value).length <= max);
    if (value instanceof float[]) return RuleResult.passes(((float[]) value).length <= max);
    if (value instanceof double[]) return RuleResult.passes(((double[]) value).length <= max);
    if (value instanceof Object[]) return RuleResult.passes(((Object[]) value).length <= max);
    if (value instanceof BigDecimal bd)
      return RuleResult.passes(bd.compareTo(BigDecimal.valueOf(max)) <= 0);
    if (value instanceof Number) return RuleResult.passes(((Number) value).doubleValue() <= max);
    return RuleResult.reject();
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("max", max);
  }

  @Override
  public String getType() {
    return TekiErrors.MAX;
  }
}

package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * Validates values with the Min rule.
 *
 * @author Tobias Dittmann
 */
public final class MinRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.MIN;

  private final long min;

  /**
   * Creates a MinRule.
   *
   * @param min minimum allowed value
   */
  public MinRule(long min) {
    this.min = min;
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (value instanceof String) return RuleResult.passes(((String) value).length() >= min);
    if (value instanceof Collection)
      return RuleResult.passes(((Collection<?>) value).size() >= min);
    if (value instanceof Map) return RuleResult.passes(((Map<?, ?>) value).size() >= min);
    if (value instanceof boolean[]) return RuleResult.passes(((boolean[]) value).length >= min);
    if (value instanceof byte[]) return RuleResult.passes(((byte[]) value).length >= min);
    if (value instanceof short[]) return RuleResult.passes(((short[]) value).length >= min);
    if (value instanceof char[]) return RuleResult.passes(((char[]) value).length >= min);
    if (value instanceof int[]) return RuleResult.passes(((int[]) value).length >= min);
    if (value instanceof long[]) return RuleResult.passes(((long[]) value).length >= min);
    if (value instanceof float[]) return RuleResult.passes(((float[]) value).length >= min);
    if (value instanceof double[]) return RuleResult.passes(((double[]) value).length >= min);
    if (value instanceof Object[]) return RuleResult.passes(((Object[]) value).length >= min);
    if (value instanceof BigDecimal bd)
      return RuleResult.passes(bd.compareTo(BigDecimal.valueOf(min)) >= 0);
    if (value instanceof Number) return RuleResult.passes(((Number) value).doubleValue() >= min);
    return RuleResult.reject();
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("min", min);
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

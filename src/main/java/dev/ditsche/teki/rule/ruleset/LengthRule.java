package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * Validates values with the Length rule.
 *
 * @author Tobias Dittmann
 */
public final class LengthRule implements Rule {

  private final long length;

  /**
   * Creates a LengthRule.
   *
   * @param length required length
   */
  public LengthRule(long length) {
    this.length = length;
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (value instanceof String) return RuleResult.passes(((String) value).length() == length);
    if (value instanceof BigDecimal bd)
      return RuleResult.passes(bd.compareTo(BigDecimal.valueOf(length)) == 0);
    if (value instanceof Number) return RuleResult.passes(((Number) value).longValue() == length);
    if (value instanceof Collection)
      return RuleResult.passes(((Collection<?>) value).size() == length);
    if (value instanceof Map) return RuleResult.passes(((Map<?, ?>) value).size() == length);
    if (value instanceof boolean[]) return RuleResult.passes(((boolean[]) value).length == length);
    if (value instanceof byte[]) return RuleResult.passes(((byte[]) value).length == length);
    if (value instanceof short[]) return RuleResult.passes(((short[]) value).length == length);
    if (value instanceof char[]) return RuleResult.passes(((char[]) value).length == length);
    if (value instanceof int[]) return RuleResult.passes(((int[]) value).length == length);
    if (value instanceof long[]) return RuleResult.passes(((long[]) value).length == length);
    if (value instanceof float[]) return RuleResult.passes(((float[]) value).length == length);
    if (value instanceof double[]) return RuleResult.passes(((double[]) value).length == length);
    if (value instanceof Object[]) return RuleResult.passes(((Object[]) value).length == length);
    return RuleResult.reject();
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("length", length);
  }

  @Override
  public String getType() {
    return TekiErrors.LENGTH;
  }
}

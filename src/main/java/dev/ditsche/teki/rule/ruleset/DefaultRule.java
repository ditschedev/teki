package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.Map;

/**
 * Validates values with the Default rule.
 *
 * @author Tobias Dittmann
 */
public final class DefaultRule implements Rule {

  private final Object defaultValue;

  /**
   * Creates a DefaultRule.
   *
   * @param defaultValue default replacement value; must not be null
   */
  public DefaultRule(Object defaultValue) {
    if (defaultValue == null) throw new IllegalArgumentException("defaultValue must not be null");
    this.defaultValue = defaultValue;
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.resolve(defaultValue);
    if (!(defaultValue.getClass().isAssignableFrom(value.getClass()))) return RuleResult.reject();
    if (value instanceof String s && s.trim().isEmpty()) return RuleResult.resolve(defaultValue);
    return RuleResult.resolve();
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("valueType", defaultValue.getClass().getName());
  }

  @Override
  public String getType() {
    return TekiErrors.UNASSIGNABLE;
  }
}

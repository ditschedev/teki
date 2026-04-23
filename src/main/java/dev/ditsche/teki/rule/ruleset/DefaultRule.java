package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigDecimal;

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
    if (value instanceof String && ((String) value).trim().isEmpty())
      return RuleResult.resolve(defaultValue);
    if (value instanceof BigDecimal bd && bd.compareTo(BigDecimal.ZERO) == 0)
      return RuleResult.resolve(defaultValue);
    if (value instanceof Number && ((Number) value).longValue() == 0)
      return RuleResult.resolve(defaultValue);
    return RuleResult.resolve();
  }

  @Override
  public String message(String field) {
    return String.format(
        "The field \"%s\" cannot obtain a value of class \"%s\"",
        field, defaultValue.getClass().getName());
  }

  @Override
  public String getType() {
    return RULE_TYPE_PREFIX + "type.unassignable";
  }
}

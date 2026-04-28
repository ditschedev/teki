package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates values with the Array rule.
 *
 * @author Tobias Dittmann
 */
public final class ArrayRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.ARRAY;

  /** Creates a rule instance. */
  public ArrayRule() {}

  @Override
  public RuleResult test(Object value) {
    if (value != null && value.getClass().isArray()) return RuleResult.resolve();
    if (value instanceof Iterable) return RuleResult.resolve();
    return RuleResult.reject();
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.Collection;
import java.util.Map;

/**
 * Validates values with the Required rule.
 *
 * @author Tobias Dittmann
 */
public final class RequiredRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.REQUIRED;

  /** Creates a rule instance. */
  public RequiredRule() {}

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (value instanceof String) return RuleResult.passes(!((String) value).trim().isEmpty());
    if (value instanceof Collection) return RuleResult.passes(!((Collection<?>) value).isEmpty());
    if (value instanceof Map) return RuleResult.passes(!((Map<?, ?>) value).isEmpty());
    if (value instanceof boolean[]) return RuleResult.passes(((boolean[]) value).length != 0);
    if (value instanceof byte[]) return RuleResult.passes(((byte[]) value).length != 0);
    if (value instanceof short[]) return RuleResult.passes(((short[]) value).length != 0);
    if (value instanceof char[]) return RuleResult.passes(((char[]) value).length != 0);
    if (value instanceof int[]) return RuleResult.passes(((int[]) value).length != 0);
    if (value instanceof long[]) return RuleResult.passes(((long[]) value).length != 0);
    if (value instanceof float[]) return RuleResult.passes(((float[]) value).length != 0);
    if (value instanceof double[]) return RuleResult.passes(((double[]) value).length != 0);
    if (value instanceof Object[]) return RuleResult.passes(((Object[]) value).length != 0);
    return RuleResult.resolve();
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

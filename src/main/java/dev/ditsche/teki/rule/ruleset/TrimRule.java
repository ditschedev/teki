package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates values with the Trim rule.
 *
 * @author Tobias Dittmann
 */
public final class TrimRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.TRIM;

  /** Creates a rule instance. */
  public TrimRule() {}

  @Override
  public RuleResult test(Object value) {
    if (!(value instanceof String)) return RuleResult.reject();
    return RuleResult.resolve(((String) value).trim());
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

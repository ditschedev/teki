package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates values with the String rule.
 *
 * @author Tobias Dittmann
 */
public final class StringRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.STRING;

  /** Creates a rule instance. */
  public StringRule() {}

  @Override
  public RuleResult test(Object value) {
    return RuleResult.passes(value instanceof String);
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

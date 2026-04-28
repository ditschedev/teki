package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;

/**
 * Validates that the value is not null and not blank (not whitespace-only).
 *
 * @author Tobias Dittmann
 */
public final class NotBlankRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.NOT_BLANK;

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    return RuleResult.passes(!s.isBlank());
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

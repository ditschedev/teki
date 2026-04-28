package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the CreditCard rule.
 *
 * @author Tobias Dittmann
 */
public final class CreditCardRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.CREDIT_CARD;

  /** Creates a rule instance. */
  public CreditCardRule() {}

  // Backreference \\1 enforces consistent separator: all spaces, all hyphens, or none.
  private static final Pattern PATTERN =
      Pattern.compile("^\\d{4}([ \\-]?)\\d{4}\\1\\d{4}\\1\\d{4}$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    return RuleResult.passes(PATTERN.matcher(s).matches());
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

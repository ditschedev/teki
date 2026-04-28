package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the Phone rule (E.164 format).
 *
 * @author Tobias Dittmann
 */
public final class PhoneRule implements Rule {

  /** Creates a rule instance. */
  public PhoneRule() {}

  private static final Pattern PATTERN = Pattern.compile("^\\+[1-9]\\d{1,14}$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String)) return RuleResult.reject();
    return RuleResult.passes(PATTERN.matcher((String) value).matches());
  }

  @Override
  public String getType() {
    return TekiErrors.PHONE;
  }
}

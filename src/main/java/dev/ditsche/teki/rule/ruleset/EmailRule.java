package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the Email rule.
 *
 * @author Tobias Dittmann
 */
public final class EmailRule implements Rule {

  /** Creates a rule instance. */
  public EmailRule() {}

  private static final Pattern PATTERN =
      Pattern.compile(
          "^[A-Za-z0-9][A-Za-z0-9._%'+\\-]*@[A-Za-z0-9][A-Za-z0-9.\\-]*\\.[A-Za-z]{2,}$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String)) return RuleResult.reject();
    return RuleResult.passes(PATTERN.matcher((String) value).matches());
  }

  @Override
  public String getType() {
    return TekiErrors.EMAIL;
  }
}

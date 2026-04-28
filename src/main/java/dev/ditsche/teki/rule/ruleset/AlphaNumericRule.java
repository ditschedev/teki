package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the AlphaNumeric rule.
 *
 * @author Tobias Dittmann
 */
public final class AlphaNumericRule implements Rule {

  /** Creates a rule instance. */
  public AlphaNumericRule() {}

  private static final Pattern PATTERN = Pattern.compile("[a-zA-Z0-9]+");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    return RuleResult.passes(PATTERN.matcher(s).matches());
  }

  @Override
  public String getType() {
    return TekiErrors.ALPHA_NUMERIC;
  }
}

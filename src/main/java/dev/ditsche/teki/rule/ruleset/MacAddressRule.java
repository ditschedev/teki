package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the MAC address rule.
 *
 * @author Tobias Dittmann
 */
public final class MacAddressRule implements Rule {

  /** Creates a rule instance. */
  public MacAddressRule() {}

  // Back-reference \2 ensures separator is consistent throughout
  private static final Pattern PATTERN =
      Pattern.compile("^([0-9A-Fa-f]{2}([:-]))(?:[0-9A-Fa-f]{2}\\2){4}[0-9A-Fa-f]{2}$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String)) return RuleResult.reject();
    return RuleResult.passes(PATTERN.matcher((String) value).matches());
  }

  @Override
  public String getType() {
    return TekiErrors.MAC_ADDRESS;
  }
}

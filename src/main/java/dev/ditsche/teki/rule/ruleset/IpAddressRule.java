package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the IpAddress rule.
 *
 * @author Tobias Dittmann
 */
public final class IpAddressRule implements Rule {

  /** Creates a rule instance. */
  public IpAddressRule() {}

  private static final Pattern IPV4 =
      Pattern.compile(
          "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

  private static final Pattern IPV6 =
      Pattern.compile(
          "^("
              + "([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|"
              + "([0-9a-fA-F]{1,4}:){1,7}:|"
              + "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|"
              + "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|"
              + "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|"
              + "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|"
              + "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|"
              + "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|"
              + ":((:[0-9a-fA-F]{1,4}){1,7}|:)|"
              + "::"
              + ")$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    return RuleResult.passes(IPV4.matcher(s).matches() || IPV6.matcher(s).matches());
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" needs to be a valid IPv4 or IPv6 address", field);
  }

  @Override
  public String getType() {
    return "format.ip";
  }
}

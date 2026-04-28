package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the IpAddress rule.
 *
 * @author Tobias Dittmann
 */
public final class IpAddressRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.IP_ADDRESS;

  private final int version; // 0 = any, 4 = IPv4 only, 6 = IPv6 only

  /** Accepts any valid IPv4 or IPv6 address. */
  public IpAddressRule() {
    this.version = 0;
  }

  /**
   * Accepts only addresses of the specified IP version.
   *
   * @param version 4 for IPv4 only, 6 for IPv6 only
   */
  public IpAddressRule(int version) {
    if (version != 4 && version != 6)
      throw new IllegalArgumentException("IP version must be 4 or 6");
    this.version = version;
  }

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
    return switch (version) {
      case 4 -> RuleResult.passes(IPV4.matcher(s).matches());
      case 6 -> RuleResult.passes(IPV6.matcher(s).matches());
      default -> RuleResult.passes(IPV4.matcher(s).matches() || IPV6.matcher(s).matches());
    };
  }

  @Override
  public String message(String field) {
    return switch (version) {
      case 4 -> String.format("The field \"%s\" needs to be a valid IPv4 address", field);
      case 6 -> String.format("The field \"%s\" needs to be a valid IPv6 address", field);
      default -> String.format("The field \"%s\" needs to be a valid IPv4 or IPv6 address", field);
    };
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

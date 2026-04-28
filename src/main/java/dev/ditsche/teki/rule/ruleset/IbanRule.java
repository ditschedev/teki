package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Validates values with the IBAN rule (ISO 7064 MOD-97-10).
 *
 * @author Tobias Dittmann
 */
public final class IbanRule implements Rule {

  /** Creates a rule instance. */
  public IbanRule() {}

  private static final Pattern FORMAT = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    s = s.replace(" ", "");
    if (!FORMAT.matcher(s).matches()) return RuleResult.reject();
    return RuleResult.passes(mod97(s) == 1);
  }

  private static int mod97(String iban) {
    // Move first 4 chars to end, then replace each letter with its numeric value
    String rearranged = iban.substring(4) + iban.substring(0, 4);
    StringBuilder numeric = new StringBuilder();
    for (char c : rearranged.toCharArray()) {
      if (Character.isLetter(c)) numeric.append(c - 'A' + 10);
      else numeric.append(c);
    }
    return new BigInteger(numeric.toString()).mod(BigInteger.valueOf(97)).intValue();
  }

  @Override
  public String getType() {
    return TekiErrors.IBAN;
  }
}

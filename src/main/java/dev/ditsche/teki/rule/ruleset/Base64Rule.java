package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.MessageResolver;
import dev.ditsche.teki.Teki;
import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the Base64 rule (standard or URL-safe variant).
 *
 * @author Tobias Dittmann
 */
public final class Base64Rule implements Rule {

  private static final Pattern STANDARD = Pattern.compile("^[A-Za-z0-9+/]*={0,2}$");
  private static final Pattern URL_SAFE = Pattern.compile("^[A-Za-z0-9_-]+$");

  private final boolean urlSafe;

  /** Creates a standard base64 rule instance. */
  public Base64Rule() {
    this.urlSafe = false;
  }

  /**
   * Creates a base64 rule instance.
   *
   * @param urlSafe when true, validates URL-safe base64 (no padding, {@code -_} alphabet)
   */
  public Base64Rule(boolean urlSafe) {
    this.urlSafe = urlSafe;
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    if (s.isEmpty()) return RuleResult.passes(true);
    if (urlSafe) return RuleResult.passes(URL_SAFE.matcher(s).matches());
    return RuleResult.passes(STANDARD.matcher(s).matches() && s.length() % 4 == 0);
  }

  @Override
  public String getType() {
    return TekiErrors.BASE64;
  }

  @Override
  public String message(String field) {
    MessageResolver resolver = Teki.getGlobalResolver();
    String msg = resolver != null ? resolver.resolve(field, getType(), params()) : null;
    if (msg != null) return msg;
    if (urlSafe)
      return String.format(
          "The field \"%s\" must be a valid URL-safe base64-encoded string", field);
    return String.format("The field \"%s\" must be a valid base64-encoded string", field);
  }
}

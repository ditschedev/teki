package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the Url rule.
 *
 * @author Tobias Dittmann
 */
public final class UrlRule implements Rule {

  public static final String TYPE_KEY = TekiErrors.URL;

  /** Creates a rule instance. */
  public UrlRule() {}

  private static final Pattern PATTERN =
      Pattern.compile(
          "^(ht|f)tps?://[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:\\d+)?(/?)([a-zA-Z0-9\\-.?,:'/\\\\+=&;%$#_]*)?$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String)) return RuleResult.reject();
    return RuleResult.passes(PATTERN.matcher((String) value).matches());
  }

  @Override
  public String getType() {
    return TYPE_KEY;
  }
}

package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the Url rule.
 *
 * @author Tobias Dittmann
 */
public final class UrlRule implements Rule {

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
  public String message(String field) {
    return String.format("The field \"%s\" needs to be a valid url", field);
  }

  @Override
  public String getType() {
    return RULE_TYPE_PREFIX + "format.url";
  }
}

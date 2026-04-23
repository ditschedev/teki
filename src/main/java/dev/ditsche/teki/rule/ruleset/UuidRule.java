package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates that the value is a well-formed UUID.
 *
 * @author Tobias Dittmann
 */
public final class UuidRule implements Rule {

  private static final Pattern PATTERN =
      Pattern.compile(
          "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    return RuleResult.passes(PATTERN.matcher(s).matches());
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" must be a valid UUID", field);
  }

  @Override
  public String getType() {
    return RULE_TYPE_PREFIX + "format.uuid";
  }
}

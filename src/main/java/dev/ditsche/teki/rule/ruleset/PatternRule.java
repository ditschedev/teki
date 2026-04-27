package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.regex.Pattern;

/**
 * Validates values with the Pattern rule.
 *
 * @author Tobias Dittmann
 */
public final class PatternRule implements Rule {

  private final Pattern compiled;

  /**
   * Creates a PatternRule.
   *
   * @param pattern regular expression pattern
   */
  public PatternRule(String pattern) {
    this.compiled = Pattern.compile(pattern);
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    return RuleResult.passes(compiled.matcher(s).matches());
  }

  @Override
  public String message(String field) {
    return String.format("The field \"%s\" has an invalid format", field);
  }

  @Override
  public String getType() {
    return "format.pattern";
  }
}

package dev.ditsche.teki.rule;

/**
 * Describes the result of applying a validation rule.
 *
 * @author Tobias Dittmann
 */
public record RuleResult(boolean passed, Object value, boolean changed) {

  public static RuleResult passes(boolean passes) {
    return new RuleResult(passes, null, false);
  }

  public static RuleResult resolve() {
    return new RuleResult(true, null, false);
  }

  public static RuleResult resolve(Object object) {
    return new RuleResult(true, object, true);
  }

  public static RuleResult reject() {
    return new RuleResult(false, null, false);
  }
}

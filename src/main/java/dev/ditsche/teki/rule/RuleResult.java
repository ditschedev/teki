package dev.ditsche.teki.rule;

/**
 * Describes the result of applying a validation rule.
 *
 * @author Tobias Dittmann
 */
public final class RuleResult {

  private final boolean passed;
  private final Object value;
  private final boolean changed;

  private RuleResult(boolean passed, Object value, boolean changed) {
    this.passed = passed;
    this.value = value;
    this.changed = changed;
  }

  /**
   * Creates a result that either passes or rejects without transforming the value.
   *
   * @param passes whether the rule passed
   * @return rule result
   */
  public static RuleResult passes(boolean passes) {
    return new RuleResult(passes, null, false);
  }

  /**
   * Creates a passing result without transforming the value.
   *
   * @return passing rule result
   */
  public static RuleResult resolve() {
    return new RuleResult(true, null, false);
  }

  /**
   * Creates a passing result that replaces the current value.
   *
   * @param object transformed value
   * @return passing rule result carrying the transformed value
   */
  public static RuleResult resolve(Object object) {
    return new RuleResult(true, object, true);
  }

  /**
   * Creates a failing result.
   *
   * @return failing rule result
   */
  public static RuleResult reject() {
    return new RuleResult(false, null, false);
  }

  /**
   * Reports whether the rule passed.
   *
   * @return {@code true} when the rule passed
   */
  public boolean isPassed() {
    return passed;
  }

  /**
   * Returns the transformed value, when present.
   *
   * @return transformed value or {@code null}
   */
  public Object getValue() {
    return value;
  }

  /**
   * Reports whether this result carries a transformed value.
   *
   * @return {@code true} when the value changed
   */
  public boolean isChanged() {
    return changed;
  }
}

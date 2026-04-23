package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.rule.Rule;
import java.util.List;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public abstract sealed class RuleBuilder implements Builder
    permits ArrayElementRuleBuilder,
        ArrayRuleBuilder,
        BooleanRuleBuilder,
        NumberRuleBuilder,
        StringRuleBuilder,
        TemporalRuleBuilder {

  /** Rules accumulated by this builder. */
  protected List<Rule> rules;

  /** Creates a rule builder. */
  protected RuleBuilder() {}

  /**
   * Adds a custom validation rule.
   *
   * @param rule custom rule to add
   * @return this builder for chaining
   */
  public abstract RuleBuilder custom(Rule rule);
}

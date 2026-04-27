package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.ruleset.BooleanRule;
import dev.ditsche.teki.rule.ruleset.RequiredRule;
import dev.ditsche.teki.validation.Validatable;
import dev.ditsche.teki.validation.ValidationField;
import java.util.LinkedList;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public final class BooleanRuleBuilder extends RuleBuilder {

  private final String field;
  private boolean optional = false;

  BooleanRuleBuilder(String field) {
    this.field = field;
    this.rules = new LinkedList<>();
  }

  /**
   * Requires the boolean value to be true.
   *
   * @return this builder for chaining
   */
  public BooleanRuleBuilder isTrue() {
    this.rules.add(new BooleanRule(true));
    return this;
  }

  /**
   * Requires the boolean value to be false.
   *
   * @return this builder for chaining
   */
  public BooleanRuleBuilder isFalse() {
    this.rules.add(new BooleanRule(false));
    return this;
  }

  /**
   * Marks the value as required.
   *
   * @return this builder for chaining
   */
  public BooleanRuleBuilder required() {
    this.rules.add(new RequiredRule());
    return this;
  }

  /**
   * Allows the value to be absent or null.
   *
   * @return this builder for chaining
   */
  public BooleanRuleBuilder optional() {
    this.optional = true;
    return this;
  }

  @Override
  public RuleBuilder custom(Rule rule) {
    this.rules.add(rule);
    return this;
  }

  @Override
  public Validatable build() {
    return new ValidationField(this.field, this.rules, this.optional);
  }
}

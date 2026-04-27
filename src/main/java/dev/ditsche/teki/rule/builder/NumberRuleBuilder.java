package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.ruleset.*;
import dev.ditsche.teki.validation.Validatable;
import dev.ditsche.teki.validation.ValidationField;
import java.util.LinkedList;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public final class NumberRuleBuilder extends RuleBuilder {

  private final String field;
  private boolean optional = false;

  NumberRuleBuilder(String field) {
    this.field = field;
    this.rules = new LinkedList<>();
    this.rules.add(new NumberRule());
  }

  /**
   * Adds a minimum value or size constraint.
   *
   * @param min minimum allowed value
   * @return this builder for chaining
   */
  public NumberRuleBuilder min(long min) {
    this.rules.add(new MinRule(min));
    return this;
  }

  /**
   * Adds a maximum value or size constraint.
   *
   * @param max maximum allowed value
   * @return this builder for chaining
   */
  public NumberRuleBuilder max(long max) {
    this.rules.add(new MaxRule(max));
    return this;
  }

  /**
   * Adds an inclusive range constraint.
   *
   * @param min minimum allowed value
   * @param max maximum allowed value
   * @return this builder for chaining
   */
  public NumberRuleBuilder between(long min, long max) {
    this.rules.add(new SizeRule(min, max));
    return this;
  }

  /**
   * Adds an exact length constraint.
   *
   * @param length required length
   * @return this builder for chaining
   */
  public NumberRuleBuilder length(int length) {
    this.rules.add(new LengthRule(length));
    return this;
  }

  /**
   * Adds a default value transformation for null values.
   *
   * @param number default numeric value
   * @return this builder for chaining
   */
  public NumberRuleBuilder defaultValue(Number number) {
    this.rules.add(new DefaultRule(number));
    return this;
  }

  /**
   * Requires the value to be strictly greater than zero.
   *
   * @return this builder for chaining
   */
  public NumberRuleBuilder positive() {
    this.rules.add(new PositiveRule());
    return this;
  }

  /**
   * Requires the value to be greater than or equal to zero.
   *
   * @return this builder for chaining
   */
  public NumberRuleBuilder positiveOrZero() {
    this.rules.add(new PositiveOrZeroRule());
    return this;
  }

  /**
   * Requires the value to be strictly less than zero.
   *
   * @return this builder for chaining
   */
  public NumberRuleBuilder negative() {
    this.rules.add(new NegativeRule());
    return this;
  }

  /**
   * Requires the value to be less than or equal to zero.
   *
   * @return this builder for chaining
   */
  public NumberRuleBuilder negativeOrZero() {
    this.rules.add(new NegativeOrZeroRule());
    return this;
  }

  /**
   * Marks the value as required.
   *
   * @return this builder for chaining
   */
  public NumberRuleBuilder required() {
    this.rules.add(new RequiredRule());
    return this;
  }

  /**
   * Allows the value to be absent or null.
   *
   * @return this builder for chaining
   */
  public NumberRuleBuilder optional() {
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

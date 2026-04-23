package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.ruleset.*;
import dev.ditsche.teki.validation.Validatable;
import dev.ditsche.teki.validation.ValidationArray;
import java.util.LinkedList;
import java.util.List;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public final class ArrayElementRuleBuilder extends RuleBuilder {

  private final String field;
  private final List<Rule> children;
  private final boolean optional;

  ArrayElementRuleBuilder(String field, List<Rule> rules, boolean optional) {
    this.field = field;
    this.rules = rules;
    this.optional = optional;
    this.children = new LinkedList<>();
  }

  /**
   * Marks the value as required.
   *
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder required() {
    this.children.add(new RequiredRule());
    return this;
  }

  /**
   * Returns string.
   *
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder string() {
    this.children.add(new StringRule());
    return this;
  }

  /**
   * Returns number.
   *
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder number() {
    this.children.add(new NumberRule());
    return this;
  }

  /**
   * Returns bool.
   *
   * @param value expected or replacement value
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder bool(boolean value) {
    this.children.add(new BooleanRule(value));
    return this;
  }

  /**
   * Adds a minimum value or size constraint.
   *
   * @param min minimum allowed value
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder min(int min) {
    this.children.add(new MinRule(min));
    return this;
  }

  /**
   * Adds a maximum value or size constraint.
   *
   * @param max maximum allowed value
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder max(int max) {
    this.children.add(new MaxRule(max));
    return this;
  }

  /**
   * Adds an exact length constraint.
   *
   * @param length required length
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder length(int length) {
    this.children.add(new LengthRule(length));
    return this;
  }

  /**
   * Adds an inclusive size range constraint.
   *
   * @param min minimum allowed value
   * @param max maximum allowed value
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder size(int min, int max) {
    this.children.add(new SizeRule(min, max));
    return this;
  }

  @Override
  public RuleBuilder custom(Rule rule) {
    this.children.add(rule);
    return this;
  }

  @Override
  public Validatable build() {
    return new ValidationArray(this.field, this.rules, this.children, null, this.optional);
  }
}

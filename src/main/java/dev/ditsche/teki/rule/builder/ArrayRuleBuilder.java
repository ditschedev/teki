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
public final class ArrayRuleBuilder extends RuleBuilder {

  private final String field;
  private List<Rule> childRules;
  private List<Validatable> childValidatable;
  private boolean optional;

  ArrayRuleBuilder(String field) {
    this.field = field;
    this.rules = new LinkedList<>();
    this.rules.add(new ArrayRule());
  }

  /**
   * Marks the value as required.
   *
   * @return this builder for chaining
   */
  public ArrayRuleBuilder required() {
    this.rules.add(new RequiredRule());
    return this;
  }

  /**
   * Adds an exact length constraint.
   *
   * @param length required length
   * @return this builder for chaining
   */
  public ArrayRuleBuilder length(int length) {
    this.rules.add(new LengthRule(length));
    return this;
  }

  /**
   * Adds a minimum value or size constraint.
   *
   * @param min minimum allowed value
   * @return this builder for chaining
   */
  public ArrayRuleBuilder min(int min) {
    this.rules.add(new MinRule(min));
    return this;
  }

  /**
   * Adds a maximum value or size constraint.
   *
   * @param max maximum allowed value
   * @return this builder for chaining
   */
  public ArrayRuleBuilder max(int max) {
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
  public ArrayRuleBuilder between(int min, int max) {
    this.rules.add(new SizeRule(min, max));
    return this;
  }

  /**
   * Starts a builder for validating array elements.
   *
   * @return this builder for chaining
   */
  public ArrayElementRuleBuilder elements() {
    return new ArrayElementRuleBuilder(this.field, this.rules, this.optional);
  }

  /**
   * Validates each array element as an object with the supplied builders.
   *
   * @param builders builders to add
   * @return this builder for chaining
   */
  public ArrayRuleBuilder objects(Builder... builders) {
    this.childRules = null;
    this.childValidatable = new LinkedList<>();
    for (Builder builder : builders) {
      this.childValidatable.add(builder.build());
    }
    return this;
  }

  /**
   * Allows the value to be absent or null.
   *
   * @return this builder for chaining
   */
  public ArrayRuleBuilder optional() {
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
    return new ValidationArray(
        this.field, this.rules, this.childRules, this.childValidatable, this.optional);
  }
}

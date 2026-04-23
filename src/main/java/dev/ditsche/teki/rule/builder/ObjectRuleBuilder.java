package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.validation.Validatable;
import dev.ditsche.teki.validation.ValidationObject;
import java.util.LinkedList;
import java.util.List;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public final class ObjectRuleBuilder implements Builder {

  private final String field;
  private boolean optional;
  private final List<Validatable> children;

  ObjectRuleBuilder(String field) {
    this.field = field;
    this.children = new LinkedList<>();
  }

  /**
   * Adds a nested child validation component.
   *
   * @param builder builder to add
   * @return this builder for chaining
   */
  public ObjectRuleBuilder child(Builder builder) {
    this.children.add(builder.build());
    return this;
  }

  /**
   * Adds a nested child validation component.
   *
   * @param validatable validation component
   * @return this builder for chaining
   */
  public ObjectRuleBuilder child(Validatable validatable) {
    this.children.add(validatable);
    return this;
  }

  /**
   * Adds nested child field builders.
   *
   * @param builders builders to add
   * @return this builder for chaining
   */
  public ObjectRuleBuilder fields(Builder... builders) {
    for (Builder builder : builders) {
      this.children.add(builder.build());
    }
    return this;
  }

  /**
   * Allows the value to be absent or null.
   *
   * @return this builder for chaining
   */
  public ObjectRuleBuilder optional() {
    this.optional = true;
    return this;
  }

  @Override
  public Validatable build() {
    return new ValidationObject(this.field, this.children, this.optional);
  }
}

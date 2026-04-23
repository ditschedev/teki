package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.validation.Validatable;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public sealed interface Builder permits ObjectRuleBuilder, RuleBuilder {

  /**
   * Builds an executable validation component from this builder.
   *
   * @return validation component
   */
  Validatable build();
}

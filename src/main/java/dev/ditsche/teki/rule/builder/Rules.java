package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.FieldSelector;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public final class Rules {

  private Rules() {}

  /**
   * Starts a string rule builder for the selected field.
   *
   * @param field field name
   * @return this builder for chaining
   */
  public static StringRuleBuilder string(String field) {
    return new StringRuleBuilder(field);
  }

  /**
   * Starts a string rule builder for the selected field.
   *
   * @param <T> declaring type for the selected accessor
   * @param <R> selected field value type
   * @param field method reference used to resolve the field name
   * @return this builder for chaining
   */
  public static <T, R> StringRuleBuilder string(FieldSelector<T, R> field) {
    return string(FieldSelectors.resolve(field));
  }

  /**
   * Starts a number rule builder for the selected field.
   *
   * @param field field name
   * @return this builder for chaining
   */
  public static NumberRuleBuilder number(String field) {
    return new NumberRuleBuilder(field);
  }

  /**
   * Starts a number rule builder for the selected field.
   *
   * @param <T> declaring type for the selected accessor
   * @param <R> selected field value type
   * @param field method reference used to resolve the field name
   * @return this builder for chaining
   */
  public static <T, R> NumberRuleBuilder number(FieldSelector<T, R> field) {
    return number(FieldSelectors.resolve(field));
  }

  /**
   * Starts an object rule builder for the selected field.
   *
   * @param field field name
   * @return this builder for chaining
   */
  public static ObjectRuleBuilder object(String field) {
    return new ObjectRuleBuilder(field);
  }

  /**
   * Starts an object rule builder for the selected field.
   *
   * @param <T> declaring type for the selected accessor
   * @param <R> selected field value type
   * @param field method reference used to resolve the field name
   * @return this builder for chaining
   */
  public static <T, R> ObjectRuleBuilder object(FieldSelector<T, R> field) {
    return object(FieldSelectors.resolve(field));
  }

  /**
   * Starts a boolean rule builder for the selected field.
   *
   * @param field field name
   * @return this builder for chaining
   */
  public static BooleanRuleBuilder bool(String field) {
    return new BooleanRuleBuilder(field);
  }

  /**
   * Starts a boolean rule builder for the selected field.
   *
   * @param <T> declaring type for the selected accessor
   * @param <R> selected field value type
   * @param field method reference used to resolve the field name
   * @return this builder for chaining
   */
  public static <T, R> BooleanRuleBuilder bool(FieldSelector<T, R> field) {
    return bool(FieldSelectors.resolve(field));
  }

  /**
   * Starts an array rule builder for the selected field.
   *
   * @param field field name
   * @return this builder for chaining
   */
  public static ArrayRuleBuilder array(String field) {
    return new ArrayRuleBuilder(field);
  }

  /**
   * Starts an array rule builder for the selected field.
   *
   * @param <T> declaring type for the selected accessor
   * @param <R> selected field value type
   * @param field method reference used to resolve the field name
   * @return this builder for chaining
   */
  public static <T, R> ArrayRuleBuilder array(FieldSelector<T, R> field) {
    return array(FieldSelectors.resolve(field));
  }

  /**
   * Starts a temporal rule builder for the selected field.
   *
   * @param field field name
   * @return this builder for chaining
   */
  public static TemporalRuleBuilder temporal(String field) {
    return new TemporalRuleBuilder(field);
  }

  /**
   * Starts a temporal rule builder for the selected field.
   *
   * @param <T> declaring type for the selected accessor
   * @param <R> selected field value type
   * @param field method reference used to resolve the field name
   * @return this builder for chaining
   */
  public static <T, R> TemporalRuleBuilder temporal(FieldSelector<T, R> field) {
    return temporal(FieldSelectors.resolve(field));
  }
}

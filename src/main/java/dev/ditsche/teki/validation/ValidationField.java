package dev.ditsche.teki.validation;

import dev.ditsche.teki.MessageResolver;
import dev.ditsche.teki.error.ErrorBag;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import dev.ditsche.teki.rule.ruleset.RequiredRule;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an internal validation component used by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ValidationField implements Validatable {

  private final String field;
  private final boolean optional;
  private final List<Rule> rules;

  /**
   * Creates a ValidationField.
   *
   * @param field field name
   */
  public ValidationField(String field) {
    this(field, new LinkedList<>(), false);
  }

  /**
   * Creates a ValidationField.
   *
   * @param field field name
   * @param rules rules to apply
   */
  public ValidationField(String field, List<Rule> rules) {
    this(field, rules, false);
  }

  /**
   * Creates a ValidationField.
   *
   * @param field field name
   * @param rules rules to apply
   * @param optional whether null or missing values should be ignored
   */
  public ValidationField(String field, List<Rule> rules, boolean optional) {
    if (field == null || field.trim().isEmpty())
      throw new IllegalArgumentException("Validation field requires a valid field name");
    if (rules == null) throw new IllegalArgumentException("Validation rules cannot be null");
    this.field = field;
    this.rules = rules;
    this.optional = optional;
  }

  /**
   * Adds a rule to this validation field.
   *
   * @param rule custom rule to add
   */
  public void addRule(Rule rule) {
    this.rules.remove(rule);
    this.rules.add(rule);
  }

  @Override
  public String getField() {
    return field;
  }

  /**
   * Reports whether null or missing values should be ignored.
   *
   * @return whether this field is optional
   */
  public boolean isOptional() {
    return optional;
  }

  /**
   * Returns the rules applied to the value.
   *
   * @return validation rules
   */
  public List<Rule> getRules() {
    return rules;
  }

  @Override
  public ValidationResult validate(String parent, Object object, boolean abortEarly, MessageResolver resolver) {
    ErrorBag errorBag = new ErrorBag();
    boolean changed = false;
    if (optional && !(new RequiredRule().test(object).isPassed()))
      return new ValidationResult(errorBag, object, false);
    for (Rule rule : rules) {
      RuleResult ruleResult = rule.test(object);
      if (!ruleResult.isPassed()) {
        String msg = resolver != null ? resolver.resolve(field, rule.getType()) : null;
        if (msg == null) msg = rule.message(field);
        errorBag.add(parent + field, rule.getType(), msg);
        if (abortEarly) throw new ValidationException(errorBag);
      } else if (ruleResult.isChanged()) {
        changed = true;
        object = ruleResult.getValue();
      }
    }
    return new ValidationResult(errorBag, object, changed);
  }
}

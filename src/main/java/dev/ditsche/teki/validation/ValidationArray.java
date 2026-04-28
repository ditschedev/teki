package dev.ditsche.teki.validation;

import dev.ditsche.teki.MessageResolver;
import dev.ditsche.teki.error.ErrorBag;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import dev.ditsche.teki.rule.ruleset.RequiredRule;
import java.util.List;

/**
 * Represents an internal validation component used by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ValidationArray implements Validatable {

  private final String field;
  private final boolean optional;
  private final List<Rule> rules;
  private final List<Rule> childRules;
  private final List<Validatable> validatables;

  /**
   * Creates an array validation component.
   *
   * @param field field name
   * @param rules rules applied to the array value
   * @param childRules rules applied to each array element
   * @param validatables validation components applied to child objects
   */
  public ValidationArray(
      String field, List<Rule> rules, List<Rule> childRules, List<Validatable> validatables) {
    this(field, rules, childRules, validatables, false);
  }

  /**
   * Creates an array validation component.
   *
   * @param field field name
   * @param rules rules applied to the array value
   * @param childRules rules applied to each array element
   * @param validatables validation components applied to child objects
   * @param optional whether null or missing values should be ignored
   */
  public ValidationArray(
      String field,
      List<Rule> rules,
      List<Rule> childRules,
      List<Validatable> validatables,
      boolean optional) {
    this.field = field;
    this.rules = rules;
    this.childRules = childRules;
    this.validatables = validatables;
    this.optional = optional;
  }

  @Override
  public String getField() {
    return field;
  }

  /**
   * Returns the rules applied to the value.
   *
   * @return validation rules
   */
  public List<Rule> getRules() {
    return rules;
  }

  /**
   * Returns the rules applied to child values.
   *
   * @return child rules
   */
  public List<Rule> getChildRules() {
    return childRules;
  }

  /**
   * Returns child validation components.
   *
   * @return child validation components
   */
  public List<Validatable> getValidatables() {
    return validatables;
  }

  @Override
  public ValidationResult validate(
      String parent, Object object, boolean abortEarly, MessageResolver resolver) {
    ErrorBag errorBag = new ErrorBag();
    boolean changed = false;
    if (optional && !(new RequiredRule().test(object).passed()))
      return new ValidationResult(errorBag, object, false);

    for (Rule rule : rules) {
      RuleResult ruleResult = rule.test(object);
      if (!ruleResult.passed()) {
        String msg =
            resolver != null ? resolver.resolve(field, rule.getType(), rule.params()) : null;
        if (msg == null) msg = rule.message(field);
        errorBag.add(parent + field, rule.getType(), msg);
        if (abortEarly) throw new ValidationException(errorBag);
      } else if (ruleResult.changed()) {
        changed = true;
        object = ruleResult.value();
      }
    }

    Iterable<?> iterable;
    if (object instanceof Iterable) {
      iterable = (Iterable<?>) object;
    } else if (object != null && object.getClass().isArray()) {
      iterable = java.util.Arrays.asList((Object[]) object);
    } else {
      iterable = List.of();
    }
    int index = 0;
    for (Object element : iterable) {
      String elementPath = parent + field + "[" + index + "]";
      if (childRules != null) {
        for (Rule rule : childRules) {
          RuleResult ruleResult = rule.test(element);
          if (!ruleResult.passed()) {
            String msg =
                resolver != null ? resolver.resolve(field, rule.getType(), rule.params()) : null;
            if (msg == null) msg = rule.message(field);
            errorBag.add(elementPath, rule.getType(), msg);
            if (abortEarly) throw new ValidationException(errorBag);
          } else if (ruleResult.changed()) {
            changed = true;
            element = ruleResult.value();
          }
        }
      } else if (validatables != null) {
        for (Validatable validatable : validatables) {
          ValidationResult validationResult =
              validatable.validate(elementPath + ".", element, abortEarly, resolver);
          errorBag.merge(validationResult.getErrorBag());
        }
      }
      index++;
    }

    return new ValidationResult(errorBag, object, changed);
  }
}

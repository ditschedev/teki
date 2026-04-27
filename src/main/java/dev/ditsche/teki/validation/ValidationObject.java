package dev.ditsche.teki.validation;

import dev.ditsche.teki.MessageResolver;
import dev.ditsche.teki.error.ErrorBag;
import dev.ditsche.teki.rule.ruleset.RequiredRule;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an internal validation component used by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ValidationObject implements Validatable {

  private final String field;
  private final boolean optional;
  private final List<Validatable> children;

  /**
   * Creates a ValidationObject.
   *
   * @param field field name
   * @param children child validation components
   */
  public ValidationObject(String field, List<Validatable> children) {
    this(field, children, false);
  }

  /**
   * Creates a ValidationObject.
   *
   * @param field field name
   * @param children child validation components
   * @param optional whether null or missing values should be ignored
   */
  public ValidationObject(String field, List<Validatable> children, boolean optional) {
    this.field = field;
    this.children = children;
    this.optional = optional;
  }

  @Override
  public String getField() {
    return field;
  }

  /**
   * Returns child validation components.
   *
   * @return child validation components
   */
  public List<Validatable> getChildren() {
    return children;
  }

  @Override
  public ValidationResult validate(String parent, Object object, boolean abortEarly, MessageResolver resolver) {
    ErrorBag errorBag = new ErrorBag();
    boolean changed = false;
    if (optional && !(new RequiredRule().test(object).isPassed()))
      return new ValidationResult(errorBag, object, false);

    List<Field> fieldSet = FieldAccess.getAllFields(object.getClass());
    boolean record = object.getClass().isRecord();
    Map<String, Object> recordChanges = record ? new HashMap<>() : Map.of();

    for (Validatable validatable : children) {
      Field f = FieldAccess.findField(fieldSet, validatable.getField());
      if (f == null) continue;
      Object value = FieldAccess.read(object, f);
      ValidationResult result = validatable.validate(this.field + ".", value, abortEarly, resolver);
      errorBag.merge(result.getErrorBag());
      if (result.isChanged()) {
        changed = true;
        if (record) {
          recordChanges.put(f.getName(), result.getValue());
        } else {
          FieldAccess.write(object, f, result.getValue());
        }
      }
    }
    if (record && changed && errorBag.isEmpty()) {
      object = FieldAccess.reconstructRecord(object, recordChanges);
    }
    return new ValidationResult(errorBag, object, changed);
  }
}

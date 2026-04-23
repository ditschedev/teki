package dev.ditsche.teki.error;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents validation error state reported by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ValidationError {

  private final String field;
  private final List<ValidationErrorInfo> errors;

  /**
   * Creates a ValidationError.
   *
   * @param field field name
   */
  public ValidationError(String field) {
    this(field, new LinkedList<>());
  }

  /**
   * Creates a ValidationError.
   *
   * @param field field name
   * @param errors validation error details
   */
  public ValidationError(String field, List<ValidationErrorInfo> errors) {
    this.field = field;
    this.errors = errors;
  }

  /**
   * Returns the validated field name.
   *
   * @return validated field name
   */
  public String getField() {
    return field;
  }

  /**
   * Returns validation error details.
   *
   * @return validation errors
   */
  public List<ValidationErrorInfo> getErrors() {
    return errors;
  }
}

package dev.ditsche.teki.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents validation error state reported by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ErrorBag {

  private HashMap<String, ValidationError> errors;

  /** Creates an empty error bag. */
  public ErrorBag() {
    clear();
  }

  /**
   * Adds validation error information.
   *
   * @param field field name
   * @param type validation error type
   * @param message validation error message
   */
  public void add(String field, String type, String message) {
    add(new ValidationError(field, List.of(new ValidationErrorInfo(message, type))));
  }

  /**
   * Adds validation error information.
   *
   * @param validationError validation error to add
   */
  public void add(ValidationError validationError) {
    List<ValidationErrorInfo> errorList =
        errors
            .getOrDefault(
                validationError.getField(), new ValidationError(validationError.getField()))
            .getErrors();
    errorList.addAll(validationError.getErrors());
    errors.put(
        validationError.getField(), new ValidationError(validationError.getField(), errorList));
  }

  /**
   * Reports whether there are no validation errors.
   *
   * @return whether the bag is empty
   */
  public boolean isEmpty() {
    return errors.isEmpty();
  }

  /** Removes all collected validation errors. */
  public void clear() {
    this.errors = new HashMap<>();
  }

  /**
   * Merges another error bag into this one.
   *
   * @param errorBag error bag to merge or expose
   */
  public void merge(ErrorBag errorBag) {
    for (ValidationError error : errorBag.getErrors().values()) {
      add(error);
    }
  }

  /**
   * Returns validation error details.
   *
   * @return validation errors
   */
  public Map<String, ValidationError> getErrors() {
    return errors;
  }
}

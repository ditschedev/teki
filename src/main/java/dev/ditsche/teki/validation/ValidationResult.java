package dev.ditsche.teki.validation;

import dev.ditsche.teki.error.ErrorBag;

/**
 * Represents an internal validation component used by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ValidationResult {

  private final ErrorBag errorBag;
  private final Object value;
  private final boolean changed;

  /**
   * Creates a ValidationResult.
   *
   * @param errorBag error bag to merge or expose
   * @param value expected or replacement value
   * @param changed whether the value changed
   */
  public ValidationResult(ErrorBag errorBag, Object value, boolean changed) {
    this.errorBag = errorBag;
    this.value = value;
    this.changed = changed;
  }

  /**
   * Returns the collected validation errors.
   *
   * @return collected errors
   */
  public ErrorBag getErrorBag() {
    return errorBag;
  }

  /**
   * Returns the transformed value.
   *
   * @return transformed value
   */
  public Object getValue() {
    return value;
  }

  /**
   * Reports whether the validation value changed.
   *
   * @return whether the value changed
   */
  public boolean isChanged() {
    return changed;
  }
}

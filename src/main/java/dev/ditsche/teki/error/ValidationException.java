package dev.ditsche.teki.error;

import java.util.Collection;

/**
 * Represents validation error state reported by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ValidationException extends RuntimeException {

  /** Error collection carried by this exception. */
  private final ErrorBag errorBag;

  /**
   * Creates a ValidationException.
   *
   * @param errorBag error bag to merge or expose
   */
  public ValidationException(ErrorBag errorBag) {
    if (errorBag == null) throw new IllegalArgumentException("error bag cannot be null");
    this.errorBag = errorBag;
  }

  /**
   * Returns validation error details.
   *
   * @return validation errors
   */
  public Collection<ValidationError> getErrors() {
    return errorBag.getErrors().values();
  }
}

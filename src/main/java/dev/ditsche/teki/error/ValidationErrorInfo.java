package dev.ditsche.teki.error;

/**
 * Represents validation error state reported by Teki.
 *
 * @author Tobias Dittmann
 */
public final class ValidationErrorInfo {

  private final String message;
  private final String errorType;

  /**
   * Creates a ValidationErrorInfo.
   *
   * @param message validation error message
   * @param errorType validation error type
   */
  public ValidationErrorInfo(String message, String errorType) {
    this.message = message;
    this.errorType = errorType;
  }

  /**
   * Returns the validation error message.
   *
   * @return validation error message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns the validation error type.
   *
   * @return validation error type
   */
  public String getErrorType() {
    return errorType;
  }
}

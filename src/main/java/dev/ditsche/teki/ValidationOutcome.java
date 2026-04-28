package dev.ditsche.teki;

import dev.ditsche.teki.error.ErrorBag;
import dev.ditsche.teki.error.ValidationError;
import dev.ditsche.teki.error.ValidationException;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Result of a non-throwing validation call ({@link Teki#check}).
 *
 * <p>Carries either a valid (and possibly transformed) value or the errors that prevented
 * validation from passing. Use {@link #isValid()} to branch, or call {@link #orElseThrow()} to
 * convert back to the throwing path.
 *
 * @param <T> validated object type
 * @author Tobias Dittmann
 */
public final class ValidationOutcome<T> {

  private final T value;
  private final ErrorBag errorBag;

  private ValidationOutcome(T value, ErrorBag errorBag) {
    this.value = value;
    this.errorBag = errorBag;
  }

  static <T> ValidationOutcome<T> valid(T value) {
    return new ValidationOutcome<>(value, new ErrorBag());
  }

  static <T> ValidationOutcome<T> invalid(T value, ErrorBag errorBag) {
    return new ValidationOutcome<>(value, errorBag);
  }

  /**
   * Returns {@code true} when all validation rules passed.
   *
   * @return whether the outcome is valid
   */
  public boolean isValid() {
    return errorBag.isEmpty();
  }

  /**
   * Returns the validated (and possibly transformed) value.
   *
   * <p>The value is present regardless of validity — callers that need only the happy-path value
   * should check {@link #isValid()} or use {@link #orElseThrow()} instead.
   *
   * @return validated value
   */
  public T getValue() {
    return value;
  }

  /**
   * Returns the validation errors, or an empty collection when valid.
   *
   * @return validation errors
   */
  public Collection<ValidationError> getErrors() {
    return errorBag.getErrors().values();
  }

  /**
   * Invokes {@code action} with the valid value when validation passed.
   *
   * @param action consumer to invoke on success
   * @return this outcome for chaining
   */
  public ValidationOutcome<T> ifValid(Consumer<T> action) {
    if (isValid()) action.accept(value);
    return this;
  }

  /**
   * Invokes {@code action} with the errors when validation failed.
   *
   * @param action consumer to invoke on failure
   * @return this outcome for chaining
   */
  public ValidationOutcome<T> orElse(Consumer<Collection<ValidationError>> action) {
    if (!isValid()) action.accept(getErrors());
    return this;
  }

  /**
   * Returns the valid value or throws {@link ValidationException} when validation failed.
   *
   * <p>Bridges the result-based and exception-based paths — useful when calling code that returns a
   * {@link ValidationOutcome} but the current call-site prefers the throwing style.
   *
   * @return the validated value
   * @throws ValidationException when validation failed
   */
  public T orElseThrow() {
    if (!isValid()) throw new ValidationException(errorBag);
    return value;
  }
}

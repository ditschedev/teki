package dev.ditsche.teki.validation;

import dev.ditsche.teki.MessageResolver;

/**
 * Represents an internal validation component used by Teki.
 *
 * @author Tobias Dittmann
 */
public sealed interface Validatable permits ValidationArray, ValidationField, ValidationObject {

  /**
   * Returns the field name validated by this component.
   *
   * @return field name
   */
  String getField();

  /**
   * Validates a value and returns validation errors plus any transformed value.
   *
   * @param parent parent field path
   * @param object value to validate
   * @param abortEarly whether validation should abort after the first failure
   * @param resolver optional message resolver; {@code null} uses rule default messages
   * @return validation result
   */
  ValidationResult validate(String parent, Object object, boolean abortEarly, MessageResolver resolver);
}

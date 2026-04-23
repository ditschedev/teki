package dev.ditsche.teki;

import dev.ditsche.teki.validation.Validatable;
import java.util.List;
import java.util.function.Predicate;

final class ConditionalValidation {

  @SuppressWarnings("rawtypes")
  private final Predicate predicate;

  private final List<Validatable> validatables;

  ConditionalValidation(Predicate<?> predicate, List<Validatable> validatables) {
    this.predicate = predicate;
    this.validatables = validatables;
  }

  @SuppressWarnings("unchecked")
  boolean test(Object object) {
    try {
      return predicate.test(object);
    } catch (ClassCastException ignored) {
      return false;
    }
  }

  List<Validatable> validatables() {
    return validatables;
  }
}

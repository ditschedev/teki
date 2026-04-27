package dev.ditsche.teki;

import java.util.function.Predicate;

final class CrossFieldConstraint {

  @SuppressWarnings("rawtypes")
  private final Predicate predicate;

  private final String field;
  private final String type;
  private final String message;

  CrossFieldConstraint(Predicate<?> predicate, String field, String type, String message) {
    this.predicate = predicate;
    this.field = field;
    this.type = type;
    this.message = message;
  }

  @SuppressWarnings("unchecked")
  boolean test(Object object) {
    try {
      return predicate.test(object);
    } catch (ClassCastException ignored) {
      return true;
    }
  }

  String field() {
    return field;
  }

  String type() {
    return type;
  }

  String message() {
    return message;
  }
}

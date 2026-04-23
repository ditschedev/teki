package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.annotation.Default;
import dev.ditsche.teki.annotation.Email;
import dev.ditsche.teki.annotation.Min;
import dev.ditsche.teki.annotation.Required;
import dev.ditsche.teki.annotation.Trim;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import org.junit.jupiter.api.Test;

class TekiFluentTest {

  static class Person {
    String name;
    int age;

    Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    String getName() {
      return name;
    }

    int getAge() {
      return age;
    }
  }

  record RecordPerson(String name, int age) {}

  record AnnotatedRecordPerson(@Required @Email String email, @Min(18) int age) {}

  record AnnotatedTransformRecord(@Trim String name, @Default("user") String role) {}

  @Test
  void validObjectPassesFluentValidation() {
    Teki teki =
        Teki.fromRules(Rules.string("name").required(), Rules.number("age").required().min(0));
    assertThat(teki.validate(new Person("Alice", 30))).isNotNull();
  }

  @Test
  void missingRequiredStringFails() {
    Teki teki = Teki.fromRules(Rules.string("name").required());
    assertThatThrownBy(() -> teki.validate(new Person(null, 0)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void abortEarlyStopsAtFirstError() {
    Teki teki =
        Teki.fromRules(Rules.string("name").required(), Rules.number("age").required().min(18));
    Person p = new Person(null, 10);
    assertThatThrownBy(() -> teki.validate(p, true))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> {
              ValidationException ve = (ValidationException) ex;
              assertThat(ve.getErrors()).hasSize(1);
            });
  }

  @Test
  void trimRuleTransformsFieldValue() {
    Teki teki = Teki.fromRules(Rules.string("name").required().trim());
    Person p = new Person("  Alice  ", 30);
    teki.validate(p);
    assertThat(p.name).isEqualTo("Alice");
  }

  @Test
  void emailRuleRejectsInvalidAddress() {
    Teki teki = Teki.fromRules(Rules.string("name").required().email());
    assertThatThrownBy(() -> teki.validate(new Person("not-an-email", 0)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void multipleErrorsCollectedWithoutAbortEarly() {
    Teki teki =
        Teki.fromRules(Rules.string("name").required(), Rules.number("age").required().min(18));
    Person p = new Person(null, 10);
    assertThatThrownBy(() -> teki.validate(p, false))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> {
              ValidationException ve = (ValidationException) ex;
              assertThat(ve.getErrors()).hasSize(2);
            });
  }

  @Test
  void methodReferenceGettersSelectFields() {
    Teki teki =
        Teki.fromRules(
            Rules.string(Person::getName).required(),
            Rules.number(Person::getAge).required().min(18));

    assertThat(teki.validate(new Person("Alice", 30))).isNotNull();
    assertThatThrownBy(() -> teki.validate(new Person(null, 10)))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> {
              ValidationException ve = (ValidationException) ex;
              assertThat(ve.getErrors()).hasSize(2);
            });
  }

  @Test
  void methodReferenceRecordAccessorsSelectFields() {
    Teki teki =
        Teki.fromRules(
            Rules.string(RecordPerson::name).required(),
            Rules.number(RecordPerson::age).required().min(18));

    assertThat(teki.validate(new RecordPerson("Alice", 30))).isNotNull();
    assertThatThrownBy(() -> teki.validate(new RecordPerson(null, 10)))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> {
              ValidationException ve = (ValidationException) ex;
              assertThat(ve.getErrors()).hasSize(2);
            });
  }

  @Test
  void transformationsReturnNewRecordInstance() {
    Teki teki = Teki.fromRules(Rules.string(RecordPerson::name).required().trim());

    RecordPerson input = new RecordPerson("  Alice  ", 30);
    RecordPerson result = teki.validate(input);

    assertThat(result).isNotSameAs(input);
    assertThat(result.name()).isEqualTo("Alice");
    assertThat(result.age()).isEqualTo(30);
  }

  @Test
  void annotationsCanValidateRecords() {
    Teki teki = Teki.from(AnnotatedRecordPerson.class);

    assertThat(teki.validate(new AnnotatedRecordPerson("alice@example.dev", 30))).isNotNull();
    assertThatThrownBy(() -> teki.validate(new AnnotatedRecordPerson("not-an-email", 16)))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> {
              ValidationException ve = (ValidationException) ex;
              assertThat(ve.getErrors()).hasSize(2);
            });
  }

  @Test
  void annotationTransformationsReturnNewRecordInstance() {
    Teki teki = Teki.from(AnnotatedTransformRecord.class);

    AnnotatedTransformRecord input = new AnnotatedTransformRecord("  Alice  ", null);
    AnnotatedTransformRecord result = teki.validate(input);

    assertThat(result).isNotSameAs(input);
    assertThat(result.name()).isEqualTo("Alice");
    assertThat(result.role()).isEqualTo("user");
  }
}

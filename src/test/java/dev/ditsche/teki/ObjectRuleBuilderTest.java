package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.error.ValidationError;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import java.util.Collection;
import org.junit.jupiter.api.Test;

class ObjectRuleBuilderTest {

  static class Address {
    String street;
    String city;

    Address(String street, String city) {
      this.street = street;
      this.city = city;
    }
  }

  static class Person {
    String name;
    Address address;

    Person(String name, Address address) {
      this.name = name;
      this.address = address;
    }
  }

  @Test
  void validNestedObjectPasses() {
    Teki teki =
        Teki.fromRules(
            Rules.string("name").required(),
            Rules.object("address")
                .fields(Rules.string("street").required(), Rules.string("city").required()));
    Person person = new Person("Alice", new Address("Main St", "Berlin"));
    assertThat(teki.validate(person)).isSameAs(person);
  }

  @Test
  void missingRequiredNestedFieldFails() {
    Teki teki =
        Teki.fromRules(
            Rules.object("address")
                .fields(Rules.string("street").required(), Rules.string("city").required()));
    Person person = new Person("Alice", new Address(null, "Berlin"));
    assertThatThrownBy(() -> teki.validate(person)).isInstanceOf(ValidationException.class);
  }

  @Test
  void nestedErrorPathUsesDotNotation() {
    Teki teki = Teki.fromRules(Rules.object("address").fields(Rules.string("street").required()));
    Person person = new Person("Alice", new Address(null, null));
    ValidationOutcome<Person> outcome = teki.check(person);
    assertThat(outcome.isValid()).isFalse();
    Collection<ValidationError> errors = outcome.getErrors();
    assertThat(errors).extracting(ValidationError::getField).containsExactly("address.street");
  }

  @Test
  void optionalNestedObjectSkipsNullValue() {
    Teki teki =
        Teki.fromRules(
            Rules.object("address").optional().fields(Rules.string("street").required()));
    Person person = new Person("Alice", null);
    assertThat(teki.validate(person)).isSameAs(person);
  }

  @Test
  void childMethodAddsSingleChild() {
    Teki teki = Teki.fromRules(Rules.object("address").child(Rules.string("city").required()));
    Person person = new Person("Alice", new Address(null, null));
    assertThatThrownBy(() -> teki.validate(person)).isInstanceOf(ValidationException.class);
  }
}

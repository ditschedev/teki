package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.annotation.*;
import dev.ditsche.teki.error.ValidationException;
import org.junit.jupiter.api.Test;

class TekiAnnotationTest {

  static class UserForm {
    @Required @Email String email;

    @Trim String name;

    @Min(18)
    Integer age;

    @Default("guest")
    String role;

    UserForm(String email, String name, Integer age, String role) {
      this.email = email;
      this.name = name;
      this.age = age;
      this.role = role;
    }
  }

  static class OptionalForm {
    @Required String required;

    String optional;

    OptionalForm(String required, String optional) {
      this.required = required;
      this.optional = optional;
    }
  }

  @Test
  void validObjectPassesAnnotationValidation() {
    Teki teki = Teki.from(UserForm.class);
    UserForm form = new UserForm("user@example.com", "Alice", 25, "admin");
    assertThat(teki.validate(form)).isSameAs(form);
  }

  @Test
  void missingRequiredFieldFails() {
    Teki teki = Teki.from(UserForm.class);
    UserForm form = new UserForm(null, "Alice", 25, "admin");
    assertThatThrownBy(() -> teki.validate(form)).isInstanceOf(ValidationException.class);
  }

  @Test
  void invalidEmailFails() {
    Teki teki = Teki.from(UserForm.class);
    UserForm form = new UserForm("not-an-email", "Alice", 25, "admin");
    assertThatThrownBy(() -> teki.validate(form)).isInstanceOf(ValidationException.class);
  }

  @Test
  void trimAnnotationTransformsValue() {
    Teki teki = Teki.from(UserForm.class);
    UserForm form = new UserForm("user@example.com", "  Alice  ", 25, "admin");
    teki.validate(form);
    assertThat(form.name).isEqualTo("Alice");
  }

  @Test
  void defaultAnnotationFillsNullValue() {
    Teki teki = Teki.from(UserForm.class);
    UserForm form = new UserForm("user@example.com", "Alice", 25, null);
    teki.validate(form);
    assertThat(form.role).isEqualTo("guest");
  }

  @Test
  void minAnnotationRejectsValueBelowThreshold() {
    Teki teki = Teki.from(UserForm.class);
    UserForm form = new UserForm("user@example.com", "Alice", 16, "admin");
    assertThatThrownBy(() -> teki.validate(form)).isInstanceOf(ValidationException.class);
  }

  @Test
  void optionalFieldNullDoesNotFail() {
    Teki teki = Teki.from(OptionalForm.class);
    OptionalForm form = new OptionalForm("hello", null);
    assertThat(teki.validate(form)).isSameAs(form);
  }

  @Test
  void cachedInstanceProducesConsistentResults() {
    Teki first = Teki.from(UserForm.class);
    Teki second = Teki.from(UserForm.class);
    assertThat(first).isSameAs(second);
    // Validate twice on same cached instance — error state must not bleed across calls
    UserForm valid = new UserForm("user@example.com", "Alice", 25, "admin");
    UserForm invalid = new UserForm(null, "Alice", 25, "admin");
    assertThatThrownBy(() -> first.validate(invalid)).isInstanceOf(ValidationException.class);
    assertThat(first.validate(valid)).isSameAs(valid);
  }
}

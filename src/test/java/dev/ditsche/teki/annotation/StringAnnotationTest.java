package dev.ditsche.teki.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationException;
import org.junit.jupiter.api.Test;

class StringAnnotationTest {

  static class UuidForm {
    @Required @Uuid String id;

    UuidForm(String id) {
      this.id = id;
    }
  }

  static class NotBlankForm {
    @NotBlank String name;

    NotBlankForm(String name) {
      this.name = name;
    }
  }

  static class OneOfForm {
    @Required
    @OneOf({"admin", "editor", "viewer"})
    String role;

    OneOfForm(String role) {
      this.role = role;
    }
  }

  // -------------------------------------------------------------------------
  // @Uuid
  // -------------------------------------------------------------------------

  @Test
  void uuidAcceptsWellFormedValue() {
    assertThat(
            Teki.from(UuidForm.class)
                .check(new UuidForm("550e8400-e29b-41d4-a716-446655440000"))
                .isValid())
        .isTrue();
  }

  @Test
  void uuidRejectsMalformedValue() {
    assertThatThrownBy(
            () -> Teki.from(UuidForm.class).validate(new UuidForm("not-a-uuid")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void uuidRejectsNull() {
    assertThatThrownBy(() -> Teki.from(UuidForm.class).validate(new UuidForm(null)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @NotBlank
  // -------------------------------------------------------------------------

  @Test
  void notBlankAcceptsNonEmptyString() {
    assertThat(Teki.from(NotBlankForm.class).check(new NotBlankForm("Alice")).isValid()).isTrue();
  }

  @Test
  void notBlankRejectsWhitespaceOnlyString() {
    assertThatThrownBy(() -> Teki.from(NotBlankForm.class).validate(new NotBlankForm("   ")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void notBlankRejectsEmptyString() {
    assertThatThrownBy(() -> Teki.from(NotBlankForm.class).validate(new NotBlankForm("")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void notBlankRejectsNull() {
    assertThatThrownBy(() -> Teki.from(NotBlankForm.class).validate(new NotBlankForm(null)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @OneOf
  // -------------------------------------------------------------------------

  @Test
  void oneOfAcceptsAllowedValue() {
    assertThat(Teki.from(OneOfForm.class).check(new OneOfForm("editor")).isValid()).isTrue();
  }

  @Test
  void oneOfRejectsDisallowedValue() {
    assertThatThrownBy(() -> Teki.from(OneOfForm.class).validate(new OneOfForm("superuser")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void oneOfIsCaseSensitive() {
    assertThatThrownBy(() -> Teki.from(OneOfForm.class).validate(new OneOfForm("Admin")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void oneOfRejectsNull() {
    assertThatThrownBy(() -> Teki.from(OneOfForm.class).validate(new OneOfForm(null)))
        .isInstanceOf(ValidationException.class);
  }
}

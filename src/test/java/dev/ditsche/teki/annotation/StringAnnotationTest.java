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
    assertThatThrownBy(() -> Teki.from(UuidForm.class).validate(new UuidForm("not-a-uuid")))
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

  // -------------------------------------------------------------------------
  // @MacAddress
  // -------------------------------------------------------------------------

  static class MacForm {
    @Required @MacAddress String mac;

    MacForm(String mac) {
      this.mac = mac;
    }
  }

  @Test
  void macAnnotationAcceptsColonForm() {
    assertThat(Teki.from(MacForm.class).check(new MacForm("AA:BB:CC:DD:EE:FF")).isValid()).isTrue();
  }

  @Test
  void macAnnotationAcceptsHyphenForm() {
    assertThat(Teki.from(MacForm.class).check(new MacForm("AA-BB-CC-DD-EE-FF")).isValid()).isTrue();
  }

  @Test
  void macAnnotationRejectsMixedSeparators() {
    assertThatThrownBy(() -> Teki.from(MacForm.class).validate(new MacForm("AA:BB-CC:DD:EE:FF")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void macAnnotationRejectsNull() {
    assertThatThrownBy(() -> Teki.from(MacForm.class).validate(new MacForm(null)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @Phone
  // -------------------------------------------------------------------------

  static class PhoneForm {
    @Required @Phone String phone;

    PhoneForm(String phone) {
      this.phone = phone;
    }
  }

  @Test
  void phoneAnnotationAcceptsE164() {
    assertThat(Teki.from(PhoneForm.class).check(new PhoneForm("+491511234567")).isValid()).isTrue();
  }

  @Test
  void phoneAnnotationRejectsLocalFormat() {
    assertThatThrownBy(() -> Teki.from(PhoneForm.class).validate(new PhoneForm("01511234567")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void phoneAnnotationRejectsNull() {
    assertThatThrownBy(() -> Teki.from(PhoneForm.class).validate(new PhoneForm(null)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @Slug
  // -------------------------------------------------------------------------

  static class SlugForm {
    @Required @Slug String slug;

    SlugForm(String slug) {
      this.slug = slug;
    }
  }

  @Test
  void slugAnnotationAcceptsValidSlug() {
    assertThat(Teki.from(SlugForm.class).check(new SlugForm("my-post")).isValid()).isTrue();
  }

  @Test
  void slugAnnotationRejectsUppercase() {
    assertThatThrownBy(() -> Teki.from(SlugForm.class).validate(new SlugForm("My-Post")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void slugAnnotationRejectsNull() {
    assertThatThrownBy(() -> Teki.from(SlugForm.class).validate(new SlugForm(null)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @Base64
  // -------------------------------------------------------------------------

  static class Base64Form {
    @Base64 String data;

    Base64Form(String data) {
      this.data = data;
    }
  }

  static class Base64UrlSafeForm {
    @Base64(urlSafe = true)
    String data;

    Base64UrlSafeForm(String data) {
      this.data = data;
    }
  }

  @Test
  void base64AnnotationAcceptsValidStandard() {
    assertThat(Teki.from(Base64Form.class).check(new Base64Form("SGVsbG8gV29ybGQ=")).isValid())
        .isTrue();
  }

  @Test
  void base64AnnotationRejectsInvalidPadding() {
    assertThatThrownBy(() -> Teki.from(Base64Form.class).validate(new Base64Form("SGVsbG8====")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void base64UrlSafeAnnotationAcceptsValid() {
    assertThat(
            Teki.from(Base64UrlSafeForm.class)
                .check(new Base64UrlSafeForm("SGVsbG8gV29ybGQ"))
                .isValid())
        .isTrue();
  }

  @Test
  void base64UrlSafeAnnotationRejectsStandardAlphabet() {
    assertThatThrownBy(
            () -> Teki.from(Base64UrlSafeForm.class).validate(new Base64UrlSafeForm("SGVs+G8/")))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @SemVer
  // -------------------------------------------------------------------------

  static class SemVerForm {
    @Required @SemVer String version;

    SemVerForm(String version) {
      this.version = version;
    }
  }

  @Test
  void semverAnnotationAcceptsValidVersion() {
    assertThat(Teki.from(SemVerForm.class).check(new SemVerForm("1.2.3")).isValid()).isTrue();
  }

  @Test
  void semverAnnotationRejectsVPrefix() {
    assertThatThrownBy(() -> Teki.from(SemVerForm.class).validate(new SemVerForm("v1.0.0")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void semverAnnotationRejectsNull() {
    assertThatThrownBy(() -> Teki.from(SemVerForm.class).validate(new SemVerForm(null)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @Iban
  // -------------------------------------------------------------------------

  static class IbanForm {
    @Required @Iban String iban;

    IbanForm(String iban) {
      this.iban = iban;
    }
  }

  @Test
  void ibanAnnotationAcceptsValidIban() {
    assertThat(Teki.from(IbanForm.class).check(new IbanForm("DE89370400440532013000")).isValid())
        .isTrue();
  }

  @Test
  void ibanAnnotationAcceptsIbanWithSpaces() {
    assertThat(
            Teki.from(IbanForm.class).check(new IbanForm("DE89 3704 0044 0532 0130 00")).isValid())
        .isTrue();
  }

  @Test
  void ibanAnnotationRejectsInvalidCheckDigits() {
    assertThatThrownBy(
            () -> Teki.from(IbanForm.class).validate(new IbanForm("DE00370400440532013000")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void ibanAnnotationRejectsNull() {
    assertThatThrownBy(() -> Teki.from(IbanForm.class).validate(new IbanForm(null)))
        .isInstanceOf(ValidationException.class);
  }
}

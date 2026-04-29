package dev.ditsche.teki.rule.builder;

import static dev.ditsche.teki.rule.builder.Rules.string;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationException;
import org.junit.jupiter.api.Test;

class StringRuleBuilderTest {

  static class Form {
    String value;

    Form(String value) {
      this.value = value;
    }
  }

  // -------------------------------------------------------------------------
  // alphanum
  // -------------------------------------------------------------------------

  @Test
  void alphanumAcceptsLettersAndDigits() {
    Teki teki = Teki.fromRules(string("value").alphanum());
    assertThat(teki.validate(new Form("abc123"))).isNotNull();
  }

  @Test
  void alphanumRejectsHyphen() {
    Teki teki = Teki.fromRules(string("value").alphanum());
    assertThatThrownBy(() -> teki.validate(new Form("abc-123")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void alphanumSkipsNullWhenOptional() {
    Teki teki = Teki.fromRules(string("value").optional().alphanum());
    assertThat(teki.validate(new Form(null))).isNotNull();
  }

  // -------------------------------------------------------------------------
  // ip / ipv4 / ipv6
  // -------------------------------------------------------------------------

  @Test
  void ipAcceptsIpv4() {
    Teki teki = Teki.fromRules(string("value").ip());
    assertThat(teki.validate(new Form("192.168.1.1"))).isNotNull();
  }

  @Test
  void ipAcceptsIpv6() {
    Teki teki = Teki.fromRules(string("value").ip());
    assertThat(teki.validate(new Form("2001:db8::1"))).isNotNull();
  }

  @Test
  void ipRejectsInvalid() {
    Teki teki = Teki.fromRules(string("value").ip());
    assertThatThrownBy(() -> teki.validate(new Form("not-an-ip")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void ipv4AcceptsIpv4() {
    Teki teki = Teki.fromRules(string("value").ipv4());
    assertThat(teki.validate(new Form("10.0.0.1"))).isNotNull();
  }

  @Test
  void ipv4RejectsIpv6() {
    Teki teki = Teki.fromRules(string("value").ipv4());
    assertThatThrownBy(() -> teki.validate(new Form("2001:db8::1")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void ipv6AcceptsIpv6() {
    Teki teki = Teki.fromRules(string("value").ipv6());
    assertThat(teki.validate(new Form("::1"))).isNotNull();
  }

  @Test
  void ipv6RejectsIpv4() {
    Teki teki = Teki.fromRules(string("value").ipv6());
    assertThatThrownBy(() -> teki.validate(new Form("192.168.1.1")))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // creditCard
  // -------------------------------------------------------------------------

  @Test
  void creditCardAcceptsValidNumber() {
    Teki teki = Teki.fromRules(string("value").creditCard());
    assertThat(teki.validate(new Form("4111111111111111"))).isNotNull();
  }

  @Test
  void creditCardRejectsInvalidNumber() {
    Teki teki = Teki.fromRules(string("value").creditCard());
    assertThatThrownBy(() -> teki.validate(new Form("4111111111111")))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // slugify
  // -------------------------------------------------------------------------

  @Test
  void slugifyTransformsField() {
    Teki teki = Teki.fromRules(string("value").slugify());
    Form form = new Form("Hello World");
    teki.validate(form);
    assertThat(form.value).isEqualTo("hello-world");
  }

  @Test
  void slugifyStripsSpecialChars() {
    Teki teki = Teki.fromRules(string("value").slugify());
    Form form = new Form("product (v2.0)!");
    teki.validate(form);
    assertThat(form.value).isEqualTo("product-v2-0");
  }

  @Test
  void slugifyRejectsUnslugifiableInput() {
    Teki teki = Teki.fromRules(string("value").slugify());
    assertThatThrownBy(() -> teki.validate(new Form("!!!")))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // notBlank
  // -------------------------------------------------------------------------

  @Test
  void notBlankAcceptsNonEmptyString() {
    Teki teki = Teki.fromRules(string("value").notBlank());
    assertThat(teki.validate(new Form("hello"))).isNotNull();
  }

  @Test
  void notBlankRejectsWhitespaceOnly() {
    Teki teki = Teki.fromRules(string("value").notBlank());
    assertThatThrownBy(() -> teki.validate(new Form("   ")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void notBlankRejectsNull() {
    Teki teki = Teki.fromRules(string("value").notBlank());
    assertThatThrownBy(() -> teki.validate(new Form(null))).isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // oneOf
  // -------------------------------------------------------------------------

  @Test
  void oneOfAcceptsAllowedValue() {
    Teki teki = Teki.fromRules(string("value").oneOf("admin", "editor", "viewer"));
    assertThat(teki.validate(new Form("editor"))).isNotNull();
  }

  @Test
  void oneOfRejectsDisallowedValue() {
    Teki teki = Teki.fromRules(string("value").oneOf("admin", "editor", "viewer"));
    assertThatThrownBy(() -> teki.validate(new Form("superuser")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void oneOfIsCaseSensitive() {
    Teki teki = Teki.fromRules(string("value").oneOf("admin", "editor"));
    assertThatThrownBy(() -> teki.validate(new Form("Admin")))
        .isInstanceOf(ValidationException.class);
  }
}

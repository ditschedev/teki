package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class ValidationOutcomeTest {

  static class Form {
    String email;
    String name;

    Form(String email, String name) {
      this.email = email;
      this.name = name;
    }
  }

  static Teki schema() {
    return Teki.fromRules(
        Rules.string("email").required().email(), Rules.string("name").required());
  }

  @Test
  void checkReturnsValidOutcomeIfValid() {
    ValidationOutcome<Form> outcome = schema().check(new Form("a@b.com", "Alice"));
    assertThat(outcome.isValid()).isTrue();
    assertThat(outcome.getValue().email).isEqualTo("a@b.com");
  }

  @Test
  void checkReturnsInvalidOutcomeOrElse() {
    ValidationOutcome<Form> outcome = schema().check(new Form(null, null));
    assertThat(outcome.isValid()).isFalse();
    assertThat(outcome.getErrors()).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  void ifValidCalledWhenValid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form("a@b.com", "Alice")).ifValid(f -> called.set(true));
    assertThat(called).isTrue();
  }

  @Test
  void ifValidNotCalledWhenInvalid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form(null, null)).ifValid(f -> called.set(true));
    assertThat(called).isFalse();
  }

  @Test
  void orElseCalledWhenInvalid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form(null, null)).orElse(errors -> called.set(true));
    assertThat(called).isTrue();
  }

  @Test
  void orElseNotCalledWhenValid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form("a@b.com", "Alice")).orElse(errors -> called.set(true));
    assertThat(called).isFalse();
  }

  @Test
  void onSuccessAndOrElseChain() {
    AtomicBoolean success = new AtomicBoolean();
    AtomicBoolean failure = new AtomicBoolean();
    schema()
        .check(new Form(null, null))
        .ifValid(f -> success.set(true))
        .orElse(e -> failure.set(true));
    assertThat(success).isFalse();
    assertThat(failure).isTrue();
  }

  @Test
  void orElseThrowReturnsValueWhenValid() {
    Form form = new Form("a@b.com", "Alice");
    assertThat(schema().check(form).orElseThrow()).isSameAs(form);
  }

  @Test
  void orElseThrowThrowsWhenInvalid() {
    assertThatThrownBy(() -> schema().check(new Form(null, null)).orElseThrow())
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void checkNeverThrowsEvenWithAbortEarly() {
    // When abortEarly=true, field-level validators throw internally — check() must absorb that.
    ValidationOutcome<Form> outcome = schema().check(new Form(null, null), true);
    assertThat(outcome.isValid()).isFalse();
    assertThat(outcome.getErrors()).hasSize(1);
  }

  @Test
  void validateStillThrowsOrElse() {
    assertThatThrownBy(() -> schema().validate(new Form(null, null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void checkCarriesTransformedValue() {
    Teki teki = Teki.fromRules(Rules.string("name").required().trim());
    Form form = new Form("a@b.com", "  Alice  ");
    ValidationOutcome<Form> outcome = teki.check(form);
    assertThat(outcome.isValid()).isTrue();
    assertThat(outcome.getValue().name).isEqualTo("Alice");
  }
}

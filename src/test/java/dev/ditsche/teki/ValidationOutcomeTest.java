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
    return Teki.fromRules(Rules.string("email").required().email(), Rules.string("name").required());
  }

  @Test
  void checkReturnsValidOutcomeOnSuccess() {
    ValidationOutcome<Form> outcome = schema().check(new Form("a@b.com", "Alice"));
    assertThat(outcome.isValid()).isTrue();
    assertThat(outcome.getValue().email).isEqualTo("a@b.com");
  }

  @Test
  void checkReturnsInvalidOutcomeOnFailure() {
    ValidationOutcome<Form> outcome = schema().check(new Form(null, null));
    assertThat(outcome.isValid()).isFalse();
    assertThat(outcome.getErrors()).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  void onSuccessCalledWhenValid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form("a@b.com", "Alice")).onSuccess(f -> called.set(true));
    assertThat(called).isTrue();
  }

  @Test
  void onSuccessNotCalledWhenInvalid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form(null, null)).onSuccess(f -> called.set(true));
    assertThat(called).isFalse();
  }

  @Test
  void onFailureCalledWhenInvalid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form(null, null)).onFailure(errors -> called.set(true));
    assertThat(called).isTrue();
  }

  @Test
  void onFailureNotCalledWhenValid() {
    AtomicBoolean called = new AtomicBoolean();
    schema().check(new Form("a@b.com", "Alice")).onFailure(errors -> called.set(true));
    assertThat(called).isFalse();
  }

  @Test
  void onSuccessAndOnFailureChain() {
    AtomicBoolean success = new AtomicBoolean();
    AtomicBoolean failure = new AtomicBoolean();
    schema()
        .check(new Form(null, null))
        .onSuccess(f -> success.set(true))
        .onFailure(e -> failure.set(true));
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
  void validateStillThrowsOnFailure() {
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

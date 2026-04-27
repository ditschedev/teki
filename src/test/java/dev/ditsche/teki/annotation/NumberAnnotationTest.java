package dev.ditsche.teki.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationException;
import org.junit.jupiter.api.Test;

class NumberAnnotationTest {

  static class PositiveForm {
    @Positive int quantity;

    PositiveForm(int quantity) {
      this.quantity = quantity;
    }
  }

  static class PositiveOrZeroForm {
    @PositiveOrZero int score;

    PositiveOrZeroForm(int score) {
      this.score = score;
    }
  }

  static class NegativeForm {
    @Negative int delta;

    NegativeForm(int delta) {
      this.delta = delta;
    }
  }

  static class NegativeOrZeroForm {
    @NegativeOrZero int balance;

    NegativeOrZeroForm(int balance) {
      this.balance = balance;
    }
  }

  // -------------------------------------------------------------------------
  // @Positive
  // -------------------------------------------------------------------------

  @Test
  void positiveAcceptsValueAboveZero() {
    assertThat(Teki.from(PositiveForm.class).check(new PositiveForm(1)).isValid()).isTrue();
  }

  @Test
  void positiveRejectsZero() {
    assertThatThrownBy(() -> Teki.from(PositiveForm.class).validate(new PositiveForm(0)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void positiveRejectsNegativeValue() {
    assertThatThrownBy(() -> Teki.from(PositiveForm.class).validate(new PositiveForm(-5)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @PositiveOrZero
  // -------------------------------------------------------------------------

  @Test
  void positiveOrZeroAcceptsZero() {
    assertThat(Teki.from(PositiveOrZeroForm.class).check(new PositiveOrZeroForm(0)).isValid())
        .isTrue();
  }

  @Test
  void positiveOrZeroAcceptsPositiveValue() {
    assertThat(Teki.from(PositiveOrZeroForm.class).check(new PositiveOrZeroForm(10)).isValid())
        .isTrue();
  }

  @Test
  void positiveOrZeroRejectsNegativeValue() {
    assertThatThrownBy(
            () -> Teki.from(PositiveOrZeroForm.class).validate(new PositiveOrZeroForm(-1)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @Negative
  // -------------------------------------------------------------------------

  @Test
  void negativeAcceptsValueBelowZero() {
    assertThat(Teki.from(NegativeForm.class).check(new NegativeForm(-1)).isValid()).isTrue();
  }

  @Test
  void negativeRejectsZero() {
    assertThatThrownBy(() -> Teki.from(NegativeForm.class).validate(new NegativeForm(0)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void negativeRejectsPositiveValue() {
    assertThatThrownBy(() -> Teki.from(NegativeForm.class).validate(new NegativeForm(5)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @NegativeOrZero
  // -------------------------------------------------------------------------

  @Test
  void negativeOrZeroAcceptsZero() {
    assertThat(Teki.from(NegativeOrZeroForm.class).check(new NegativeOrZeroForm(0)).isValid())
        .isTrue();
  }

  @Test
  void negativeOrZeroAcceptsNegativeValue() {
    assertThat(Teki.from(NegativeOrZeroForm.class).check(new NegativeOrZeroForm(-10)).isValid())
        .isTrue();
  }

  @Test
  void negativeOrZeroRejectsPositiveValue() {
    assertThatThrownBy(
            () -> Teki.from(NegativeOrZeroForm.class).validate(new NegativeOrZeroForm(1)))
        .isInstanceOf(ValidationException.class);
  }
}

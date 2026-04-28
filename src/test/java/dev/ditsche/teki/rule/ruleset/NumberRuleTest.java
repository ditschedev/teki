package dev.ditsche.teki.rule.ruleset;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class NumberRuleTest {

  // -------------------------------------------------------------------------
  // MinRule
  // -------------------------------------------------------------------------

  @Test
  void minAcceptsValueAtBoundary() {
    assertThat(new MinRule(5).test(5).passed()).isTrue();
  }

  @Test
  void minAcceptsValueAboveBoundary() {
    assertThat(new MinRule(5).test(10).passed()).isTrue();
  }

  @Test
  void minRejectsValueBelowBoundary() {
    assertThat(new MinRule(5).test(4).passed()).isFalse();
  }

  @Test
  void minAcceptsBigDecimal() {
    assertThat(new MinRule(5).test(BigDecimal.valueOf(5.5)).passed()).isTrue();
  }

  @Test
  void minRejectsNull() {
    assertThat(new MinRule(0).test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // MaxRule
  // -------------------------------------------------------------------------

  @Test
  void maxAcceptsValueAtBoundary() {
    assertThat(new MaxRule(10).test(10).passed()).isTrue();
  }

  @Test
  void maxAcceptsValueBelowBoundary() {
    assertThat(new MaxRule(10).test(5).passed()).isTrue();
  }

  @Test
  void maxRejectsValueAboveBoundary() {
    assertThat(new MaxRule(10).test(11).passed()).isFalse();
  }

  @Test
  void maxRejectsNull() {
    assertThat(new MaxRule(10).test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PositiveRule
  // -------------------------------------------------------------------------

  @Test
  void positiveAcceptsValueAboveZero() {
    assertThat(new PositiveRule().test(1).passed()).isTrue();
  }

  @Test
  void positiveRejectsZero() {
    assertThat(new PositiveRule().test(0).passed()).isFalse();
  }

  @Test
  void positiveRejectsNegativeValue() {
    assertThat(new PositiveRule().test(-1).passed()).isFalse();
  }

  @Test
  void positiveAcceptsPositiveBigDecimal() {
    assertThat(new PositiveRule().test(BigDecimal.valueOf(0.01)).passed()).isTrue();
  }

  @Test
  void positiveRejectsZeroBigDecimal() {
    assertThat(new PositiveRule().test(BigDecimal.ZERO).passed()).isFalse();
  }

  @Test
  void positiveRejectsNull() {
    assertThat(new PositiveRule().test(null).passed()).isFalse();
  }

  @Test
  void positiveRejectsNonNumber() {
    assertThat(new PositiveRule().test("5").passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PositiveOrZeroRule
  // -------------------------------------------------------------------------

  @Test
  void positiveOrZeroAcceptsZero() {
    assertThat(new PositiveOrZeroRule().test(0).passed()).isTrue();
  }

  @Test
  void positiveOrZeroAcceptsPositiveValue() {
    assertThat(new PositiveOrZeroRule().test(1).passed()).isTrue();
  }

  @Test
  void positiveOrZeroRejectsNegativeValue() {
    assertThat(new PositiveOrZeroRule().test(-1).passed()).isFalse();
  }

  @Test
  void positiveOrZeroAcceptsZeroBigDecimal() {
    assertThat(new PositiveOrZeroRule().test(BigDecimal.ZERO).passed()).isTrue();
  }

  @Test
  void positiveOrZeroRejectsNull() {
    assertThat(new PositiveOrZeroRule().test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // NegativeRule
  // -------------------------------------------------------------------------

  @Test
  void negativeAcceptsValueBelowZero() {
    assertThat(new NegativeRule().test(-1).passed()).isTrue();
  }

  @Test
  void negativeRejectsZero() {
    assertThat(new NegativeRule().test(0).passed()).isFalse();
  }

  @Test
  void negativeRejectsPositiveValue() {
    assertThat(new NegativeRule().test(1).passed()).isFalse();
  }

  @Test
  void negativeAcceptsNegativeBigDecimal() {
    assertThat(new NegativeRule().test(BigDecimal.valueOf(-0.01)).passed()).isTrue();
  }

  @Test
  void negativeRejectsNull() {
    assertThat(new NegativeRule().test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // NegativeOrZeroRule
  // -------------------------------------------------------------------------

  @Test
  void negativeOrZeroAcceptsZero() {
    assertThat(new NegativeOrZeroRule().test(0).passed()).isTrue();
  }

  @Test
  void negativeOrZeroAcceptsNegativeValue() {
    assertThat(new NegativeOrZeroRule().test(-1).passed()).isTrue();
  }

  @Test
  void negativeOrZeroRejectsPositiveValue() {
    assertThat(new NegativeOrZeroRule().test(1).passed()).isFalse();
  }

  @Test
  void negativeOrZeroAcceptsZeroBigDecimal() {
    assertThat(new NegativeOrZeroRule().test(BigDecimal.ZERO).passed()).isTrue();
  }

  @Test
  void negativeOrZeroRejectsNull() {
    assertThat(new NegativeOrZeroRule().test(null).passed()).isFalse();
  }
}

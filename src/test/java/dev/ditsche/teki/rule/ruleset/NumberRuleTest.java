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
    assertThat(new MinRule(5).test(5).isPassed()).isTrue();
  }

  @Test
  void minAcceptsValueAboveBoundary() {
    assertThat(new MinRule(5).test(10).isPassed()).isTrue();
  }

  @Test
  void minRejectsValueBelowBoundary() {
    assertThat(new MinRule(5).test(4).isPassed()).isFalse();
  }

  @Test
  void minAcceptsBigDecimal() {
    assertThat(new MinRule(5).test(BigDecimal.valueOf(5.5)).isPassed()).isTrue();
  }

  @Test
  void minRejectsNull() {
    assertThat(new MinRule(0).test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // MaxRule
  // -------------------------------------------------------------------------

  @Test
  void maxAcceptsValueAtBoundary() {
    assertThat(new MaxRule(10).test(10).isPassed()).isTrue();
  }

  @Test
  void maxAcceptsValueBelowBoundary() {
    assertThat(new MaxRule(10).test(5).isPassed()).isTrue();
  }

  @Test
  void maxRejectsValueAboveBoundary() {
    assertThat(new MaxRule(10).test(11).isPassed()).isFalse();
  }

  @Test
  void maxRejectsNull() {
    assertThat(new MaxRule(10).test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PositiveRule
  // -------------------------------------------------------------------------

  @Test
  void positiveAcceptsValueAboveZero() {
    assertThat(new PositiveRule().test(1).isPassed()).isTrue();
  }

  @Test
  void positiveRejectsZero() {
    assertThat(new PositiveRule().test(0).isPassed()).isFalse();
  }

  @Test
  void positiveRejectsNegativeValue() {
    assertThat(new PositiveRule().test(-1).isPassed()).isFalse();
  }

  @Test
  void positiveAcceptsPositiveBigDecimal() {
    assertThat(new PositiveRule().test(BigDecimal.valueOf(0.01)).isPassed()).isTrue();
  }

  @Test
  void positiveRejectsZeroBigDecimal() {
    assertThat(new PositiveRule().test(BigDecimal.ZERO).isPassed()).isFalse();
  }

  @Test
  void positiveRejectsNull() {
    assertThat(new PositiveRule().test(null).isPassed()).isFalse();
  }

  @Test
  void positiveRejectsNonNumber() {
    assertThat(new PositiveRule().test("5").isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PositiveOrZeroRule
  // -------------------------------------------------------------------------

  @Test
  void positiveOrZeroAcceptsZero() {
    assertThat(new PositiveOrZeroRule().test(0).isPassed()).isTrue();
  }

  @Test
  void positiveOrZeroAcceptsPositiveValue() {
    assertThat(new PositiveOrZeroRule().test(1).isPassed()).isTrue();
  }

  @Test
  void positiveOrZeroRejectsNegativeValue() {
    assertThat(new PositiveOrZeroRule().test(-1).isPassed()).isFalse();
  }

  @Test
  void positiveOrZeroAcceptsZeroBigDecimal() {
    assertThat(new PositiveOrZeroRule().test(BigDecimal.ZERO).isPassed()).isTrue();
  }

  @Test
  void positiveOrZeroRejectsNull() {
    assertThat(new PositiveOrZeroRule().test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // NegativeRule
  // -------------------------------------------------------------------------

  @Test
  void negativeAcceptsValueBelowZero() {
    assertThat(new NegativeRule().test(-1).isPassed()).isTrue();
  }

  @Test
  void negativeRejectsZero() {
    assertThat(new NegativeRule().test(0).isPassed()).isFalse();
  }

  @Test
  void negativeRejectsPositiveValue() {
    assertThat(new NegativeRule().test(1).isPassed()).isFalse();
  }

  @Test
  void negativeAcceptsNegativeBigDecimal() {
    assertThat(new NegativeRule().test(BigDecimal.valueOf(-0.01)).isPassed()).isTrue();
  }

  @Test
  void negativeRejectsNull() {
    assertThat(new NegativeRule().test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // NegativeOrZeroRule
  // -------------------------------------------------------------------------

  @Test
  void negativeOrZeroAcceptsZero() {
    assertThat(new NegativeOrZeroRule().test(0).isPassed()).isTrue();
  }

  @Test
  void negativeOrZeroAcceptsNegativeValue() {
    assertThat(new NegativeOrZeroRule().test(-1).isPassed()).isTrue();
  }

  @Test
  void negativeOrZeroRejectsPositiveValue() {
    assertThat(new NegativeOrZeroRule().test(1).isPassed()).isFalse();
  }

  @Test
  void negativeOrZeroAcceptsZeroBigDecimal() {
    assertThat(new NegativeOrZeroRule().test(BigDecimal.ZERO).isPassed()).isTrue();
  }

  @Test
  void negativeOrZeroRejectsNull() {
    assertThat(new NegativeOrZeroRule().test(null).isPassed()).isFalse();
  }
}

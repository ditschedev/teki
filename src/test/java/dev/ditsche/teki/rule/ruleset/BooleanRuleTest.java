package dev.ditsche.teki.rule.ruleset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import org.junit.jupiter.api.Test;

class BooleanRuleTest {

  // -------------------------------------------------------------------------
  // BooleanRule
  // -------------------------------------------------------------------------

  @Test
  void trueRuleAcceptsTrue() {
    assertThat(new BooleanRule(true).test(true).passed()).isTrue();
  }

  @Test
  void trueRuleRejectsFalse() {
    assertThat(new BooleanRule(true).test(false).passed()).isFalse();
  }

  @Test
  void falseRuleAcceptsFalse() {
    assertThat(new BooleanRule(false).test(false).passed()).isTrue();
  }

  @Test
  void falseRuleRejectsTrue() {
    assertThat(new BooleanRule(false).test(true).passed()).isFalse();
  }

  @Test
  void booleanRuleRejectsNull() {
    assertThat(new BooleanRule(true).test(null).passed()).isFalse();
  }

  @Test
  void booleanRuleRejectsNonBoolean() {
    assertThat(new BooleanRule(true).test("true").passed()).isFalse();
  }

  @Test
  void paramsContainsExpected() {
    assertThat(new BooleanRule(true).params()).containsEntry("expected", true);
    assertThat(new BooleanRule(false).params()).containsEntry("expected", false);
  }

  // -------------------------------------------------------------------------
  // BooleanRuleBuilder
  // -------------------------------------------------------------------------

  static class Form {
    Boolean accepted;
    Boolean active;

    Form(Boolean accepted, Boolean active) {
      this.accepted = accepted;
      this.active = active;
    }
  }

  @Test
  void isTruePassesWhenTrue() {
    Teki teki = Teki.fromRules(Rules.bool("accepted").isTrue());
    assertThat(teki.validate(new Form(true, null))).isNotNull();
  }

  @Test
  void isTrueFailsWhenFalse() {
    Teki teki = Teki.fromRules(Rules.bool("accepted").isTrue());
    assertThatThrownBy(() -> teki.validate(new Form(false, null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void isFalsePassesWhenFalse() {
    Teki teki = Teki.fromRules(Rules.bool("active").isFalse());
    assertThat(teki.validate(new Form(null, false))).isNotNull();
  }

  @Test
  void isFalseFailsWhenTrue() {
    Teki teki = Teki.fromRules(Rules.bool("active").isFalse());
    assertThatThrownBy(() -> teki.validate(new Form(null, true)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void requiredRejectsNull() {
    Teki teki = Teki.fromRules(Rules.bool("accepted").required());
    assertThatThrownBy(() -> teki.validate(new Form(null, null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void optionalSkipsNull() {
    Teki teki = Teki.fromRules(Rules.bool("accepted").optional().isTrue());
    assertThat(teki.validate(new Form(null, null))).isNotNull();
  }
}

package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.annotation.Required;
import dev.ditsche.teki.annotation.TekiConstraint;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;

class TekiConstraintTest {

  // --- Stateless custom annotation (no-arg rule constructor) ---

  @TekiConstraint(rule = AlwaysFailRule.class)
  @Target({ElementType.FIELD, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @interface AlwaysFail {}

  static class AlwaysFailRule implements Rule {
    @Override
    public RuleResult test(Object value) {
      return RuleResult.reject();
    }

    @Override
    public String message(String field) {
      return "\"" + field + "\" always fails";
    }

    @Override
    public String getType() {
      return "validation.error.always_fail";
    }
  }

  // --- Parameterized custom annotation (annotation-taking rule constructor) ---

  @TekiConstraint(rule = ExactLengthRule.class)
  @Target({ElementType.FIELD, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @interface ExactLength {
    int value();
  }

  static class ExactLengthRule implements Rule {
    private final int expected;

    ExactLengthRule(ExactLength annotation) {
      this.expected = annotation.value();
    }

    @Override
    public RuleResult test(Object value) {
      if (!(value instanceof String s)) return RuleResult.reject();
      return RuleResult.passes(s.length() == expected);
    }

    @Override
    public String message(String field) {
      return "\"" + field + "\" must be exactly " + expected + " characters";
    }

    @Override
    public String getType() {
      return "validation.error.exact_length";
    }
  }

  // --- Forms ---

  static class NoArgForm {
    @Required @AlwaysFail String value;

    NoArgForm(String value) {
      this.value = value;
    }
  }

  static class ParameterizedForm {
    @ExactLength(5) String code;

    ParameterizedForm(String code) {
      this.code = code;
    }
  }

  // --- Tests ---

  @Test
  void noArgCustomRulePickedUpByScanner() {
    Teki teki = Teki.from(NoArgForm.class);
    assertThatThrownBy(() -> teki.validate(new NoArgForm("anything")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void parameterizedCustomRuleReadsAnnotationValue() {
    Teki teki = Teki.from(ParameterizedForm.class);
    assertThat(teki.validate(new ParameterizedForm("hello"))).isNotNull();
    assertThatThrownBy(() -> teki.validate(new ParameterizedForm("hi")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void parameterizedCustomRuleRespectsDifferentValues() {
    // Each annotation instance carries its own parameter — scanner must use the annotation value,
    // not a cached rule.
    Teki teki = Teki.from(ParameterizedForm.class);
    assertThat(teki.validate(new ParameterizedForm("12345"))).isNotNull();
    assertThatThrownBy(() -> teki.validate(new ParameterizedForm("123")))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void customRuleAlsoWorksInFluentApi() {
    // Custom rules can be added to the fluent API via .custom() — validate integration
    Teki teki = Teki.fromRules(
        dev.ditsche.teki.rule.builder.Rules.string("code").required().custom(new ExactLengthRule(
            ParameterizedForm.class.getDeclaredFields()[0].getAnnotation(ExactLength.class))));

    assertThatThrownBy(() -> teki.validate(new ParameterizedForm("hi")))
        .isInstanceOf(ValidationException.class);
  }
}

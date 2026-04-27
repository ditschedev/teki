package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import org.junit.jupiter.api.Test;

class ConditionalValidationTest {

  static class PersonForm {
    int age;
    String parentName;
    String guardianEmail;

    PersonForm(int age, String parentName, String guardianEmail) {
      this.age = age;
      this.parentName = parentName;
      this.guardianEmail = guardianEmail;
    }
  }

  static class AccountForm {
    String accountType;
    String vatNumber;
    String companyName;

    AccountForm(String accountType, String vatNumber, String companyName) {
      this.accountType = accountType;
      this.vatNumber = vatNumber;
      this.companyName = companyName;
    }
  }

  @Test
  void whenConditionTrueAppliesRules() {
    Teki teki =
        Teki.fromRules(Rules.number("age").required(), Rules.string("parentName").optional())
            .when(
                (PersonForm f) -> f.age < 18,
                Rules.string("parentName").required());

    assertThatThrownBy(() -> teki.validate(new PersonForm(16, null, null)))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex ->
                assertThat(((ValidationException) ex).getErrors())
                    .anyMatch(e -> e.getField().equals("parentName")));
  }

  @Test
  void whenConditionFalseSkipsRules() {
    Teki teki =
        Teki.fromRules(Rules.number("age").required(), Rules.string("parentName").optional())
            .when(
                (PersonForm f) -> f.age < 18,
                Rules.string("parentName").required());

    assertThat(teki.check(new PersonForm(25, null, null)).isValid()).isTrue();
  }

  @Test
  void whenConditionTrueAppliesMultipleRules() {
    Teki teki =
        Teki.fromRules(
                Rules.number("age").required(),
                Rules.string("parentName").optional(),
                Rules.string("guardianEmail").optional())
            .when(
                (PersonForm f) -> f.age < 18,
                Rules.string("parentName").required(),
                Rules.string("guardianEmail").required().email());

    assertThatThrownBy(() -> teki.validate(new PersonForm(15, null, null)))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> assertThat(((ValidationException) ex).getErrors()).hasSize(2));
  }

  @Test
  void whenConditionTrueButRulesPassValidationSucceeds() {
    Teki teki =
        Teki.fromRules(Rules.number("age").required(), Rules.string("parentName").optional())
            .when(
                (PersonForm f) -> f.age < 18,
                Rules.string("parentName").required());

    assertThat(teki.check(new PersonForm(16, "Jane Doe", null)).isValid()).isTrue();
  }

  @Test
  void multipleWhenBlocksAreEvaluatedIndependently() {
    Teki teki =
        Teki.fromRules(
                Rules.string("accountType").required(),
                Rules.string("vatNumber").optional(),
                Rules.string("companyName").optional())
            .when(
                (AccountForm f) -> "business".equals(f.accountType),
                Rules.string("vatNumber").required())
            .when(
                (AccountForm f) -> "business".equals(f.accountType),
                Rules.string("companyName").required());

    assertThatThrownBy(() -> teki.validate(new AccountForm("business", null, null)))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> assertThat(((ValidationException) ex).getErrors()).hasSize(2));

    assertThat(teki.check(new AccountForm("personal", null, null)).isValid()).isTrue();
  }

  @Test
  void whenBlockOnlyRunsWhenConditionHolds() {
    Teki teki =
        Teki.fromRules(
                Rules.string("accountType").required(),
                Rules.string("vatNumber").optional())
            .when(
                (AccountForm f) -> "business".equals(f.accountType),
                Rules.string("vatNumber").required());

    assertThat(teki.check(new AccountForm("personal", null, null)).isValid()).isTrue();
    assertThatThrownBy(() -> teki.validate(new AccountForm("business", null, null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void whenAndConstraintBothApply() {
    Teki teki =
        Teki.fromRules(
                Rules.number("age").required(),
                Rules.string("parentName").optional())
            .when(
                (PersonForm f) -> f.age < 18,
                Rules.string("parentName").required())
            .constraint(
                (PersonForm f) -> f.age >= 0,
                "age",
                "validation.error.age.negative",
                "Age must not be negative");

    assertThat(teki.check(new PersonForm(20, null, null)).isValid()).isTrue();
    assertThatThrownBy(() -> teki.validate(new PersonForm(15, null, null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void whenOnCachedInstanceThrows() {
    Teki cached = Teki.from(PersonForm.class);
    assertThatThrownBy(
            () -> cached.when((PersonForm f) -> f.age < 18, Rules.string("parentName").required()))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void whenAcceptsPrebuiltValidatables() {
    var parentRule = Rules.string("parentName").required().build();

    Teki teki =
        Teki.fromRules(Rules.number("age").required(), Rules.string("parentName").optional())
            .when((PersonForm f) -> f.age < 18, parentRule);

    assertThatThrownBy(() -> teki.validate(new PersonForm(10, null, null)))
        .isInstanceOf(ValidationException.class);
    assertThat(teki.check(new PersonForm(20, null, null)).isValid()).isTrue();
  }
}

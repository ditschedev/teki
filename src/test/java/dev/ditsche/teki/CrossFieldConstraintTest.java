package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import org.junit.jupiter.api.Test;

class CrossFieldConstraintTest {

  static class PasswordForm {
    String password;
    String confirmPassword;

    PasswordForm(String password, String confirmPassword) {
      this.password = password;
      this.confirmPassword = confirmPassword;
    }
  }

  static class ShippingForm {
    String shippingMode;
    String billingAddress;

    ShippingForm(String shippingMode, String billingAddress) {
      this.shippingMode = shippingMode;
      this.billingAddress = billingAddress;
    }
  }

  @Test
  void passingConstraintDoesNotAddError() {
    Teki teki =
        Teki.fromRules(Rules.string("password").required(), Rules.string("confirmPassword").required())
            .constraint(
                (PasswordForm f) -> f.password.equals(f.confirmPassword),
                "confirmPassword",
                "Passwords must match");

    PasswordForm form = new PasswordForm("secret", "secret");
    assertThat(teki.validate(form)).isSameAs(form);
  }

  @Test
  void failingConstraintAddsErrorOnCorrectField() {
    Teki teki =
        Teki.fromRules(Rules.string("password").required(), Rules.string("confirmPassword").required())
            .constraint(
                (PasswordForm f) -> f.password.equals(f.confirmPassword),
                "confirmPassword",
                "Passwords must match");

    assertThatThrownBy(() -> teki.validate(new PasswordForm("secret", "different")))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> {
              var errors = ((ValidationException) ex).getErrors();
              assertThat(errors).hasSize(1);
              assertThat(errors.iterator().next().getField()).isEqualTo("confirmPassword");
            });
  }

  @Test
  void constraintRunsAfterPerFieldValidation() {
    // If field-level validation already fails, constraint errors are still collected.
    Teki teki =
        Teki.fromRules(Rules.string("password").required(), Rules.string("confirmPassword").required())
            .constraint(
                (PasswordForm f) -> f.password != null && f.password.equals(f.confirmPassword),
                "confirmPassword",
                "Passwords must match");

    // Both fields null → field error (required) + constraint error
    assertThatThrownBy(() -> teki.validate(new PasswordForm(null, null)))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> assertThat(((ValidationException) ex).getErrors()).hasSizeGreaterThanOrEqualTo(2));
  }

  @Test
  void conditionalRequiredViaConstraint() {
    Teki teki =
        Teki.fromRules(Rules.string("shippingMode").required())
            .constraint(
                (ShippingForm f) -> !"separate".equals(f.shippingMode) || f.billingAddress != null,
                "billingAddress",
                "Billing address is required when shipping mode is 'separate'");

    assertThat(teki.validate(new ShippingForm("same", null))).isNotNull();
    assertThatThrownBy(() -> teki.validate(new ShippingForm("separate", null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void multipleConstraintsAllEvaluated() {
    Teki teki =
        Teki.fromRules(Rules.string("password").required(), Rules.string("confirmPassword").required())
            .constraint(
                (PasswordForm f) -> f.password.equals(f.confirmPassword),
                "confirmPassword",
                "Passwords must match")
            .constraint(
                (PasswordForm f) -> f.password.length() >= 8,
                "password",
                "Password must be at least 8 characters");

    assertThatThrownBy(() -> teki.validate(new PasswordForm("short", "short")))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> assertThat(((ValidationException) ex).getErrors()).hasSize(1));

    assertThatThrownBy(() -> teki.validate(new PasswordForm("short", "different")))
        .isInstanceOf(ValidationException.class)
        .satisfies(
            ex -> assertThat(((ValidationException) ex).getErrors()).hasSize(2));
  }

  @Test
  void checkReturnsOutcomeWithConstraintErrors() {
    Teki teki =
        Teki.fromRules(Rules.string("password").required(), Rules.string("confirmPassword").required())
            .constraint(
                (PasswordForm f) -> f.password.equals(f.confirmPassword),
                "confirmPassword",
                "Passwords must match");

    ValidationOutcome<PasswordForm> outcome = teki.check(new PasswordForm("a", "b"));
    assertThat(outcome.isValid()).isFalse();
    assertThat(outcome.getErrors()).hasSize(1);
  }

  @Test
  void constraintOnCachedInstanceThrows() {
    Teki cached = Teki.from(PasswordForm.class);
    assertThatThrownBy(
            () -> cached.constraint((PasswordForm f) -> true, "password", "msg"))
        .isInstanceOf(IllegalStateException.class);
  }
}

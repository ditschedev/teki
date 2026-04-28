package dev.ditsche.teki.examples;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationError;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import java.util.Collection;

/**
 * Demonstrates cross-field constraints and conditional validation.
 *
 * <p>Run via IDE or:
 *
 * <pre>
 *   mvn exec:java -Dexec.mainClass="dev.ditsche.teki.examples.CrossFieldAndConditionalExample" -Dexec.classpathScope=test
 * </pre>
 */
public class CrossFieldAndConditionalExample {

  // --- cross-field: password confirmation ---

  record PasswordForm(String password, String confirmPassword) {}

  static void passwordExample() {
    System.out.println("=== Password confirmation (cross-field constraint) ===");

    Teki schema =
        Teki.fromRules(
                Rules.string("password").required().between(8, 100),
                Rules.string("confirmPassword").required())
            .constraint(
                (PasswordForm f) -> f.password().equals(f.confirmPassword()),
                "confirmPassword",
                "password.mismatch",
                "Passwords must match");

    System.out.print("Matching passwords: ");
    printOutcome(schema, new PasswordForm("s3cr3t!!", "s3cr3t!!"));

    System.out.print("Mismatched passwords: ");
    printOutcome(schema, new PasswordForm("s3cr3t!!", "wrong"));

    System.out.print("Too short password: ");
    printOutcome(schema, new PasswordForm("abc", "abc"));
  }

  // --- conditional: billing address required only for invoice payment ---

  record OrderForm(String paymentMethod, String billingAddress) {}

  static void conditionalExample() {
    System.out.println(
        "\n=== Billing address required only for 'invoice' payment (conditional) ===");

    Teki schema =
        Teki.fromRules(Rules.string("paymentMethod").required())
            .when(
                (OrderForm f) -> "invoice".equals(f.paymentMethod()),
                Rules.string("billingAddress").required());

    System.out.print("Credit card, no billing address: ");
    printOutcome(schema, new OrderForm("credit_card", null));

    System.out.print("Invoice, no billing address: ");
    printOutcome(schema, new OrderForm("invoice", null));

    System.out.print("Invoice, with billing address: ");
    printOutcome(schema, new OrderForm("invoice", "123 Main St"));
  }

  public static void main(String[] args) {
    passwordExample();
    conditionalExample();
  }

  static void printOutcome(Teki schema, Object input) {
    try {
      schema.validate(input);
      System.out.println("passed");
    } catch (ValidationException e) {
      System.out.println("failed");
      printErrors(e.getErrors());
    }
  }

  static void printErrors(Collection<ValidationError> errors) {
    errors.forEach(
        err ->
            err.getErrors()
                .forEach(
                    info -> System.out.printf("  %-25s %s%n", err.getField(), info.getMessage())));
  }
}

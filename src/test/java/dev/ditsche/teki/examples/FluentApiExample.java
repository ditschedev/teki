package dev.ditsche.teki.examples;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationError;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import java.util.Collection;

/**
 * Demonstrates the fluent builder API.
 *
 * <p>Run via IDE or:
 *
 * <pre>
 *   mvn exec:java -Dexec.mainClass="dev.ditsche.teki.examples.FluentApiExample" -Dexec.classpathScope=test
 * </pre>
 */
public class FluentApiExample {

  record RegistrationForm(String email, String username, int age) {}

  public static void main(String[] args) {
    Teki schema =
        Teki.fromRules(
            Rules.string(RegistrationForm::email).required().email(),
            Rules.string(RegistrationForm::username).required().between(3, 20).trim(),
            Rules.number(RegistrationForm::age).required().min(18).max(120));

    // --- valid input ---
    System.out.println("=== Valid input ===");
    RegistrationForm valid = new RegistrationForm("alice@example.com", "alice99 ", 25);
    RegistrationForm result = schema.validate(valid);
    System.out.println("Passed: " + result);

    // --- invalid input via validate() ---
    System.out.println("\n=== Invalid input (validate throws) ===");
    RegistrationForm invalid = new RegistrationForm("not-an-email", "ab", 15);
    try {
      schema.validate(invalid);
    } catch (ValidationException e) {
      printErrors(e.getErrors());
    }

    // --- non-throwing check() ---
    System.out.println("\n=== Invalid input (check does not throw) ===");
    schema.check(invalid).orElse(FluentApiExample::printErrors);
  }

  static void printErrors(Collection<ValidationError> errors) {
    errors.forEach(
        err ->
            err.getErrors()
                .forEach(
                    info ->
                        System.out.printf(
                            "  %-20s [%s] %s%n",
                            err.getField(), info.getErrorType(), info.getMessage())));
  }
}

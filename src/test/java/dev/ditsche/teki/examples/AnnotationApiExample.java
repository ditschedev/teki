package dev.ditsche.teki.examples;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.annotation.Between;
import dev.ditsche.teki.annotation.Default;
import dev.ditsche.teki.annotation.Email;
import dev.ditsche.teki.annotation.Max;
import dev.ditsche.teki.annotation.Min;
import dev.ditsche.teki.annotation.Required;
import dev.ditsche.teki.annotation.Trim;
import dev.ditsche.teki.error.ValidationError;
import dev.ditsche.teki.error.ValidationException;
import java.util.Collection;

/**
 * Demonstrates the annotation-driven API.
 *
 * <p>Run via IDE or:
 *
 * <pre>
 *   mvn exec:java -Dexec.mainClass="dev.ditsche.teki.examples.AnnotationApiExample" -Dexec.classpathScope=test
 * </pre>
 */
public class AnnotationApiExample {

  static class UserForm {
    @Required @Email String email;

    @Required
    @Trim
    @Between(min = 2, max = 50)
    String name;

    @Min(18)
    @Max(120)
    Integer age;

    @Default("user")
    String role;

    UserForm(String email, String name, Integer age, String role) {
      this.email = email;
      this.name = name;
      this.age = age;
      this.role = role;
    }

    @Override
    public String toString() {
      return "UserForm{email='"
          + email
          + "', name='"
          + name
          + "', age="
          + age
          + ", role='"
          + role
          + "'}";
    }
  }

  public static void main(String[] args) {
    // Schema is built once from annotations and cached
    Teki schema = Teki.from(UserForm.class);

    // --- valid input, role gets its default ---
    System.out.println("=== Valid input ===");
    UserForm valid = new UserForm("bob@example.com", "  Bob  ", 30, null);
    UserForm result = schema.validate(valid);
    System.out.println("Passed: " + result);
    System.out.println("  name trimmed to: '" + result.name + "'");
    System.out.println("  role defaulted to: '" + result.role + "'");

    // --- invalid: missing email, underage ---
    System.out.println("\n=== Invalid input ===");
    UserForm invalid = new UserForm(null, "B", 12, null);
    try {
      schema.validate(invalid);
    } catch (ValidationException e) {
      printErrors(e.getErrors());
    }
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

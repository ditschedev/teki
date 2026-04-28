package dev.ditsche.teki.examples;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.TekiMessages;
import dev.ditsche.teki.error.ValidationError;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import java.util.Collection;

/**
 * Demonstrates message customization via {@link TekiMessages}.
 *
 * <p>Run via IDE or:
 *
 * <pre>
 *   mvn exec:java -Dexec.mainClass="dev.ditsche.teki.examples.MessageResolverExample" -Dexec.classpathScope=test
 * </pre>
 */
public class MessageResolverExample {

  record AgeForm(int age) {}

  public static void main(String[] args) {
    Teki schema = Teki.fromRules(Rules.number("age").required().min(18).max(120));

    // --- default English messages ---
    System.out.println("=== Default English messages ===");
    printValidationErrors(schema, new AgeForm(10));

    // --- single-field override via TekiMessages ---
    System.out.println("\n=== Override size.min message ===");
    Teki schemaWithOverride =
        Teki.fromRules(Rules.number("age").required().min(18).max(120))
            .messages(
                TekiMessages.defaults()
                    .override(TekiErrors.MIN, "You must be at least {min} years old")
                    .override(TekiErrors.MAX, "Maximum age is {max}"));
    printValidationErrors(schemaWithOverride, new AgeForm(10));
    printValidationErrors(schemaWithOverride, new AgeForm(200));

    // --- global German messages loaded from a map ---
    System.out.println("\n=== Global German messages ===");
    Teki.setGlobalMessages(
        TekiMessages.empty()
            .override(TekiErrors.REQUIRED, "Das Feld \"{field}\" ist erforderlich")
            .override(TekiErrors.EMAIL, "Das Feld \"{field}\" muss eine gültige E-Mail sein")
            .override(TekiErrors.MIN, "Das Feld \"{field}\" muss mindestens {min} betragen")
            .override(TekiErrors.MAX, "Das Feld \"{field}\" darf höchstens {max} betragen"));
    printValidationErrors(schema, new AgeForm(10));

    // --- load from a properties file on the classpath ---
    // Teki.setGlobalMessages(TekiMessages.defaults().fromProperties("i18n/messages_de.properties"));

    // --- per-schema resolver still overrides the global messages ---
    System.out.println("\n=== Per-schema override on top of global German ===");
    printValidationErrors(schemaWithOverride, new AgeForm(10));

    // Reset to built-in English defaults
    Teki.clearGlobalResolver();
    System.out.println("\n=== Restored English defaults ===");
    printValidationErrors(schema, new AgeForm(10));
  }

  static void printValidationErrors(Teki schema, Object input) {
    try {
      schema.validate(input);
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

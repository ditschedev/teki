package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class TekiMessagesPropertiesTest {

  record EmailForm(String email) {}

  record AgeForm(int age) {}

  @AfterEach
  void resetGlobal() {
    Teki.clearGlobalResolver();
  }

  @Test
  void fromPropertiesLoadsCustomRequiredMessage() {
    TekiMessages messages = TekiMessages.defaults().fromProperties("messages_test.properties");
    Teki teki = Teki.fromRules(Rules.string("email").required()).messages(messages);

    try {
      teki.validate(new EmailForm(null));
    } catch (ValidationException e) {
      boolean found =
          e.getErrors().stream()
              .flatMap(err -> err.getErrors().stream())
              .map(info -> info.getMessage())
              .anyMatch(msg -> msg.equals("Das Feld \"email\" ist erforderlich"));
      assertThat(found).as("expected German required message to be present").isTrue();
    }
  }

  @Test
  void fromPropertiesOverridesMinMessage() {
    TekiMessages messages = TekiMessages.defaults().fromProperties("messages_test.properties");
    Teki teki = Teki.fromRules(Rules.number("age").required().min(18)).messages(messages);

    try {
      teki.validate(new AgeForm(5));
    } catch (ValidationException e) {
      String message =
          e.getErrors().stream()
              .flatMap(err -> err.getErrors().stream())
              .map(info -> info.getMessage())
              .findFirst()
              .orElseThrow();
      assertThat(message).isEqualTo("Das Feld \"age\" muss mindestens 18 betragen");
    }
  }

  @Test
  void fromPropertiesWithMissingResourceThrows() {
    assertThatThrownBy(() -> TekiMessages.defaults().fromProperties("nonexistent.properties"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("nonexistent.properties");
  }

  @Test
  void globalMessagesFromPropertiesApplyToAllSchemas() {
    Teki.setGlobalMessages(TekiMessages.defaults().fromProperties("messages_test.properties"));
    Teki teki = Teki.fromRules(Rules.string("email").required());

    try {
      teki.validate(new EmailForm(null));
    } catch (ValidationException e) {
      boolean found =
          e.getErrors().stream()
              .flatMap(err -> err.getErrors().stream())
              .map(info -> info.getMessage())
              .anyMatch(msg -> msg.equals("Das Feld \"email\" ist erforderlich"));
      assertThat(found).as("expected German required message to be present").isTrue();
    }
  }
}

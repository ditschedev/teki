package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.error.ValidationError;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Rules;
import dev.ditsche.teki.rule.ruleset.EmailRule;
import java.util.List;
import org.junit.jupiter.api.Test;

class ValidationArrayTest {

  static class Form {
    List<String> tags;

    Form(List<String> tags) {
      this.tags = tags;
    }
  }

  static class Item {
    String sku;

    Item(String sku) {
      this.sku = sku;
    }
  }

  static class ItemForm {
    List<Item> items;

    ItemForm(List<Item> items) {
      this.items = items;
    }
  }

  @Test
  void validListPassesArrayValidation() {
    Teki teki =
        Teki.fromRules(Rules.array("tags").required().elements().string().required().build());
    Form form = new Form(List.of("java", "kotlin"));
    assertThat(teki.validate(form)).isSameAs(form);
  }

  @Test
  void childRuleValidatedForEachElement() {
    Teki teki =
        Teki.fromRules(
            Rules.array("tags")
                .required()
                .elements()
                .string()
                .required()
                .custom(new EmailRule())
                .build());
    Form valid = new Form(List.of("a@b.com", "c@d.com"));
    assertThat(teki.validate(valid)).isSameAs(valid);

    Form invalid = new Form(List.of("a@b.com", "not-an-email"));
    assertThatThrownBy(() -> teki.validate(invalid)).isInstanceOf(ValidationException.class);
  }

  @Test
  void emptyRequiredListFails() {
    Teki teki = Teki.fromRules(Rules.array("tags").required().build());
    Form form = new Form(List.of());
    assertThatThrownBy(() -> teki.validate(form)).isInstanceOf(ValidationException.class);
  }

  @Test
  void nullOptionalArrayDoesNotFail() {
    Teki teki = Teki.fromRules(Rules.array("tags").optional().build());
    Form form = new Form(null);
    assertThat(teki.validate(form)).isSameAs(form);
  }

  @Test
  void maxLengthEnforced() {
    Teki teki = Teki.fromRules(Rules.array("tags").required().max(2).build());
    Form valid = new Form(List.of("a", "b"));
    assertThat(teki.validate(valid)).isSameAs(valid);

    Form invalid = new Form(List.of("a", "b", "c"));
    assertThatThrownBy(() -> teki.validate(invalid)).isInstanceOf(ValidationException.class);
  }

  @Test
  void failingElementAtIndex1HasIndexedPath() {
    Teki teki =
        Teki.fromRules(
            Rules.array("tags")
                .required()
                .elements()
                .string()
                .required()
                .custom(new EmailRule())
                .build());
    Form form = new Form(List.of("a@b.com", "not-an-email"));
    ValidationOutcome<Form> outcome = teki.check(form);
    assertThat(outcome.isValid()).isFalse();
    assertThat(outcome.getErrors())
        .extracting(ValidationError::getField)
        .containsExactly("tags[1]");
  }

  @Test
  void nestedObjectErrorIncludesIndexInPath() {
    Teki teki =
        Teki.fromRules(
            Rules.array("items").required().objects(Rules.string("sku").required()).build());
    ItemForm form = new ItemForm(List.of(new Item(null)));
    ValidationOutcome<ItemForm> outcome = teki.check(form);
    assertThat(outcome.isValid()).isFalse();
    assertThat(outcome.getErrors())
        .extracting(ValidationError::getField)
        .containsExactly("items[0].sku");
  }
}

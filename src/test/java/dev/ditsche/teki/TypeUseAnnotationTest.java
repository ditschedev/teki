package dev.ditsche.teki;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.annotation.Between;
import dev.ditsche.teki.annotation.Email;
import dev.ditsche.teki.annotation.Max;
import dev.ditsche.teki.annotation.Min;
import dev.ditsche.teki.annotation.Required;
import dev.ditsche.teki.annotation.Trim;
import dev.ditsche.teki.error.ValidationException;
import java.util.List;
import org.junit.jupiter.api.Test;

class TypeUseAnnotationTest {

  static class EmailListForm {
    @Required List<@Email String> recipients;

    EmailListForm(List<String> recipients) {
      this.recipients = recipients;
    }
  }

  static class SizeConstrainedForm {
    @Between(min = 1, max = 3)
    List<@Min(0) @Max(100) Integer> scores;

    SizeConstrainedForm(List<Integer> scores) {
      this.scores = scores;
    }
  }

  static class OptionalTagsForm {
    List<@Required String> tags;

    OptionalTagsForm(List<String> tags) {
      this.tags = tags;
    }
  }

  static class TrimElementsForm {
    List<@Trim String> names;

    TrimElementsForm(List<String> names) {
      this.names = names;
    }
  }

  @Test
  void validEmailListPasses() {
    Teki teki = Teki.from(EmailListForm.class);
    var form = new EmailListForm(List.of("a@b.com", "c@d.com"));
    assertThat(teki.validate(form)).isSameAs(form);
  }

  @Test
  void invalidEmailInListFails() {
    Teki teki = Teki.from(EmailListForm.class);
    assertThatThrownBy(() -> teki.validate(new EmailListForm(List.of("a@b.com", "not-an-email"))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void nullRequiredListFails() {
    Teki teki = Teki.from(EmailListForm.class);
    assertThatThrownBy(() -> teki.validate(new EmailListForm(null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void allEmailsInvalidProducesErrors() {
    Teki teki = Teki.from(EmailListForm.class);
    assertThatThrownBy(() -> teki.validate(new EmailListForm(List.of("bad1", "bad2", "bad3"))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void collectionSizeConstraintEnforced() {
    Teki teki = Teki.from(SizeConstrainedForm.class);
    assertThat(teki.validate(new SizeConstrainedForm(List.of(10, 50)))).isNotNull();
    // Too many elements
    assertThatThrownBy(() -> teki.validate(new SizeConstrainedForm(List.of(10, 20, 30, 40))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void elementValueConstraintEnforced() {
    Teki teki = Teki.from(SizeConstrainedForm.class);
    // Element value out of [0,100]
    assertThatThrownBy(() -> teki.validate(new SizeConstrainedForm(List.of(-1, 50))))
        .isInstanceOf(ValidationException.class);
    assertThatThrownBy(() -> teki.validate(new SizeConstrainedForm(List.of(50, 200))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void nullOptionalListWithElementAnnotationsSkipsValidation() {
    Teki teki = Teki.from(OptionalTagsForm.class);
    // No @Required on the field → optional; null list passes
    assertThat(teki.validate(new OptionalTagsForm(null))).isNotNull();
  }

  @Test
  void nullElementInListFailsRequiredElementRule() {
    Teki teki = Teki.from(OptionalTagsForm.class);
    assertThatThrownBy(() -> teki.validate(new OptionalTagsForm(List.of("a", ""))))
        .isInstanceOf(ValidationException.class);
  }
}

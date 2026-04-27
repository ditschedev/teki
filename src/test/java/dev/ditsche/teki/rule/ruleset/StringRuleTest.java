package dev.ditsche.teki.rule.ruleset;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StringRuleTest {

  // -------------------------------------------------------------------------
  // EmailRule
  // -------------------------------------------------------------------------

  @Test
  void emailAcceptsValidAddress() {
    assertThat(new EmailRule().test("user@example.com").isPassed()).isTrue();
  }

  @Test
  void emailRejectsAddressWithoutAt() {
    assertThat(new EmailRule().test("notanemail").isPassed()).isFalse();
  }

  @Test
  void emailRejectsNull() {
    assertThat(new EmailRule().test(null).isPassed()).isFalse();
  }

  @Test
  void emailRejectsNonString() {
    assertThat(new EmailRule().test(42).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // UuidRule
  // -------------------------------------------------------------------------

  @Test
  void uuidAcceptsWellFormedLowercase() {
    assertThat(new UuidRule().test("550e8400-e29b-41d4-a716-446655440000").isPassed()).isTrue();
  }

  @Test
  void uuidAcceptsWellFormedUppercase() {
    assertThat(new UuidRule().test("550E8400-E29B-41D4-A716-446655440000").isPassed()).isTrue();
  }

  @Test
  void uuidRejectsMissingHyphens() {
    assertThat(new UuidRule().test("550e8400e29b41d4a716446655440000").isPassed()).isFalse();
  }

  @Test
  void uuidRejectsTooShort() {
    assertThat(new UuidRule().test("550e8400-e29b-41d4-a716").isPassed()).isFalse();
  }

  @Test
  void uuidRejectsNull() {
    assertThat(new UuidRule().test(null).isPassed()).isFalse();
  }

  @Test
  void uuidRejectsNonString() {
    assertThat(new UuidRule().test(123).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // NotBlankRule
  // -------------------------------------------------------------------------

  @Test
  void notBlankAcceptsNonEmptyString() {
    assertThat(new NotBlankRule().test("hello").isPassed()).isTrue();
  }

  @Test
  void notBlankRejectsWhitespaceOnly() {
    assertThat(new NotBlankRule().test("   ").isPassed()).isFalse();
  }

  @Test
  void notBlankRejectsEmptyString() {
    assertThat(new NotBlankRule().test("").isPassed()).isFalse();
  }

  @Test
  void notBlankRejectsNull() {
    assertThat(new NotBlankRule().test(null).isPassed()).isFalse();
  }

  @Test
  void notBlankRejectsTabAndNewline() {
    assertThat(new NotBlankRule().test("\t\n").isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // OneOfRule
  // -------------------------------------------------------------------------

  @Test
  void oneOfAcceptsAllowedValue() {
    assertThat(new OneOfRule("admin", "editor", "viewer").test("editor").isPassed()).isTrue();
  }

  @Test
  void oneOfRejectsUnknownValue() {
    assertThat(new OneOfRule("admin", "editor").test("superuser").isPassed()).isFalse();
  }

  @Test
  void oneOfIsCaseSensitive() {
    assertThat(new OneOfRule("admin").test("Admin").isPassed()).isFalse();
  }

  @Test
  void oneOfRejectsNull() {
    assertThat(new OneOfRule("admin").test(null).isPassed()).isFalse();
  }

  @Test
  void oneOfWorksWithSingleValue() {
    assertThat(new OneOfRule("only").test("only").isPassed()).isTrue();
    assertThat(new OneOfRule("only").test("other").isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // TrimRule
  // -------------------------------------------------------------------------

  @Test
  void trimTransformsLeadingAndTrailingWhitespace() {
    var result = new TrimRule().test("  hello  ");
    assertThat(result.isPassed()).isTrue();
    assertThat(result.isChanged()).isTrue();
    assertThat(result.getValue()).isEqualTo("hello");
  }

  @Test
  void trimPassesStringWithNoWhitespace() {
    var result = new TrimRule().test("hello");
    assertThat(result.isPassed()).isTrue();
  }

  @Test
  void trimRejectsNull() {
    assertThat(new TrimRule().test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PatternRule
  // -------------------------------------------------------------------------

  @Test
  void patternAcceptsMatchingString() {
    assertThat(new PatternRule("^[0-9]+$").test("12345").isPassed()).isTrue();
  }

  @Test
  void patternRejectsNonMatchingString() {
    assertThat(new PatternRule("^[0-9]+$").test("abc").isPassed()).isFalse();
  }

  @Test
  void patternRejectsNull() {
    assertThat(new PatternRule(".*").test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // AlphaNumericRule
  // -------------------------------------------------------------------------

  @Test
  void alphaNumericAcceptsLettersAndDigits() {
    assertThat(new AlphaNumericRule().test("abc123").isPassed()).isTrue();
  }

  @Test
  void alphaNumericRejectsSpecialCharacters() {
    assertThat(new AlphaNumericRule().test("abc!").isPassed()).isFalse();
  }

  @Test
  void alphaNumericRejectsNull() {
    assertThat(new AlphaNumericRule().test(null).isPassed()).isFalse();
  }
}

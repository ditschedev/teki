package dev.ditsche.teki.rule.ruleset;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class StringRuleTest {

  // -------------------------------------------------------------------------
  // EmailRule
  // -------------------------------------------------------------------------

  @Test
  void emailAcceptsValidAddress() {
    assertThat(new EmailRule().test("user@example.com").passed()).isTrue();
  }

  @Test
  void emailRejectsAddressWithoutAt() {
    assertThat(new EmailRule().test("notanemail").passed()).isFalse();
  }

  @Test
  void emailRejectsNull() {
    assertThat(new EmailRule().test(null).passed()).isFalse();
  }

  @Test
  void emailRejectsNonString() {
    assertThat(new EmailRule().test(42).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // UuidRule
  // -------------------------------------------------------------------------

  @Test
  void uuidAcceptsWellFormedLowercase() {
    assertThat(new UuidRule().test("550e8400-e29b-41d4-a716-446655440000").passed()).isTrue();
  }

  @Test
  void uuidAcceptsWellFormedUppercase() {
    assertThat(new UuidRule().test("550E8400-E29B-41D4-A716-446655440000").passed()).isTrue();
  }

  @Test
  void uuidRejectsMissingHyphens() {
    assertThat(new UuidRule().test("550e8400e29b41d4a716446655440000").passed()).isFalse();
  }

  @Test
  void uuidRejectsTooShort() {
    assertThat(new UuidRule().test("550e8400-e29b-41d4-a716").passed()).isFalse();
  }

  @Test
  void uuidRejectsNull() {
    assertThat(new UuidRule().test(null).passed()).isFalse();
  }

  @Test
  void uuidRejectsNonString() {
    assertThat(new UuidRule().test(123).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // NotBlankRule
  // -------------------------------------------------------------------------

  @Test
  void notBlankAcceptsNonEmptyString() {
    assertThat(new NotBlankRule().test("hello").passed()).isTrue();
  }

  @Test
  void notBlankRejectsWhitespaceOnly() {
    assertThat(new NotBlankRule().test("   ").passed()).isFalse();
  }

  @Test
  void notBlankRejectsEmptyString() {
    assertThat(new NotBlankRule().test("").passed()).isFalse();
  }

  @Test
  void notBlankRejectsNull() {
    assertThat(new NotBlankRule().test(null).passed()).isFalse();
  }

  @Test
  void notBlankRejectsTabAndNewline() {
    assertThat(new NotBlankRule().test("\t\n").passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // OneOfRule
  // -------------------------------------------------------------------------

  @Test
  void oneOfAcceptsAllowedValue() {
    assertThat(new OneOfRule("admin", "editor", "viewer").test("editor").passed()).isTrue();
  }

  @Test
  void oneOfRejectsUnknownValue() {
    assertThat(new OneOfRule("admin", "editor").test("superuser").passed()).isFalse();
  }

  @Test
  void oneOfIsCaseSensitive() {
    assertThat(new OneOfRule("admin").test("Admin").passed()).isFalse();
  }

  @Test
  void oneOfRejectsNull() {
    assertThat(new OneOfRule("admin").test(null).passed()).isFalse();
  }

  @Test
  void oneOfWorksWithSingleValue() {
    assertThat(new OneOfRule("only").test("only").passed()).isTrue();
    assertThat(new OneOfRule("only").test("other").passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // TrimRule
  // -------------------------------------------------------------------------

  @Test
  void trimTransformsLeadingAndTrailingWhitespace() {
    var result = new TrimRule().test("  hello  ");
    assertThat(result.passed()).isTrue();
    assertThat(result.changed()).isTrue();
    assertThat(result.value()).isEqualTo("hello");
  }

  @Test
  void trimPassesStringWithNoWhitespace() {
    var result = new TrimRule().test("hello");
    assertThat(result.passed()).isTrue();
  }

  @Test
  void trimRejectsNull() {
    assertThat(new TrimRule().test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PatternRule
  // -------------------------------------------------------------------------

  @Test
  void patternAcceptsMatchingString() {
    assertThat(new PatternRule("^[0-9]+$").test("12345").passed()).isTrue();
  }

  @Test
  void patternRejectsNonMatchingString() {
    assertThat(new PatternRule("^[0-9]+$").test("abc").passed()).isFalse();
  }

  @Test
  void patternRejectsNull() {
    assertThat(new PatternRule(".*").test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // AlphaNumericRule
  // -------------------------------------------------------------------------

  @Test
  void alphaNumericAcceptsLettersAndDigits() {
    assertThat(new AlphaNumericRule().test("abc123").passed()).isTrue();
  }

  @Test
  void alphaNumericRejectsSpecialCharacters() {
    assertThat(new AlphaNumericRule().test("abc!").passed()).isFalse();
  }

  @Test
  void alphaNumericRejectsNull() {
    assertThat(new AlphaNumericRule().test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // IpAddressRule
  // -------------------------------------------------------------------------

  @Test
  void ipv4AcceptsValidIpv4() {
    assertThat(new IpAddressRule(4).test("192.168.1.1").passed()).isTrue();
  }

  @Test
  void ipv4RejectsIpv6() {
    assertThat(new IpAddressRule(4).test("::1").passed()).isFalse();
  }

  @Test
  void ipv6AcceptsValidIpv6() {
    assertThat(new IpAddressRule(6).test("::1").passed()).isTrue();
  }

  @Test
  void ipv6RejectsIpv4() {
    assertThat(new IpAddressRule(6).test("10.0.0.1").passed()).isFalse();
  }

  @Test
  void anyAcceptsBothIpv4AndIpv6() {
    assertThat(new IpAddressRule().test("192.168.1.1").passed()).isTrue();
    assertThat(new IpAddressRule().test("::1").passed()).isTrue();
  }

  // -------------------------------------------------------------------------
  // CreditCardRule
  // -------------------------------------------------------------------------

  @Test
  void creditCardAcceptsNoSeparator() {
    assertThat(new CreditCardRule().test("4111111111111111").passed()).isTrue();
  }

  @Test
  void creditCardAcceptsSpaceSeparator() {
    assertThat(new CreditCardRule().test("4111 1111 1111 1111").passed()).isTrue();
  }

  @Test
  void creditCardAcceptsHyphenSeparator() {
    assertThat(new CreditCardRule().test("4111-1111-1111-1111").passed()).isTrue();
  }

  @Test
  void creditCardRejectsMixedSeparators() {
    assertThat(new CreditCardRule().test("4111-1111 1111 1111").passed()).isFalse();
  }

  @Test
  void creditCardRejectsTooShort() {
    assertThat(new CreditCardRule().test("4111111111111").passed()).isFalse();
  }

  @Test
  void creditCardRejectsNull() {
    assertThat(new CreditCardRule().test(null).passed()).isFalse();
  }

  @Test
  void creditCardRejectsNonString() {
    assertThat(new CreditCardRule().test(4111111111111111L).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // UrlRule
  // -------------------------------------------------------------------------

  @Test
  void urlAcceptsHttp() {
    assertThat(new UrlRule().test("http://example.com").passed()).isTrue();
  }

  @Test
  void urlAcceptsHttps() {
    assertThat(new UrlRule().test("https://example.com/path?q=1").passed()).isTrue();
  }

  @Test
  void urlAcceptsFtp() {
    assertThat(new UrlRule().test("ftp://files.example.com").passed()).isTrue();
  }

  @Test
  void urlRejectsNoProtocol() {
    assertThat(new UrlRule().test("example.com").passed()).isFalse();
  }

  @Test
  void urlRejectsNull() {
    assertThat(new UrlRule().test(null).passed()).isFalse();
  }

  @Test
  void urlRejectsNonString() {
    assertThat(new UrlRule().test(42).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // LengthRule
  // -------------------------------------------------------------------------

  @Test
  void lengthPassesStringWithExactLength() {
    assertThat(new LengthRule(5).test("hello").passed()).isTrue();
  }

  @Test
  void lengthRejectsStringWithWrongLength() {
    assertThat(new LengthRule(5).test("hi").passed()).isFalse();
  }

  @Test
  void lengthPassesNumberEqualToLength() {
    assertThat(new LengthRule(42).test(42).passed()).isTrue();
  }

  @Test
  void lengthRejectsNumberNotEqualToLength() {
    assertThat(new LengthRule(42).test(10).passed()).isFalse();
  }

  @Test
  void lengthPassesCollectionWithExactSize() {
    assertThat(new LengthRule(3).test(List.of("a", "b", "c")).passed()).isTrue();
  }

  @Test
  void lengthRejectsCollectionWithWrongSize() {
    assertThat(new LengthRule(3).test(List.of("a", "b")).passed()).isFalse();
  }

  @Test
  void lengthPassesMapWithExactSize() {
    assertThat(new LengthRule(2).test(Map.of("a", 1, "b", 2)).passed()).isTrue();
  }

  @Test
  void lengthPassesIntArrayWithExactLength() {
    assertThat(new LengthRule(3).test(new int[] {1, 2, 3}).passed()).isTrue();
  }

  @Test
  void lengthRejectsNull() {
    assertThat(new LengthRule(5).test(null).passed()).isFalse();
  }

  @Test
  void lengthRejectsUnsupportedType() {
    assertThat(new LengthRule(1).test(new Object()).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // MacAddressRule
  // -------------------------------------------------------------------------

  @Test
  void macAcceptsColonForm() {
    assertThat(new MacAddressRule().test("AA:BB:CC:DD:EE:FF").passed()).isTrue();
  }

  @Test
  void macAcceptsHyphenForm() {
    assertThat(new MacAddressRule().test("AA-BB-CC-DD-EE-FF").passed()).isTrue();
  }

  @Test
  void macAcceptsLowercase() {
    assertThat(new MacAddressRule().test("aa:bb:cc:dd:ee:ff").passed()).isTrue();
  }

  @Test
  void macRejectsMixedSeparators() {
    assertThat(new MacAddressRule().test("AA:BB-CC:DD:EE:FF").passed()).isFalse();
  }

  @Test
  void macRejectsTooFewOctets() {
    assertThat(new MacAddressRule().test("AA:BB:CC:DD:EE").passed()).isFalse();
  }

  @Test
  void macRejectsNonHexCharacters() {
    assertThat(new MacAddressRule().test("GG:BB:CC:DD:EE:FF").passed()).isFalse();
  }

  @Test
  void macRejectsNull() {
    assertThat(new MacAddressRule().test(null).passed()).isFalse();
  }

  @Test
  void macRejectsNonString() {
    assertThat(new MacAddressRule().test(42).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PhoneRule
  // -------------------------------------------------------------------------

  @Test
  void phoneAcceptsE164() {
    assertThat(new PhoneRule().test("+491511234567").passed()).isTrue();
  }

  @Test
  void phoneAcceptsUsNumber() {
    assertThat(new PhoneRule().test("+12025550100").passed()).isTrue();
  }

  @Test
  void phoneRejectsLocalFormat() {
    assertThat(new PhoneRule().test("01511234567").passed()).isFalse();
  }

  @Test
  void phoneRejectsSpaces() {
    assertThat(new PhoneRule().test("+1 202 555 0100").passed()).isFalse();
  }

  @Test
  void phoneRejectsNull() {
    assertThat(new PhoneRule().test(null).passed()).isFalse();
  }

  @Test
  void phoneRejectsNonString() {
    assertThat(new PhoneRule().test(491511234567L).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // SlugRule
  // -------------------------------------------------------------------------

  @Test
  void slugAcceptsSimpleSlug() {
    assertThat(new SlugRule().test("my-blog-post").passed()).isTrue();
  }

  @Test
  void slugAcceptsDigits() {
    assertThat(new SlugRule().test("product-123").passed()).isTrue();
  }

  @Test
  void slugAcceptsSingleWord() {
    assertThat(new SlugRule().test("v2").passed()).isTrue();
  }

  @Test
  void slugRejectsUppercase() {
    assertThat(new SlugRule().test("My-Post").passed()).isFalse();
  }

  @Test
  void slugRejectsTrailingHyphen() {
    assertThat(new SlugRule().test("trailing-").passed()).isFalse();
  }

  @Test
  void slugRejectsConsecutiveHyphens() {
    assertThat(new SlugRule().test("bad--slug").passed()).isFalse();
  }

  @Test
  void slugRejectsLeadingHyphen() {
    assertThat(new SlugRule().test("-leading").passed()).isFalse();
  }

  @Test
  void slugRejectsNull() {
    assertThat(new SlugRule().test(null).passed()).isFalse();
  }

  @Test
  void slugRejectsNonString() {
    assertThat(new SlugRule().test(42).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // Base64Rule
  // -------------------------------------------------------------------------

  @Test
  void base64AcceptsValidStandard() {
    assertThat(new Base64Rule().test("SGVsbG8gV29ybGQ=").passed()).isTrue();
  }

  @Test
  void base64AcceptsEmptyString() {
    assertThat(new Base64Rule().test("").passed()).isTrue();
  }

  @Test
  void base64RejectsBadPadding() {
    assertThat(new Base64Rule().test("SGVsbG8=====").passed()).isFalse();
  }

  @Test
  void base64RejectsWrongAlphabet() {
    assertThat(new Base64Rule().test("SGVs_G8=").passed()).isFalse();
  }

  @Test
  void base64UrlSafeAcceptsValid() {
    assertThat(new Base64Rule(true).test("SGVsbG8gV29ybGQ").passed()).isTrue();
  }

  @Test
  void base64UrlSafeAcceptsDashUnderscore() {
    assertThat(new Base64Rule(true).test("a-b_c").passed()).isTrue();
  }

  @Test
  void base64UrlSafeRejectsStandardAlphabet() {
    assertThat(new Base64Rule(true).test("SGVs+G8/").passed()).isFalse();
  }

  @Test
  void base64RejectsNull() {
    assertThat(new Base64Rule().test(null).passed()).isFalse();
  }

  @Test
  void base64RejectsNonString() {
    assertThat(new Base64Rule().test(42).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // SemVerRule
  // -------------------------------------------------------------------------

  @Test
  void semverAcceptsBaseVersion() {
    assertThat(new SemVerRule().test("1.0.0").passed()).isTrue();
  }

  @Test
  void semverAcceptsPreRelease() {
    assertThat(new SemVerRule().test("2.3.1-alpha.1").passed()).isTrue();
  }

  @Test
  void semverAcceptsBuildMetadata() {
    assertThat(new SemVerRule().test("1.0.0-rc.1+build.123").passed()).isTrue();
  }

  @Test
  void semverRejectsVPrefix() {
    assertThat(new SemVerRule().test("v1.0.0").passed()).isFalse();
  }

  @Test
  void semverRejectsTwoPart() {
    assertThat(new SemVerRule().test("1.0").passed()).isFalse();
  }

  @Test
  void semverRejectsLeadingZeros() {
    assertThat(new SemVerRule().test("01.0.0").passed()).isFalse();
  }

  @Test
  void semverRejectsNull() {
    assertThat(new SemVerRule().test(null).passed()).isFalse();
  }

  @Test
  void semverRejectsNonString() {
    assertThat(new SemVerRule().test(100).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // IbanRule
  // -------------------------------------------------------------------------

  @Test
  void ibanAcceptsValidGerman() {
    assertThat(new IbanRule().test("DE89370400440532013000").passed()).isTrue();
  }

  @Test
  void ibanAcceptsValidGermanWithSpaces() {
    assertThat(new IbanRule().test("DE89 3704 0044 0532 0130 00").passed()).isTrue();
  }

  @Test
  void ibanAcceptsValidUk() {
    assertThat(new IbanRule().test("GB29NWBK60161331926819").passed()).isTrue();
  }

  @Test
  void ibanRejectsInvalidCheckDigits() {
    assertThat(new IbanRule().test("DE00370400440532013000").passed()).isFalse();
  }

  @Test
  void ibanRejectsWrongCountryCode() {
    assertThat(new IbanRule().test("12370400440532013000").passed()).isFalse();
  }

  @Test
  void ibanRejectsTooShort() {
    assertThat(new IbanRule().test("DE89").passed()).isFalse();
  }

  @Test
  void ibanRejectsNull() {
    assertThat(new IbanRule().test(null).passed()).isFalse();
  }

  @Test
  void ibanRejectsNonString() {
    assertThat(new IbanRule().test(42).passed()).isFalse();
  }
}

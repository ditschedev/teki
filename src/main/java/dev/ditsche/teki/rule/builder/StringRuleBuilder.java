package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.ruleset.*;
import dev.ditsche.teki.validation.ValidationField;
import java.util.LinkedList;

/**
 * Builds validation definitions for the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public final class StringRuleBuilder extends RuleBuilder {

  private final String field;
  private boolean optional = false;

  StringRuleBuilder(String field) {
    this.field = field;
    this.rules = new LinkedList<>();
    this.rules.add(new StringRule());
  }

  /**
   * Adds an exact length constraint.
   *
   * @param length required length
   * @return this builder for chaining
   */
  public StringRuleBuilder length(int length) {
    this.rules.add(new LengthRule(length));
    return this;
  }

  /**
   * Adds an inclusive size range constraint.
   *
   * @param min minimum allowed value
   * @param max maximum allowed value
   * @return this builder for chaining
   */
  public StringRuleBuilder between(int min, int max) {
    this.rules.add(new BetweenRule(min, max));
    return this;
  }

  /**
   * Adds a minimum value or size constraint.
   *
   * @param min minimum allowed value
   * @return this builder for chaining
   */
  public StringRuleBuilder min(int min) {
    this.rules.add(new MinRule(min));
    return this;
  }

  /**
   * Adds a maximum value or size constraint.
   *
   * @param max maximum allowed value
   * @return this builder for chaining
   */
  public StringRuleBuilder max(int max) {
    this.rules.add(new MaxRule(max));
    return this;
  }

  /**
   * Requires the value to be a valid email address.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder email() {
    this.rules.add(new EmailRule());
    return this;
  }

  /**
   * Requires the value to be a valid URL.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder url() {
    this.rules.add(new UrlRule());
    return this;
  }

  /**
   * Requires the value to match a regular expression.
   *
   * @param pattern regular expression pattern
   * @return this builder for chaining
   */
  public StringRuleBuilder pattern(String pattern) {
    this.rules.add(new PatternRule(pattern));
    return this;
  }

  /**
   * Marks the value as required.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder required() {
    this.rules.add(new RequiredRule());
    return this;
  }

  /**
   * Requires the value to contain only letters and numbers.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder alphanum() {
    this.rules.add(new AlphaNumericRule());
    return this;
  }

  /**
   * Requires the value to be a valid IP address.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder ip() {
    this.rules.add(new IpAddressRule());
    return this;
  }

  /**
   * Requires the value to be a valid IPv4 address.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder ipv4() {
    this.rules.add(new IpAddressRule(4));
    return this;
  }

  /**
   * Requires the value to be a valid IPv6 address.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder ipv6() {
    this.rules.add(new IpAddressRule(6));
    return this;
  }

  /**
   * Requires the value to be a valid credit card number.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder creditCard() {
    this.rules.add(new CreditCardRule());
    return this;
  }

  /**
   * Adds a default value transformation for null values.
   *
   * @param value expected or replacement value
   * @return this builder for chaining
   */
  public StringRuleBuilder defaultValue(String value) {
    this.rules.add(new DefaultRule(value));
    return this;
  }

  /**
   * Adds a string trimming transformation.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder trim() {
    this.rules.add(new TrimRule());
    return this;
  }

  /**
   * Requires the value to be a valid URL slug.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder slug() {
    this.rules.add(new SlugRule());
    return this;
  }

  /**
   * Transforms the value into a URL-safe slug (lowercase, diacritics stripped, non-alphanumeric
   * runs replaced with hyphens). An input that produces an empty slug is rejected.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder slugify() {
    this.rules.add(new SlugifyRule());
    return this;
  }

  /**
   * Requires the value to be a valid standard base64-encoded string.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder base64() {
    this.rules.add(new Base64Rule());
    return this;
  }

  /**
   * Requires the value to be a valid base64-encoded string.
   *
   * @param urlSafe when true, validates URL-safe base64 ({@code -_} alphabet, no padding)
   * @return this builder for chaining
   */
  public StringRuleBuilder base64(boolean urlSafe) {
    this.rules.add(new Base64Rule(urlSafe));
    return this;
  }

  /**
   * Requires the value to be a valid semantic version (semver.org).
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder semver() {
    this.rules.add(new SemVerRule());
    return this;
  }

  /**
   * Requires the value to be a valid IBAN.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder iban() {
    this.rules.add(new IbanRule());
    return this;
  }

  /**
   * Requires the value to be a valid MAC address (colon or hyphen separator).
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder mac() {
    this.rules.add(new MacAddressRule());
    return this;
  }

  /**
   * Requires the value to be a valid phone number in E.164 format.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder phone() {
    this.rules.add(new PhoneRule());
    return this;
  }

  /**
   * Requires the value to be a well-formed UUID.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder uuid() {
    this.rules.add(new UuidRule());
    return this;
  }

  /**
   * Requires the value to be a well-formed UUID of the specified version (1–5).
   *
   * @param version required UUID version
   * @return this builder for chaining
   */
  public StringRuleBuilder uuid(int version) {
    this.rules.add(new UuidRule(version));
    return this;
  }

  /**
   * Rejects blank values (null or whitespace-only strings).
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder notBlank() {
    this.rules.add(new NotBlankRule());
    return this;
  }

  /**
   * Requires the value to be one of a fixed set of allowed values.
   *
   * @param allowed allowed values
   * @return this builder for chaining
   */
  public StringRuleBuilder oneOf(String... allowed) {
    this.rules.add(new OneOfRule(allowed));
    return this;
  }

  /**
   * Allows the value to be absent or null.
   *
   * @return this builder for chaining
   */
  public StringRuleBuilder optional() {
    this.optional = true;
    return this;
  }

  @Override
  public RuleBuilder custom(Rule rule) {
    this.rules.add(rule);
    return this;
  }

  @Override
  public ValidationField build() {
    return new ValidationField(this.field, this.rules, this.optional);
  }
}

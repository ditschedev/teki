package dev.ditsche.teki;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Fluent message configuration for Teki validation errors.
 *
 * <p>{@code TekiMessages} replaces writing a full {@link MessageResolver} for the common case of
 * overriding one or more built-in messages. Templates use <code>{field}</code> for the field name
 * and named placeholders for rule-specific params (e.g. <code>{min}</code>, <code>{max}</code>).
 *
 * <pre>{@code
 * // Override a couple of messages globally:
 * Teki.setGlobalMessages(
 *     TekiMessages.defaults()
 *         .override(TekiErrors.BETWEEN,     "Must be between {min} and {max}")
 *         .override(TekiErrors.REQUIRED, "{field} is required")
 * );
 *
 * // Load translations from a properties file on the classpath:
 * Teki.setGlobalMessages(
 *     TekiMessages.defaults()
 *         .fromProperties("i18n/messages_de.properties")
 * );
 *
 * // Per-schema override:
 * Teki schema = Teki.fromRules(...)
 *     .messages(TekiMessages.defaults().override(TekiErrors.EMAIL, "Invalid email"));
 * }</pre>
 *
 * <p>Return values from {@link MessageResolver} take precedence. {@code TekiMessages} sits below a
 * custom resolver in the chain and above the rule's built-in fallback.
 *
 * @author Tobias Dittmann
 */
public final class TekiMessages implements MessageResolver {

  private final Map<String, String> templates;

  private TekiMessages(Map<String, String> templates) {
    this.templates = templates;
  }

  /**
   * Returns a new {@code TekiMessages} pre-loaded with the default English templates for all
   * built-in rules.
   */
  public static TekiMessages defaults() {
    Map<String, String> m = new HashMap<>();

    m.put(TekiErrors.REQUIRED, "The field \"{field}\" is required");
    m.put(TekiErrors.EMAIL, "The field \"{field}\" must be a valid email address");
    m.put(TekiErrors.URL, "The field \"{field}\" must be a valid URL");
    m.put(TekiErrors.IP_ADDRESS, "The field \"{field}\" must be a valid IP address");
    m.put(TekiErrors.CREDIT_CARD, "The field \"{field}\" must be a valid credit card number");
    m.put(TekiErrors.ALPHA_NUMERIC, "The field \"{field}\" must be alphanumeric");
    m.put(TekiErrors.PATTERN, "The field \"{field}\" has an invalid format");
    m.put(TekiErrors.TRIM, "The field \"{field}\" must be a string");
    m.put(TekiErrors.UUID, "The field \"{field}\" must be a valid UUID");
    m.put(
        TekiErrors.PHONE,
        "The field \"{field}\" must be a valid phone number in E.164 format (e.g. +491511234567)");
    m.put(
        TekiErrors.MAC_ADDRESS,
        "The field \"{field}\" must be a valid MAC address (e.g. AA:BB:CC:DD:EE:FF)");
    m.put(
        TekiErrors.SLUG,
        "The field \"{field}\" must be a valid slug (lowercase letters, digits, and hyphens only)");
    m.put(TekiErrors.BASE64, "The field \"{field}\" must be a valid base64-encoded string");
    m.put(
        TekiErrors.SEMVER,
        "The field \"{field}\" must be a valid semantic version (e.g. 1.2.3 or 1.0.0-alpha.1)");
    m.put(TekiErrors.IBAN, "The field \"{field}\" must be a valid IBAN");

    m.put(TekiErrors.STRING, "The field \"{field}\" must be a string");
    m.put(TekiErrors.NUMBER, "The field \"{field}\" must be a number");
    m.put(TekiErrors.BOOLEAN, "The field \"{field}\" must be {expected}");
    m.put(TekiErrors.ARRAY, "The field \"{field}\" must be an array");
    m.put(TekiErrors.TEMPORAL, "The field \"{field}\" must be a valid date/time value");
    m.put(
        TekiErrors.UNASSIGNABLE,
        "The field \"{field}\" cannot be set to a value of type \"{valueType}\"");

    m.put(TekiErrors.NOT_BLANK, "The field \"{field}\" must not be blank");
    m.put(TekiErrors.ONE_OF, "The field \"{field}\" must be one of: {allowed}");

    m.put(TekiErrors.POSITIVE, "The field \"{field}\" must be positive");
    m.put(TekiErrors.POSITIVE_OR_ZERO, "The field \"{field}\" must be positive or zero");
    m.put(TekiErrors.NEGATIVE, "The field \"{field}\" must be negative");
    m.put(TekiErrors.NEGATIVE_OR_ZERO, "The field \"{field}\" must be negative or zero");

    m.put(TekiErrors.MIN, "The field \"{field}\" must be at least {min}");
    m.put(TekiErrors.MAX, "The field \"{field}\" must be at most {max}");
    m.put(TekiErrors.LENGTH, "The field \"{field}\" must have a length of {length}");
    m.put(TekiErrors.BETWEEN, "The field \"{field}\" must be between {min} and {max}");

    m.put(TekiErrors.PAST, "The field \"{field}\" must be a date in the past");
    m.put(
        TekiErrors.PAST_OR_PRESENT, "The field \"{field}\" must be a date in the past or present");
    m.put(TekiErrors.FUTURE, "The field \"{field}\" must be a date in the future");
    m.put(
        TekiErrors.FUTURE_OR_PRESENT,
        "The field \"{field}\" must be a date in the future or present");
    m.put(TekiErrors.BEFORE, "The field \"{field}\" must be before {boundary}");
    m.put(TekiErrors.AFTER, "The field \"{field}\" must be after {boundary}");

    return new TekiMessages(m);
  }

  /** Returns a new empty {@code TekiMessages} with no templates configured. */
  public static TekiMessages empty() {
    return new TekiMessages(new HashMap<>());
  }

  /**
   * Overrides the template for a single error type.
   *
   * <p>Use {@link TekiErrors} constants as the {@code type} argument. Templates may contain <code>
   * {field}</code> and any param placeholders exposed by the rule (e.g. <code>{min}</code>, <code>
   * {max}</code>, <code>{length}</code>).
   *
   * @param type stable error type key (see {@link TekiErrors})
   * @param template message template with optional <code>{placeholder}</code> tokens
   * @return this instance for chaining
   */
  public TekiMessages override(String type, String template) {
    this.templates.put(type, template);
    return this;
  }

  /**
   * Overrides templates for multiple error types at once.
   *
   * @param overrides map of error type key to template string
   * @return this instance for chaining
   */
  public TekiMessages overrideAll(Map<String, String> overrides) {
    this.templates.putAll(overrides);
    return this;
  }

  /**
   * Loads templates from a properties file on the classpath and merges them into this instance.
   * Existing templates are overridden by entries from the file.
   *
   * <p>Each property key must be a stable error type key (see {@link TekiErrors}); the value is the
   * template string.
   *
   * <pre>{@code
   * # messages_de.properties
   * format.required  = Das Feld "{field}" ist erforderlich
   * format.email     = Das Feld "{field}" muss eine gültige E-Mail sein
   * size             = Muss zwischen {min} und {max} liegen
   * }</pre>
   *
   * @param classpathResource path to the properties file on the classpath
   * @return this instance for chaining
   * @throws IllegalArgumentException if the resource cannot be found or read
   */
  public TekiMessages fromProperties(String classpathResource) {
    try (InputStream in =
        TekiMessages.class.getClassLoader().getResourceAsStream(classpathResource)) {
      if (in == null)
        throw new IllegalArgumentException("Classpath resource not found: " + classpathResource);
      return fromProperties(in);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to load messages from: " + classpathResource, e);
    }
  }

  /**
   * Loads templates from a {@link Properties} stream and merges them into this instance.
   *
   * @param in input stream of a properties file
   * @return this instance for chaining
   * @throws IllegalArgumentException if the stream cannot be read
   */
  public TekiMessages fromProperties(InputStream in) {
    Properties props = new Properties();
    try {
      props.load(in);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to load messages from stream", e);
    }
    for (String key : props.stringPropertyNames()) {
      this.templates.put(key, props.getProperty(key));
    }
    return this;
  }

  /**
   * Resolves a message for the given field, type, and rule params by looking up and interpolating
   * the configured template. Returns {@code null} if no template is registered for the type, which
   * causes Teki to fall through to the rule's built-in message.
   */
  @Override
  public String resolve(String field, String type, Map<String, Object> params) {
    String template = templates.get(type);
    if (template == null) return null;
    return interpolate(template, field, params);
  }

  private static String interpolate(String template, String field, Map<String, Object> params) {
    String result = template.replace("{field}", field);
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      result = result.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
    }
    return result;
  }
}

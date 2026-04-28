package dev.ditsche.teki;

/**
 * Stable error type keys for all built-in Teki rules.
 *
 * <p>Use these constants when overriding messages via {@link TekiMessages}:
 *
 * <pre>{@code
 * TekiMessages.defaults()
 *     .override(TekiErrors.BETWEEN, "Must be between {min} and {max}")
 *     .override(TekiErrors.REQUIRED, "{field} is required")
 * }</pre>
 *
 * @author Tobias Dittmann
 */
public final class TekiErrors {

  private TekiErrors() {}

  // --- format ---

  public static final String REQUIRED = "format.required";
  public static final String EMAIL = "format.email";
  public static final String URL = "format.url";
  public static final String IP_ADDRESS = "format.ip";
  public static final String CREDIT_CARD = "format.creditcard";
  public static final String ALPHA_NUMERIC = "format.alphanum";
  public static final String PATTERN = "format.pattern";
  public static final String TRIM = "format.trim";
  public static final String UUID = "format.uuid";

  // --- type ---

  public static final String STRING = "type.string";
  public static final String NUMBER = "type.number";
  public static final String BOOLEAN = "type.boolean";
  public static final String ARRAY = "type.array";
  public static final String TEMPORAL = "type.temporal";
  public static final String UNASSIGNABLE = "type.unassignable";

  // --- string ---

  public static final String NOT_BLANK = "string.not_blank";
  public static final String ONE_OF = "string.one_of";

  // --- number ---

  public static final String POSITIVE = "number.positive";
  public static final String POSITIVE_OR_ZERO = "number.positive_or_zero";
  public static final String NEGATIVE = "number.negative";
  public static final String NEGATIVE_OR_ZERO = "number.negative_or_zero";

  // --- size ---

  public static final String MIN = "size.min";
  public static final String MAX = "size.max";
  public static final String LENGTH = "size.length";
  public static final String BETWEEN = "between";

  // --- temporal ---

  public static final String PAST = "temporal.past";
  public static final String PAST_OR_PRESENT = "temporal.past_or_present";
  public static final String FUTURE = "temporal.future";
  public static final String FUTURE_OR_PRESENT = "temporal.future_or_present";
  public static final String BEFORE = "temporal.before";
  public static final String AFTER = "temporal.after";
}

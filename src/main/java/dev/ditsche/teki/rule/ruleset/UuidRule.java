package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.Map;
import java.util.UUID;

/**
 * Validates that the value is a well-formed UUID, optionally of a specific version.
 *
 * @author Tobias Dittmann
 */
public final class UuidRule implements Rule {

  private final int version;

  /** Accepts any valid UUID. */
  public UuidRule() {
    this.version = 0;
  }

  /**
   * Accepts only UUIDs of the specified version (1–5).
   *
   * @param version required UUID version
   */
  public UuidRule(int version) {
    if (version < 1 || version > 5)
      throw new IllegalArgumentException("UUID version must be between 1 and 5");
    this.version = version;
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    try {
      UUID uuid = UUID.fromString(s);
      if (version != 0 && uuid.version() != version) return RuleResult.reject();
      return RuleResult.resolve();
    } catch (IllegalArgumentException e) {
      return RuleResult.reject();
    }
  }

  @Override
  public Map<String, Object> params() {
    return version != 0 ? Map.of("version", version) : Map.of();
  }

  @Override
  public String getType() {
    return TekiErrors.UUID;
  }
}

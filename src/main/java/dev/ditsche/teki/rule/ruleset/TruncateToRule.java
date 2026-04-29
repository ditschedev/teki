package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.annotation.TruncateTo;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Transforms a temporal value by truncating it to the given {@link ChronoUnit} and resolving the
 * field as an {@link Instant}.
 *
 * <p>Supports units up to {@code DAYS}. Larger units ({@code WEEKS}, {@code MONTHS}, {@code YEARS})
 * are not supported by {@link Instant#truncatedTo} and will cause a validation failure.
 *
 * @author Tobias Dittmann
 */
public final class TruncateToRule implements Rule {

  private final ChronoUnit unit;

  public TruncateToRule(ChronoUnit unit) {
    this.unit = unit;
  }

  public TruncateToRule(TruncateTo annotation) {
    this.unit = annotation.value();
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    Instant instant = TemporalHelper.toInstant(value);
    if (instant == null) return RuleResult.reject();
    try {
      return RuleResult.resolve(instant.truncatedTo(unit));
    } catch (Exception e) {
      return RuleResult.reject();
    }
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("unit", unit.name());
  }

  @Override
  public String getType() {
    return TekiErrors.TRUNCATE_TO;
  }
}

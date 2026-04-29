package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;

/**
 * Transforms a temporal value to its {@link Instant} representation in UTC.
 *
 * <p>Accepts any temporal type supported by Teki ({@code Instant}, {@code ZonedDateTime}, {@code
 * OffsetDateTime}, {@code LocalDateTime}, {@code LocalDate}, {@code Date}, {@code Calendar}) and
 * resolves the field as an {@link Instant}.
 *
 * @author Tobias Dittmann
 */
public final class ToUtcRule implements Rule {

  /** Creates a rule instance. */
  public ToUtcRule() {}

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    Instant instant = TemporalHelper.toInstant(value);
    if (instant == null) return RuleResult.reject();
    return RuleResult.resolve(instant);
  }

  @Override
  public String getType() {
    return TekiErrors.TO_UTC;
  }
}

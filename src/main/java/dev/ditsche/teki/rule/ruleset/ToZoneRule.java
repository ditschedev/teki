package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.annotation.ToZone;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Transforms a temporal value to a {@link ZonedDateTime} in the given {@link ZoneId}.
 *
 * <p>Accepts any temporal type supported by Teki, converts it to an {@link Instant}, and then
 * wraps it as a {@link ZonedDateTime} in the target zone.
 *
 * @author Tobias Dittmann
 */
public final class ToZoneRule implements Rule {

  private final ZoneId zone;

  public ToZoneRule(ZoneId zone) {
    this.zone = zone;
  }

  public ToZoneRule(ToZone annotation) {
    this.zone = ZoneId.of(annotation.value());
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    Instant instant = TemporalHelper.toInstant(value);
    if (instant == null) return RuleResult.reject();
    return RuleResult.resolve(instant.atZone(zone));
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("zone", zone.getId());
  }

  @Override
  public String getType() {
    return TekiErrors.TO_ZONE;
  }
}

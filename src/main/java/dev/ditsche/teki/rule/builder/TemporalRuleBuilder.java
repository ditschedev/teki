package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.ruleset.*;
import dev.ditsche.teki.validation.Validatable;
import dev.ditsche.teki.validation.ValidationField;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

/**
 * Builds validation definitions for temporal (date/time) fields in the Teki fluent rule API.
 *
 * @author Tobias Dittmann
 */
public final class TemporalRuleBuilder extends RuleBuilder {

  private final String field;
  private boolean optional = false;

  TemporalRuleBuilder(String field) {
    this.field = field;
    this.rules = new LinkedList<>();
    this.rules.add(new TemporalRule());
  }

  /**
   * Requires the value to be in the past.
   *
   * @return this builder for chaining
   */
  public TemporalRuleBuilder past() {
    this.rules.add(new PastRule());
    return this;
  }

  /**
   * Requires the value to be in the future.
   *
   * @return this builder for chaining
   */
  public TemporalRuleBuilder future() {
    this.rules.add(new FutureRule());
    return this;
  }

  /**
   * Requires the value to be in the past or present.
   *
   * @return this builder for chaining
   */
  public TemporalRuleBuilder pastOrPresent() {
    this.rules.add(new PastOrPresentRule());
    return this;
  }

  /**
   * Requires the value to be in the future or present.
   *
   * @return this builder for chaining
   */
  public TemporalRuleBuilder futureOrPresent() {
    this.rules.add(new FutureOrPresentRule());
    return this;
  }

  /**
   * Requires the value to be strictly before the given instant.
   *
   * @param boundary exclusive upper bound
   * @return this builder for chaining
   */
  public TemporalRuleBuilder before(Instant boundary) {
    this.rules.add(new BeforeRule(boundary));
    return this;
  }

  /**
   * Requires the value to be strictly after the given instant.
   *
   * @param boundary exclusive lower bound
   * @return this builder for chaining
   */
  public TemporalRuleBuilder after(Instant boundary) {
    this.rules.add(new AfterRule(boundary));
    return this;
  }

  /**
   * Truncates the temporal value to the given {@link ChronoUnit} and resolves the field as an
   * {@link java.time.Instant}. Supports units up to {@code DAYS}.
   *
   * @param unit granularity to truncate to
   * @return this builder for chaining
   */
  public TemporalRuleBuilder truncateTo(ChronoUnit unit) {
    this.rules.add(new TruncateToRule(unit));
    return this;
  }

  /**
   * Normalizes the temporal value to its {@link java.time.Instant} representation in UTC.
   *
   * @return this builder for chaining
   */
  public TemporalRuleBuilder toUtc() {
    this.rules.add(new ToUtcRule());
    return this;
  }

  /**
   * Converts the temporal value to a {@link java.time.ZonedDateTime} in the given timezone.
   *
   * @param zone target timezone
   * @return this builder for chaining
   */
  public TemporalRuleBuilder toZone(ZoneId zone) {
    this.rules.add(new ToZoneRule(zone));
    return this;
  }

  /**
   * Marks the field as required (non-null).
   *
   * @return this builder for chaining
   */
  public TemporalRuleBuilder required() {
    this.rules.add(new RequiredRule());
    return this;
  }

  /**
   * Allows the value to be absent or null.
   *
   * @return this builder for chaining
   */
  public TemporalRuleBuilder optional() {
    this.optional = true;
    return this;
  }

  @Override
  public RuleBuilder custom(Rule rule) {
    this.rules.add(rule);
    return this;
  }

  @Override
  public Validatable build() {
    return new ValidationField(this.field, this.rules, this.optional);
  }
}

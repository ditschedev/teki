package dev.ditsche.teki.rule.ruleset;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;

class TemporalRuleTest {

  private static final Instant PAST = Instant.now().minusSeconds(3600);
  private static final Instant FUTURE = Instant.now().plusSeconds(3600);
  private static final Instant BOUNDARY = Instant.parse("2020-01-01T00:00:00Z");
  private static final Instant BEFORE_BOUNDARY = Instant.parse("2019-06-01T00:00:00Z");
  private static final Instant AFTER_BOUNDARY = Instant.parse("2021-06-01T00:00:00Z");

  // -------------------------------------------------------------------------
  // TemporalRule
  // -------------------------------------------------------------------------

  @Test
  void temporalAcceptsInstant() {
    assertThat(new TemporalRule().test(Instant.now()).passed()).isTrue();
  }

  @Test
  void temporalAcceptsLocalDate() {
    assertThat(new TemporalRule().test(LocalDate.now()).passed()).isTrue();
  }

  @Test
  void temporalAcceptsLocalDateTime() {
    assertThat(new TemporalRule().test(LocalDateTime.now()).passed()).isTrue();
  }

  @Test
  void temporalAcceptsZonedDateTime() {
    assertThat(new TemporalRule().test(ZonedDateTime.now()).passed()).isTrue();
  }

  @Test
  void temporalAcceptsOffsetDateTime() {
    assertThat(new TemporalRule().test(OffsetDateTime.now(ZoneOffset.UTC)).passed()).isTrue();
  }

  @Test
  void temporalAcceptsDate() {
    assertThat(new TemporalRule().test(new Date()).passed()).isTrue();
  }

  @Test
  void temporalAcceptsCalendar() {
    assertThat(new TemporalRule().test(Calendar.getInstance()).passed()).isTrue();
  }

  @Test
  void temporalRejectsNull() {
    assertThat(new TemporalRule().test(null).passed()).isFalse();
  }

  @Test
  void temporalRejectsString() {
    assertThat(new TemporalRule().test("2024-01-01").passed()).isFalse();
  }

  @Test
  void temporalRejectsInteger() {
    assertThat(new TemporalRule().test(12345).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PastRule
  // -------------------------------------------------------------------------

  @Test
  void pastAcceptsPastInstant() {
    assertThat(new PastRule().test(PAST).passed()).isTrue();
  }

  @Test
  void pastRejectsFutureInstant() {
    assertThat(new PastRule().test(FUTURE).passed()).isFalse();
  }

  @Test
  void pastAcceptsPastLocalDate() {
    assertThat(new PastRule().test(LocalDate.now().minusDays(1)).passed()).isTrue();
  }

  @Test
  void pastRejectsFutureLocalDate() {
    assertThat(new PastRule().test(LocalDate.now().plusDays(1)).passed()).isFalse();
  }

  @Test
  void pastAcceptsPastLocalDateTime() {
    assertThat(new PastRule().test(LocalDateTime.now().minusHours(1)).passed()).isTrue();
  }

  @Test
  void pastAcceptsPastZonedDateTime() {
    assertThat(new PastRule().test(ZonedDateTime.now().minusHours(1)).passed()).isTrue();
  }

  @Test
  void pastAcceptsPastDate() {
    assertThat(new PastRule().test(new Date(System.currentTimeMillis() - 10_000)).passed())
        .isTrue();
  }

  @Test
  void pastRejectsNull() {
    assertThat(new PastRule().test(null).passed()).isFalse();
  }

  @Test
  void pastRejectsNonTemporalType() {
    assertThat(new PastRule().test("yesterday").passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // FutureRule
  // -------------------------------------------------------------------------

  @Test
  void futureAcceptsFutureInstant() {
    assertThat(new FutureRule().test(FUTURE).passed()).isTrue();
  }

  @Test
  void futureRejectsPastInstant() {
    assertThat(new FutureRule().test(PAST).passed()).isFalse();
  }

  @Test
  void futureAcceptsFutureLocalDate() {
    assertThat(new FutureRule().test(LocalDate.now().plusDays(1)).passed()).isTrue();
  }

  @Test
  void futureRejectsPastLocalDate() {
    assertThat(new FutureRule().test(LocalDate.now().minusDays(1)).passed()).isFalse();
  }

  @Test
  void futureRejectsNull() {
    assertThat(new FutureRule().test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PastOrPresentRule
  // -------------------------------------------------------------------------

  @Test
  void pastOrPresentAcceptsPastInstant() {
    assertThat(new PastOrPresentRule().test(PAST).passed()).isTrue();
  }

  @Test
  void pastOrPresentRejectsFutureInstant() {
    assertThat(new PastOrPresentRule().test(FUTURE).passed()).isFalse();
  }

  @Test
  void pastOrPresentRejectsNull() {
    assertThat(new PastOrPresentRule().test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // FutureOrPresentRule
  // -------------------------------------------------------------------------

  @Test
  void futureOrPresentAcceptsFutureInstant() {
    assertThat(new FutureOrPresentRule().test(FUTURE).passed()).isTrue();
  }

  @Test
  void futureOrPresentRejectsPastInstant() {
    assertThat(new FutureOrPresentRule().test(PAST).passed()).isFalse();
  }

  @Test
  void futureOrPresentRejectsNull() {
    assertThat(new FutureOrPresentRule().test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // BeforeRule
  // -------------------------------------------------------------------------

  @Test
  void beforeAcceptsInstantBeforeBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(BEFORE_BOUNDARY).passed()).isTrue();
  }

  @Test
  void beforeRejectsInstantAfterBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(AFTER_BOUNDARY).passed()).isFalse();
  }

  @Test
  void beforeRejectsInstantEqualToBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(BOUNDARY).passed()).isFalse();
  }

  @Test
  void beforeAcceptsLocalDateBeforeBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(LocalDate.of(2019, 1, 1)).passed()).isTrue();
  }

  @Test
  void beforeRejectsNull() {
    assertThat(new BeforeRule(BOUNDARY).test(null).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // AfterRule
  // -------------------------------------------------------------------------

  @Test
  void afterAcceptsInstantAfterBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(AFTER_BOUNDARY).passed()).isTrue();
  }

  @Test
  void afterRejectsInstantBeforeBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(BEFORE_BOUNDARY).passed()).isFalse();
  }

  @Test
  void afterRejectsInstantEqualToBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(BOUNDARY).passed()).isFalse();
  }

  @Test
  void afterAcceptsLocalDateAfterBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(LocalDate.of(2021, 1, 1)).passed()).isTrue();
  }

  @Test
  void afterRejectsNull() {
    assertThat(new AfterRule(BOUNDARY).test(null).passed()).isFalse();
  }
}

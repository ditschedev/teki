package dev.ditsche.teki.rule.ruleset;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import org.junit.jupiter.api.Test;

class TemporalRuleTest {

  private static final Instant PAST = Instant.now().minusSeconds(3600);
  private static final Instant FUTURE = Instant.now().plusSeconds(3600);
  private static final Instant BOUNDARY = Instant.parse("2020-01-01T00:00:00Z");
  private static final Instant BEFORE_BOUNDARY = Instant.parse("2019-06-01T00:00:00Z");
  private static final Instant AFTER_BOUNDARY = Instant.parse("2021-06-01T00:00:00Z");

  // -------------------------------------------------------------------------
  // PastRule
  // -------------------------------------------------------------------------

  @Test
  void pastAcceptsPastInstant() {
    assertThat(new PastRule().test(PAST).isPassed()).isTrue();
  }

  @Test
  void pastRejectsFutureInstant() {
    assertThat(new PastRule().test(FUTURE).isPassed()).isFalse();
  }

  @Test
  void pastAcceptsPastLocalDate() {
    assertThat(new PastRule().test(LocalDate.now().minusDays(1)).isPassed()).isTrue();
  }

  @Test
  void pastRejectsFutureLocalDate() {
    assertThat(new PastRule().test(LocalDate.now().plusDays(1)).isPassed()).isFalse();
  }

  @Test
  void pastAcceptsPastLocalDateTime() {
    assertThat(new PastRule().test(LocalDateTime.now().minusHours(1)).isPassed()).isTrue();
  }

  @Test
  void pastAcceptsPastZonedDateTime() {
    assertThat(new PastRule().test(ZonedDateTime.now().minusHours(1)).isPassed()).isTrue();
  }

  @Test
  void pastAcceptsPastDate() {
    assertThat(new PastRule().test(new Date(System.currentTimeMillis() - 10_000)).isPassed()).isTrue();
  }

  @Test
  void pastRejectsNull() {
    assertThat(new PastRule().test(null).isPassed()).isFalse();
  }

  @Test
  void pastRejectsNonTemporalType() {
    assertThat(new PastRule().test("yesterday").isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // FutureRule
  // -------------------------------------------------------------------------

  @Test
  void futureAcceptsFutureInstant() {
    assertThat(new FutureRule().test(FUTURE).isPassed()).isTrue();
  }

  @Test
  void futureRejectsPastInstant() {
    assertThat(new FutureRule().test(PAST).isPassed()).isFalse();
  }

  @Test
  void futureAcceptsFutureLocalDate() {
    assertThat(new FutureRule().test(LocalDate.now().plusDays(1)).isPassed()).isTrue();
  }

  @Test
  void futureRejectsPastLocalDate() {
    assertThat(new FutureRule().test(LocalDate.now().minusDays(1)).isPassed()).isFalse();
  }

  @Test
  void futureRejectsNull() {
    assertThat(new FutureRule().test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // PastOrPresentRule
  // -------------------------------------------------------------------------

  @Test
  void pastOrPresentAcceptsPastInstant() {
    assertThat(new PastOrPresentRule().test(PAST).isPassed()).isTrue();
  }

  @Test
  void pastOrPresentRejectsFutureInstant() {
    assertThat(new PastOrPresentRule().test(FUTURE).isPassed()).isFalse();
  }

  @Test
  void pastOrPresentRejectsNull() {
    assertThat(new PastOrPresentRule().test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // FutureOrPresentRule
  // -------------------------------------------------------------------------

  @Test
  void futureOrPresentAcceptsFutureInstant() {
    assertThat(new FutureOrPresentRule().test(FUTURE).isPassed()).isTrue();
  }

  @Test
  void futureOrPresentRejectsPastInstant() {
    assertThat(new FutureOrPresentRule().test(PAST).isPassed()).isFalse();
  }

  @Test
  void futureOrPresentRejectsNull() {
    assertThat(new FutureOrPresentRule().test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // BeforeRule
  // -------------------------------------------------------------------------

  @Test
  void beforeAcceptsInstantBeforeBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(BEFORE_BOUNDARY).isPassed()).isTrue();
  }

  @Test
  void beforeRejectsInstantAfterBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(AFTER_BOUNDARY).isPassed()).isFalse();
  }

  @Test
  void beforeRejectsInstantEqualToBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(BOUNDARY).isPassed()).isFalse();
  }

  @Test
  void beforeAcceptsLocalDateBeforeBoundary() {
    assertThat(new BeforeRule(BOUNDARY).test(LocalDate.of(2019, 1, 1)).isPassed()).isTrue();
  }

  @Test
  void beforeRejectsNull() {
    assertThat(new BeforeRule(BOUNDARY).test(null).isPassed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // AfterRule
  // -------------------------------------------------------------------------

  @Test
  void afterAcceptsInstantAfterBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(AFTER_BOUNDARY).isPassed()).isTrue();
  }

  @Test
  void afterRejectsInstantBeforeBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(BEFORE_BOUNDARY).isPassed()).isFalse();
  }

  @Test
  void afterRejectsInstantEqualToBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(BOUNDARY).isPassed()).isFalse();
  }

  @Test
  void afterAcceptsLocalDateAfterBoundary() {
    assertThat(new AfterRule(BOUNDARY).test(LocalDate.of(2021, 1, 1)).isPassed()).isTrue();
  }

  @Test
  void afterRejectsNull() {
    assertThat(new AfterRule(BOUNDARY).test(null).isPassed()).isFalse();
  }
}

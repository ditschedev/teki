package dev.ditsche.teki.rule.ruleset;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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

  // -------------------------------------------------------------------------
  // TruncateToRule
  // -------------------------------------------------------------------------

  @Test
  void truncateToTruncatesInstantToDay() {
    Instant input = Instant.parse("2024-06-15T14:32:55Z");
    var result = new TruncateToRule(ChronoUnit.DAYS).test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.changed()).isTrue();
    assertThat(result.value()).isEqualTo(Instant.parse("2024-06-15T00:00:00Z"));
  }

  @Test
  void truncateToTruncatesInstantToHour() {
    Instant input = Instant.parse("2024-06-15T14:32:55Z");
    var result = new TruncateToRule(ChronoUnit.HOURS).test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.value()).isEqualTo(Instant.parse("2024-06-15T14:00:00Z"));
  }

  @Test
  void truncateToTruncatesInstantToMinutes() {
    Instant input = Instant.parse("2024-06-15T14:32:55Z");
    var result = new TruncateToRule(ChronoUnit.MINUTES).test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.value()).isEqualTo(Instant.parse("2024-06-15T14:32:00Z"));
  }

  @Test
  void truncateToAcceptsLocalDateTime() {
    var result = new TruncateToRule(ChronoUnit.DAYS).test(LocalDateTime.of(2024, 6, 15, 14, 32));
    assertThat(result.passed()).isTrue();
    assertThat(result.changed()).isTrue();
  }

  @Test
  void truncateToAcceptsZonedDateTime() {
    var input = ZonedDateTime.of(2024, 6, 15, 14, 32, 0, 0, ZoneOffset.UTC);
    var result = new TruncateToRule(ChronoUnit.HOURS).test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.value()).isEqualTo(Instant.parse("2024-06-15T14:00:00Z"));
  }

  @Test
  void truncateToRejectsNull() {
    assertThat(new TruncateToRule(ChronoUnit.DAYS).test(null).passed()).isFalse();
  }

  @Test
  void truncateToRejectsNonTemporalType() {
    assertThat(new TruncateToRule(ChronoUnit.DAYS).test("2024-01-01").passed()).isFalse();
  }

  @Test
  void truncateToRejectsUnsupportedUnit() {
    assertThat(new TruncateToRule(ChronoUnit.MONTHS).test(Instant.now()).passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // ToUtcRule
  // -------------------------------------------------------------------------

  @Test
  void toUtcConvertsZonedDateTimeToInstant() {
    ZonedDateTime input = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneOffset.ofHours(2));
    var result = new ToUtcRule().test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.changed()).isTrue();
    assertThat(result.value()).isEqualTo(Instant.parse("2024-06-15T08:00:00Z"));
  }

  @Test
  void toUtcConvertsLocalDateTimeToInstant() {
    var result = new ToUtcRule().test(LocalDateTime.of(2024, 6, 15, 0, 0));
    assertThat(result.passed()).isTrue();
    assertThat(result.value()).isInstanceOf(Instant.class);
  }

  @Test
  void toUtcPassesThroughInstant() {
    Instant input = Instant.parse("2024-06-15T08:00:00Z");
    var result = new ToUtcRule().test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.value()).isEqualTo(input);
  }

  @Test
  void toUtcConvertsLegacyDate() {
    Date input = new Date(0);
    var result = new ToUtcRule().test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.value()).isEqualTo(Instant.EPOCH);
  }

  @Test
  void toUtcRejectsNull() {
    assertThat(new ToUtcRule().test(null).passed()).isFalse();
  }

  @Test
  void toUtcRejectsNonTemporalType() {
    assertThat(new ToUtcRule().test("2024-01-01").passed()).isFalse();
  }

  // -------------------------------------------------------------------------
  // ToZoneRule
  // -------------------------------------------------------------------------

  private static final ZoneId BERLIN = ZoneId.of("Europe/Berlin");
  private static final ZoneId NEW_YORK = ZoneId.of("America/New_York");

  @Test
  void toZoneConvertsInstantToZonedDateTime() {
    Instant input = Instant.parse("2024-06-15T12:00:00Z");
    var result = new ToZoneRule(BERLIN).test(input);
    assertThat(result.passed()).isTrue();
    assertThat(result.changed()).isTrue();
    assertThat(result.value()).isInstanceOf(ZonedDateTime.class);
    ZonedDateTime zdt = (ZonedDateTime) result.value();
    assertThat(zdt.getZone()).isEqualTo(BERLIN);
    assertThat(zdt.toInstant()).isEqualTo(input);
  }

  @Test
  void toZonePreservesPointInTime() {
    Instant input = Instant.parse("2024-01-15T06:00:00Z");
    var berlinResult = new ToZoneRule(BERLIN).test(input);
    var nyResult = new ToZoneRule(NEW_YORK).test(input);
    ZonedDateTime berlin = (ZonedDateTime) berlinResult.value();
    ZonedDateTime newYork = (ZonedDateTime) nyResult.value();
    assertThat(berlin.toInstant()).isEqualTo(newYork.toInstant());
    assertThat(berlin.getZone()).isEqualTo(BERLIN);
    assertThat(newYork.getZone()).isEqualTo(NEW_YORK);
  }

  @Test
  void toZoneAcceptsZonedDateTime() {
    ZonedDateTime input = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneOffset.UTC);
    var result = new ToZoneRule(BERLIN).test(input);
    assertThat(result.passed()).isTrue();
    ZonedDateTime zdt = (ZonedDateTime) result.value();
    assertThat(zdt.getZone()).isEqualTo(BERLIN);
  }

  @Test
  void toZoneAcceptsLocalDateTime() {
    var result = new ToZoneRule(BERLIN).test(LocalDateTime.of(2024, 6, 15, 12, 0));
    assertThat(result.passed()).isTrue();
    assertThat(result.value()).isInstanceOf(ZonedDateTime.class);
  }

  @Test
  void toZoneAcceptsLegacyDate() {
    var result = new ToZoneRule(NEW_YORK).test(new Date(0));
    assertThat(result.passed()).isTrue();
    ZonedDateTime zdt = (ZonedDateTime) result.value();
    assertThat(zdt.toInstant()).isEqualTo(Instant.EPOCH);
    assertThat(zdt.getZone()).isEqualTo(NEW_YORK);
  }

  @Test
  void toZoneRejectsNull() {
    assertThat(new ToZoneRule(BERLIN).test(null).passed()).isFalse();
  }

  @Test
  void toZoneRejectsNonTemporalType() {
    assertThat(new ToZoneRule(BERLIN).test("2024-01-01").passed()).isFalse();
  }
}

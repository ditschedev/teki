package dev.ditsche.teki.rule.builder;

import static dev.ditsche.teki.rule.builder.Rules.temporal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class TemporalRuleBuilderTest {

  static class Event {
    Instant scheduledAt;

    Event(Instant scheduledAt) {
      this.scheduledAt = scheduledAt;
    }
  }

  static class ZonedEvent {
    ZonedDateTime scheduledAt;

    ZonedEvent(ZonedDateTime scheduledAt) {
      this.scheduledAt = scheduledAt;
    }
  }

  private static final Instant PAST = Instant.now().minusSeconds(3600);
  private static final Instant FUTURE = Instant.now().plusSeconds(3600);
  private static final Instant BOUNDARY = Instant.parse("2020-01-01T00:00:00Z");

  // -------------------------------------------------------------------------
  // required / optional
  // -------------------------------------------------------------------------

  @Test
  void requiredRejectsNull() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").required());
    assertThatThrownBy(() -> teki.validate(new Event(null)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void optionalSkipsNull() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").optional());
    assertThat(teki.validate(new Event(null))).isNotNull();
  }

  @Test
  void validInstantPasses() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").required());
    assertThat(teki.validate(new Event(PAST))).isNotNull();
  }

  // -------------------------------------------------------------------------
  // past / future / pastOrPresent / futureOrPresent
  // -------------------------------------------------------------------------

  @Test
  void pastAcceptsPastInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").past());
    assertThat(teki.validate(new Event(PAST))).isNotNull();
  }

  @Test
  void pastRejectsFutureInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").past());
    assertThatThrownBy(() -> teki.validate(new Event(FUTURE)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void futureAcceptsFutureInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").future());
    assertThat(teki.validate(new Event(FUTURE))).isNotNull();
  }

  @Test
  void futureRejectsPastInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").future());
    assertThatThrownBy(() -> teki.validate(new Event(PAST)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void pastOrPresentAcceptsPastInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").pastOrPresent());
    assertThat(teki.validate(new Event(PAST))).isNotNull();
  }

  @Test
  void pastOrPresentRejectsFutureInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").pastOrPresent());
    assertThatThrownBy(() -> teki.validate(new Event(FUTURE)))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void futureOrPresentAcceptsFutureInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").futureOrPresent());
    assertThat(teki.validate(new Event(FUTURE))).isNotNull();
  }

  @Test
  void futureOrPresentRejectsPastInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").futureOrPresent());
    assertThatThrownBy(() -> teki.validate(new Event(PAST)))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // before / after
  // -------------------------------------------------------------------------

  @Test
  void beforeAcceptsInstantBeforeBoundary() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").before(BOUNDARY));
    assertThat(teki.validate(new Event(Instant.parse("2019-01-01T00:00:00Z")))).isNotNull();
  }

  @Test
  void beforeRejectsInstantAfterBoundary() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").before(BOUNDARY));
    assertThatThrownBy(() -> teki.validate(new Event(Instant.parse("2021-01-01T00:00:00Z"))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void afterAcceptsInstantAfterBoundary() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").after(BOUNDARY));
    assertThat(teki.validate(new Event(Instant.parse("2021-01-01T00:00:00Z")))).isNotNull();
  }

  @Test
  void afterRejectsInstantBeforeBoundary() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").after(BOUNDARY));
    assertThatThrownBy(() -> teki.validate(new Event(Instant.parse("2019-01-01T00:00:00Z"))))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // truncateTo
  // -------------------------------------------------------------------------

  @Test
  void truncateToWritesTruncatedInstantBackToField() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").truncateTo(ChronoUnit.DAYS));
    Event event = new Event(Instant.parse("2024-06-15T14:32:55Z"));
    teki.validate(event);
    assertThat(event.scheduledAt).isEqualTo(Instant.parse("2024-06-15T00:00:00Z"));
  }

  @Test
  void truncateToHoursWritesCorrectInstant() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").truncateTo(ChronoUnit.HOURS));
    Event event = new Event(Instant.parse("2024-06-15T14:32:55Z"));
    teki.validate(event);
    assertThat(event.scheduledAt).isEqualTo(Instant.parse("2024-06-15T14:00:00Z"));
  }

  // -------------------------------------------------------------------------
  // toUtc
  // -------------------------------------------------------------------------

  @Test
  void toUtcWritesInstantBackToField() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").toUtc());
    Instant input = Instant.parse("2024-06-15T12:00:00Z");
    Event event = new Event(input);
    teki.validate(event);
    assertThat(event.scheduledAt).isEqualTo(input);
  }

  // -------------------------------------------------------------------------
  // toZone
  // -------------------------------------------------------------------------

  @Test
  void toZoneWritesZonedDateTimeBackToField() {
    Teki teki = Teki.fromRules(temporal("scheduledAt").toZone(ZoneId.of("Europe/Berlin")));
    ZonedEvent event = new ZonedEvent(Instant.parse("2024-06-15T12:00:00Z").atZone(ZoneId.of("UTC")));
    teki.validate(event);
    assertThat(event.scheduledAt).isInstanceOf(ZonedDateTime.class);
    assertThat(event.scheduledAt.getZone()).isEqualTo(ZoneId.of("Europe/Berlin"));
    assertThat(event.scheduledAt.toInstant()).isEqualTo(Instant.parse("2024-06-15T12:00:00Z"));
  }
}

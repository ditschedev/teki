package dev.ditsche.teki.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class TemporalAnnotationTest {

  static class PastForm {
    @Past LocalDate birthDate;

    PastForm(LocalDate birthDate) {
      this.birthDate = birthDate;
    }
  }

  static class FutureForm {
    @Future Instant scheduledAt;

    FutureForm(Instant scheduledAt) {
      this.scheduledAt = scheduledAt;
    }
  }

  static class PastOrPresentForm {
    @PastOrPresent Instant verifiedAt;

    PastOrPresentForm(Instant verifiedAt) {
      this.verifiedAt = verifiedAt;
    }
  }

  static class FutureOrPresentForm {
    @FutureOrPresent Instant startsAt;

    FutureOrPresentForm(Instant startsAt) {
      this.startsAt = startsAt;
    }
  }

  static class BeforeForm {
    @Before("2020-01-01")
    LocalDate deadline;

    BeforeForm(LocalDate deadline) {
      this.deadline = deadline;
    }
  }

  static class AfterForm {
    @After("2020-01-01")
    LocalDate publishedAt;

    AfterForm(LocalDate publishedAt) {
      this.publishedAt = publishedAt;
    }
  }

  @Test
  void pastAcceptsPastDate() {
    assertThat(
            Teki.from(PastForm.class).check(new PastForm(LocalDate.now().minusDays(1))).isValid())
        .isTrue();
  }

  @Test
  void pastRejectsFutureDate() {
    assertThatThrownBy(
            () -> Teki.from(PastForm.class).validate(new PastForm(LocalDate.now().plusDays(1))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void pastAllowsNullWhenFieldIsOptional() {
    assertThat(Teki.from(PastForm.class).check(new PastForm(null)).isValid()).isTrue();
  }

  @Test
  void futureAcceptsFutureInstant() {
    assertThat(
            Teki.from(FutureForm.class)
                .check(new FutureForm(Instant.now().plusSeconds(3600)))
                .isValid())
        .isTrue();
  }

  @Test
  void futureRejectsPastInstant() {
    assertThatThrownBy(
            () ->
                Teki.from(FutureForm.class)
                    .validate(new FutureForm(Instant.now().minusSeconds(3600))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void pastOrPresentAcceptsPastInstant() {
    assertThat(
            Teki.from(PastOrPresentForm.class)
                .check(new PastOrPresentForm(Instant.now().minusSeconds(1)))
                .isValid())
        .isTrue();
  }

  @Test
  void pastOrPresentRejectsFutureInstant() {
    assertThatThrownBy(
            () ->
                Teki.from(PastOrPresentForm.class)
                    .validate(new PastOrPresentForm(Instant.now().plusSeconds(3600))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void futureOrPresentAcceptsFutureInstant() {
    assertThat(
            Teki.from(FutureOrPresentForm.class)
                .check(new FutureOrPresentForm(Instant.now().plusSeconds(3600)))
                .isValid())
        .isTrue();
  }

  @Test
  void futureOrPresentRejectsPastInstant() {
    assertThatThrownBy(
            () ->
                Teki.from(FutureOrPresentForm.class)
                    .validate(new FutureOrPresentForm(Instant.now().minusSeconds(3600))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void beforeAcceptsDateBeforeBoundary() {
    assertThat(
            Teki.from(BeforeForm.class).check(new BeforeForm(LocalDate.of(2019, 6, 1))).isValid())
        .isTrue();
  }

  @Test
  void beforeRejectsDateAfterBoundary() {
    assertThatThrownBy(
            () -> Teki.from(BeforeForm.class).validate(new BeforeForm(LocalDate.of(2021, 1, 1))))
        .isInstanceOf(ValidationException.class);
  }

  @Test
  void afterAcceptsDateAfterBoundary() {
    assertThat(Teki.from(AfterForm.class).check(new AfterForm(LocalDate.of(2021, 6, 1))).isValid())
        .isTrue();
  }

  @Test
  void afterRejectsDateBeforeBoundary() {
    assertThatThrownBy(
            () -> Teki.from(AfterForm.class).validate(new AfterForm(LocalDate.of(2019, 1, 1))))
        .isInstanceOf(ValidationException.class);
  }

  // -------------------------------------------------------------------------
  // @TruncateTo
  // -------------------------------------------------------------------------

  static class TruncateToForm {
    @TruncateTo(ChronoUnit.DAYS)
    Instant scheduledAt;

    TruncateToForm(Instant scheduledAt) {
      this.scheduledAt = scheduledAt;
    }
  }

  @Test
  void truncateToAnnotationTruncatesInstant() {
    TruncateToForm form = new TruncateToForm(Instant.parse("2024-06-15T14:32:55Z"));
    Teki.from(TruncateToForm.class).validate(form);
    assertThat(form.scheduledAt).isEqualTo(Instant.parse("2024-06-15T00:00:00Z"));
  }

  @Test
  void truncateToAnnotationAllowsNullWhenOptional() {
    assertThat(Teki.from(TruncateToForm.class).check(new TruncateToForm(null)).isValid()).isTrue();
  }

  // -------------------------------------------------------------------------
  // @ToUtc
  // -------------------------------------------------------------------------

  static class ToUtcForm {
    @ToUtc Instant publishedAt;

    ToUtcForm(Instant publishedAt) {
      this.publishedAt = publishedAt;
    }
  }

  @Test
  void toUtcAnnotationPreservesInstant() {
    Instant input = Instant.parse("2024-06-15T12:00:00Z");
    ToUtcForm form = new ToUtcForm(input);
    Teki.from(ToUtcForm.class).validate(form);
    assertThat(form.publishedAt).isEqualTo(input);
  }

  @Test
  void toUtcAnnotationAllowsNullWhenOptional() {
    assertThat(Teki.from(ToUtcForm.class).check(new ToUtcForm(null)).isValid()).isTrue();
  }

  // -------------------------------------------------------------------------
  // @ToZone
  // -------------------------------------------------------------------------

  static class ToZoneForm {
    @ToZone("Europe/Berlin")
    ZonedDateTime meetingAt;

    ToZoneForm(ZonedDateTime meetingAt) {
      this.meetingAt = meetingAt;
    }
  }

  @Test
  void toZoneAnnotationConvertsToTargetZone() {
    ZonedDateTime input = Instant.parse("2024-06-15T12:00:00Z").atZone(ZoneId.of("UTC"));
    ToZoneForm form = new ToZoneForm(input);
    Teki.from(ToZoneForm.class).validate(form);
    assertThat(form.meetingAt.getZone()).isEqualTo(ZoneId.of("Europe/Berlin"));
    assertThat(form.meetingAt.toInstant()).isEqualTo(Instant.parse("2024-06-15T12:00:00Z"));
  }

  @Test
  void toZoneAnnotationAllowsNullWhenOptional() {
    assertThat(Teki.from(ToZoneForm.class).check(new ToZoneForm(null)).isValid()).isTrue();
  }
}

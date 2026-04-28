package dev.ditsche.teki.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.Teki;
import dev.ditsche.teki.error.ValidationException;
import java.time.Instant;
import java.time.LocalDate;
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
}

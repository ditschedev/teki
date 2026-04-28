package dev.ditsche.teki.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.ditsche.teki.validation.FieldAccess;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class FieldNotAccessibleExceptionTest {

  @Test
  void isRuntimeException() {
    assertThat(new FieldNotAccessibleException()).isInstanceOf(RuntimeException.class);
  }

  @Test
  void canBeConstructedWithoutArguments() {
    assertThat(new FieldNotAccessibleException()).isNotNull();
  }

  static class Immutable {
    final String value = "locked";
  }

  @Test
  void fieldAccessWriteThrowsOnFinalField() throws NoSuchFieldException {
    Field field = Immutable.class.getDeclaredField("value");
    assertThatThrownBy(() -> FieldAccess.write(new Immutable(), field, "changed"))
        .isInstanceOf(FieldNotAccessibleException.class);
  }
}

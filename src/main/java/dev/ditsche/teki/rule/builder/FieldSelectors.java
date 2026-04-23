package dev.ditsche.teki.rule.builder;

import dev.ditsche.teki.FieldSelector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class FieldSelectors {

  private FieldSelectors() {}

  static <T, R> String resolve(FieldSelector<T, R> selector) {
    if (selector == null) {
      throw new IllegalArgumentException("Field selector cannot be null");
    }

    SerializedLambda lambda = serializedLambda(selector);
    String methodName = lambda.getImplMethodName();
    if (methodName.startsWith("get") && methodName.length() > 3) {
      return decapitalize(methodName.substring(3));
    }
    if (methodName.startsWith("is") && methodName.length() > 2) {
      return decapitalize(methodName.substring(2));
    }
    if (methodName.startsWith("lambda$")) {
      throw new IllegalArgumentException("Field selector must be a method reference");
    }
    return methodName;
  }

  private static SerializedLambda serializedLambda(Object selector) {
    try {
      Method writeReplace = selector.getClass().getDeclaredMethod("writeReplace");
      writeReplace.setAccessible(true);
      Object replacement = writeReplace.invoke(selector);
      if (replacement instanceof SerializedLambda lambda) {
        return lambda;
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
      throw new IllegalArgumentException("Unable to resolve field selector", ex);
    }
    throw new IllegalArgumentException("Unable to resolve field selector");
  }

  private static String decapitalize(String value) {
    if (value.length() > 1
        && Character.isUpperCase(value.charAt(0))
        && Character.isUpperCase(value.charAt(1))) {
      return value;
    }
    return Character.toLowerCase(value.charAt(0)) + value.substring(1);
  }
}

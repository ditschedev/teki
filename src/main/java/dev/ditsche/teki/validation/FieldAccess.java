package dev.ditsche.teki.validation;

import dev.ditsche.teki.error.FieldNotAccessibleException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents an internal validation component used by Teki.
 *
 * @author Tobias Dittmann
 */
public final class FieldAccess {

  private FieldAccess() {}

  /**
   * Returns all declared fields on a type and its superclasses.
   *
   * @param type type to inspect
   * @return all fields
   */
  public static List<Field> getAllFields(Class<?> type) {
    List<Field> fields = new ArrayList<>();
    for (Class<?> c = type; c != null; c = c.getSuperclass()) {
      fields.addAll(Arrays.asList(c.getDeclaredFields()));
    }
    return fields;
  }

  /**
   * Finds a field by name.
   *
   * @param fields fields to search
   * @param name field name to find
   * @return matching field or {@code null}
   */
  public static Field findField(List<Field> fields, String name) {
    return fields.stream().filter(field -> field.getName().equals(name)).findFirst().orElse(null);
  }

  /**
   * Reads a field value reflectively.
   *
   * @param object value or object being validated
   * @param field field to read
   * @return field value
   */
  public static Object read(Object object, Field field) {
    if (object.getClass().isRecord()) {
      Method accessor = findRecordAccessor(object.getClass(), field.getName());
      if (accessor != null) {
        try {
          accessor.setAccessible(true);
          return accessor.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException ex) {
          throw new FieldNotAccessibleException();
        }
      }
    }

    try {
      field.setAccessible(true);
      return field.get(object);
    } catch (IllegalAccessException ex) {
      throw new FieldNotAccessibleException();
    }
  }

  /**
   * Writes a field value reflectively.
   *
   * @param object value or object being validated
   * @param field field to write
   * @param value expected or replacement value
   */
  public static void write(Object object, Field field, Object value) {
    if (Modifier.isFinal(field.getModifiers())) {
      throw new FieldNotAccessibleException();
    }

    try {
      field.setAccessible(true);
      field.set(object, value);
    } catch (IllegalAccessException ex) {
      throw new FieldNotAccessibleException();
    }
  }

  /**
   * Creates a new record instance with selected component values replaced.
   *
   * @param record record instance to reconstruct
   * @param changes record component replacements
   * @return reconstructed record instance
   */
  public static Object reconstructRecord(Object record, Map<String, Object> changes) {
    Class<?> type = record.getClass();
    RecordComponent[] components = type.getRecordComponents();
    Class<?>[] componentTypes = new Class<?>[components.length];
    Object[] values = new Object[components.length];

    for (int i = 0; i < components.length; i++) {
      RecordComponent component = components[i];
      componentTypes[i] = component.getType();
      values[i] =
          changes.containsKey(component.getName())
              ? changes.get(component.getName())
              : invokeRecordAccessor(record, component.getAccessor());
    }

    try {
      Constructor<?> constructor = type.getDeclaredConstructor(componentTypes);
      constructor.setAccessible(true);
      return constructor.newInstance(values);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException ex) {
      throw new FieldNotAccessibleException();
    }
  }

  private static Method findRecordAccessor(Class<?> type, String name) {
    try {
      return type.getDeclaredMethod(name);
    } catch (NoSuchMethodException ex) {
      return null;
    }
  }

  private static Object invokeRecordAccessor(Object record, Method accessor) {
    try {
      accessor.setAccessible(true);
      return accessor.invoke(record);
    } catch (IllegalAccessException | InvocationTargetException ex) {
      throw new FieldNotAccessibleException();
    }
  }
}

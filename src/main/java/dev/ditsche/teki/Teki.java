package dev.ditsche.teki;

import dev.ditsche.teki.error.ErrorBag;
import dev.ditsche.teki.error.ValidationException;
import dev.ditsche.teki.rule.builder.Builder;
import dev.ditsche.teki.validation.FieldAccess;
import dev.ditsche.teki.validation.Validatable;
import dev.ditsche.teki.validation.ValidationResult;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * Entry point for building and running Teki validation schemas.
 *
 * @author Tobias Dittmann
 */
public final class Teki {

  private static final Map<Class<?>, Teki> CACHE = new ConcurrentHashMap<>();
  private static final AtomicReference<MessageResolver> GLOBAL_RESOLVER =
      new AtomicReference<>(TekiMessages.defaults());

  private final List<Validatable> fields;
  private final List<CrossFieldConstraint> constraints;
  private final List<ConditionalValidation> conditionals;
  private boolean fromCache;
  private MessageResolver messageResolver;

  private Teki() {
    this.fields = new ArrayList<>();
    this.constraints = new ArrayList<>();
    this.conditionals = new ArrayList<>();
  }

  // -------------------------------------------------------------------------
  // Factory methods — fluent builder API
  // -------------------------------------------------------------------------

  /**
   * Creates a validation schema from fluent rule builders.
   *
   * @param builders rule builders to include in the schema
   * @return a schema containing the supplied rules
   */
  public static Teki fromRules(Builder... builders) {
    Teki teki = new Teki();
    for (Builder builder : builders) {
      teki.addField(builder);
    }
    return teki;
  }

  /**
   * Creates a validation schema from prepared validation components.
   *
   * @param validatable validation components to include in the schema
   * @return a schema containing the supplied components
   */
  public static Teki fromRules(Validatable... validatable) {
    Teki teki = new Teki();
    for (Validatable val : validatable) {
      teki.addField(val);
    }
    return teki;
  }

  /**
   * Creates an empty validation schema that can be populated programmatically.
   *
   * @return an empty schema
   */
  public static Teki empty() {
    return new Teki();
  }

  // -------------------------------------------------------------------------
  // Factory methods — annotation-driven API
  // -------------------------------------------------------------------------

  /**
   * Creates or reuses a cached validation schema by scanning annotations on the supplied type.
   *
   * @param type DTO or record type to scan
   * @return a schema based on the type's validation annotations
   */
  public static Teki from(Class<?> type) {
    return CACHE.computeIfAbsent(
        type,
        t -> {
          Teki teki = AnnotationScanner.scan(t);
          teki.fromCache = true;
          return teki;
        });
  }

  // -------------------------------------------------------------------------
  // Message resolver
  // -------------------------------------------------------------------------

  /**
   * Configures per-schema message templates. Takes precedence over the global messages set via
   * {@link #setGlobalMessages(TekiMessages)}.
   *
   * <pre>{@code
   * Teki schema = Teki.fromRules(...)
   *     .messages(TekiMessages.defaults().override(TekiErrors.EMAIL, "Invalid email"));
   * }</pre>
   *
   * @param messages configured {@link TekiMessages} instance
   * @return this schema for chaining
   */
  public Teki messages(TekiMessages messages) {
    this.messageResolver = messages;
    return this;
  }

  /**
   * Registers a custom {@link MessageResolver} for this schema. Use this for full programmatic
   * control; prefer {@link #messages(TekiMessages)} for simple template overrides.
   *
   * <p>Return {@code null} to fall back to the global resolver or the rule's built-in message.
   *
   * @param resolver message resolver to use
   * @return this schema for chaining
   */
  public Teki messages(MessageResolver resolver) {
    this.messageResolver = resolver;
    return this;
  }

  /**
   * Sets the global message resolver used by all {@code Teki} instances that do not have a
   * per-schema resolver registered via {@link #messages(MessageResolver)}.
   *
   * <p>Resolution order: per-schema resolver → global resolver → {@link Rule#message(String)}.
   *
   * <p>This method is thread-safe.
   *
   * @param resolver global resolver to use
   */
  public static void setGlobalResolver(MessageResolver resolver) {
    GLOBAL_RESOLVER.set(resolver);
  }

  /**
   * Resets the global message resolver to the built-in English defaults (same as {@link
   * TekiMessages#defaults()}).
   */
  public static void clearGlobalResolver() {
    GLOBAL_RESOLVER.set(TekiMessages.defaults());
  }

  /**
   * Sets the global {@link TekiMessages} used by all {@code Teki} instances that do not have a
   * per-schema resolver. This is the preferred way to configure messages for most applications.
   *
   * <pre>{@code
   * Teki.setGlobalMessages(
   *     TekiMessages.defaults()
   *         .override(TekiErrors.BETWEEN,  "Must be between {min} and {max}")
   *         .override(TekiErrors.REQUIRED, "{field} is required")
   *         .fromProperties("i18n/messages_de.properties")
   * );
   * }</pre>
   *
   * @param messages configured {@link TekiMessages} instance
   */
  public static void setGlobalMessages(TekiMessages messages) {
    GLOBAL_RESOLVER.set(messages);
  }

  /**
   * Returns the current global message resolver.
   *
   * @return global resolver
   */
  public static MessageResolver getGlobalResolver() {
    return GLOBAL_RESOLVER.get();
  }

  private MessageResolver effectiveResolver() {
    MessageResolver instance = this.messageResolver;
    if (instance == null) return GLOBAL_RESOLVER.get();
    MessageResolver global = GLOBAL_RESOLVER.get();
    return (field, type, params) -> {
      String msg = instance.resolve(field, type, params);
      return msg != null ? msg : (global != null ? global.resolve(field, type, params) : null);
    };
  }

  // -------------------------------------------------------------------------
  // Field registration
  // -------------------------------------------------------------------------

  /**
   * Adds a fluent rule builder to this schema.
   *
   * @param builder rule builder to add
   * @return this schema for chaining
   */
  public Teki addField(Builder builder) {
    return addField(builder.build());
  }

  /**
   * Adds a prepared validation component to this schema.
   *
   * @param validatable validation component to add
   * @return this schema for chaining
   */
  public Teki addField(Validatable validatable) {
    this.fields.add(validatable);
    return this;
  }

  /**
   * Adds multiple fluent rule builders to this schema.
   *
   * @param builders rule builders to add
   * @return this schema for chaining
   */
  public Teki addFields(Builder... builders) {
    for (Builder builder : builders) {
      this.addField(builder.build());
    }
    return this;
  }

  /**
   * Adds multiple prepared validation components to this schema.
   *
   * @param validatable validation components to add
   * @return this schema for chaining
   */
  public Teki addFields(Validatable... validatable) {
    this.fields.addAll(List.of(validatable));
    return this;
  }

  // -------------------------------------------------------------------------
  // Cross-field constraints
  // -------------------------------------------------------------------------

  /**
   * Adds a whole-object invariant that is checked after per-field validation.
   *
   * <p>Must only be called on instances created via {@link #fromRules}. Calling this on a cached
   * instance obtained from {@link #from} throws {@link IllegalStateException} to prevent
   * unintentionally mutating a shared schema.
   *
   * @param predicate returns {@code true} when the invariant holds
   * @param field field name to attach the error to when the invariant fails
   * @param type stable error type identifier used for i18n
   * @param <T> object type the predicate operates on
   * @return this schema for chaining
   * @throws IllegalStateException when called on a cached instance
   */
  public <T> Teki constraint(Predicate<T> predicate, String field, String type) {
    return constraint(predicate, field, type, null);
  }

  /**
   * Adds a whole-object invariant that is checked after per-field validation.
   *
   * @param predicate returns {@code true} when the invariant holds
   * @param field field name to attach the error to when the invariant fails
   * @param type stable error type identifier used for i18n
   * @param message optional human-readable fallback message
   * @param <T> object type the predicate operates on
   * @return this schema for chaining
   * @throws IllegalStateException when called on a cached instance
   */
  public <T> Teki constraint(Predicate<T> predicate, String field, String type, String message) {
    if (fromCache) {
      throw new IllegalStateException(
          "Cross-field constraints cannot be added to a schema obtained from Teki.from(). "
              + "Use Teki.fromRules() to build a schema that supports constraints.");
    }
    this.constraints.add(new CrossFieldConstraint(predicate, field, type, message));
    return this;
  }

  // -------------------------------------------------------------------------
  // Conditional validation
  // -------------------------------------------------------------------------

  /**
   * Adds a conditional block: the supplied validatables are only evaluated when the predicate holds
   * for the object under validation.
   *
   * <p>Must only be called on instances created via {@link #fromRules}. Calling this on a cached
   * instance obtained from {@link #from} throws {@link IllegalStateException}.
   *
   * @param condition predicate evaluated against the full object
   * @param builders rule builders to run when the condition is true
   * @param <T> object type the predicate operates on
   * @return this schema for chaining
   * @throws IllegalStateException when called on a cached instance
   */
  public <T> Teki when(Predicate<T> condition, Builder... builders) {
    if (fromCache) {
      throw new IllegalStateException(
          "Conditional validation cannot be added to a schema obtained from Teki.from(). "
              + "Use Teki.fromRules() to build a schema that supports conditionals.");
    }
    List<Validatable> validatables = new ArrayList<>();
    for (Builder builder : builders) validatables.add(builder.build());
    this.conditionals.add(new ConditionalValidation(condition, validatables));
    return this;
  }

  /**
   * Adds a conditional block: the supplied validatables are only evaluated when the predicate holds
   * for the object under validation.
   *
   * <p>Must only be called on instances created via {@link #fromRules}. Calling this on a cached
   * instance obtained from {@link #from} throws {@link IllegalStateException}.
   *
   * @param condition predicate evaluated against the full object
   * @param validatables validation components to run when the condition is true
   * @param <T> object type the predicate operates on
   * @return this schema for chaining
   * @throws IllegalStateException when called on a cached instance
   */
  public <T> Teki when(Predicate<T> condition, Validatable... validatables) {
    if (fromCache) {
      throw new IllegalStateException(
          "Conditional validation cannot be added to a schema obtained from Teki.from(). "
              + "Use Teki.fromRules() to build a schema that supports conditionals.");
    }
    this.conditionals.add(new ConditionalValidation(condition, List.of(validatables)));
    return this;
  }

  // -------------------------------------------------------------------------
  // Validation
  // -------------------------------------------------------------------------

  /**
   * Validates the supplied object and applies any transformation rules.
   *
   * <p>Mutable objects are updated in place. Records are reconstructed when a transformation
   * changes any component value.
   *
   * @param object object to validate
   * @param <T> validated object type
   * @return the validated object, or a reconstructed record instance when record values changed
   * @throws ValidationException when one or more rules fail
   */
  public <T> T validate(T object) {
    return check(object).orElseThrow();
  }

  /**
   * Validates the supplied object and optionally stops on the first nested validation failure.
   *
   * <p>Mutable objects are updated in place. Records are reconstructed when a transformation
   * changes any component value.
   *
   * @param object object to validate
   * @param abortEarly whether to stop collecting errors after the first failure
   * @param <T> validated object type
   * @return the validated object, or a reconstructed record instance when record values changed
   * @throws ValidationException when one or more rules fail
   */
  public <T> T validate(T object, boolean abortEarly) {
    return check(object, abortEarly).orElseThrow();
  }

  /**
   * Validates the supplied object without throwing.
   *
   * <p>The returned {@link ValidationOutcome} carries the result: either the valid (and possibly
   * transformed) object or the accumulated errors. Use {@link ValidationOutcome#orElseThrow()} to
   * convert back to the throwing path when needed.
   *
   * @param object object to validate
   * @param <T> validated object type
   * @return a {@link ValidationOutcome} carrying the result
   */
  public <T> ValidationOutcome<T> check(T object) {
    return check(object, false);
  }

  /**
   * Validates the supplied object without throwing, with configurable abort-early behaviour.
   *
   * @param object object to validate
   * @param abortEarly when {@code true}, validation stops collecting errors after the first failure
   * @param <T> validated object type
   * @return a {@link ValidationOutcome} carrying the result
   */
  public <T> ValidationOutcome<T> check(T object, boolean abortEarly) {
    ErrorBag errorBag = new ErrorBag();
    List<Field> fieldSet = FieldAccess.getAllFields(object.getClass());
    boolean record = object.getClass().isRecord();
    boolean changed = false;
    Map<String, Object> recordChanges = record ? new HashMap<>() : Map.of();
    MessageResolver resolver = effectiveResolver();

    for (Validatable validatable : this.fields) {
      Field field = FieldAccess.findField(fieldSet, validatable.getField());
      if (field == null) continue;
      Object value = FieldAccess.read(object, field);
      ValidationResult result;
      try {
        result = validatable.validate("", value, abortEarly, resolver);
      } catch (ValidationException e) {
        // abortEarly=true causes field-level validators to throw instead of returning errors.
        // Catch and convert so check() never throws — that is validate()'s job via orElseThrow().
        for (var error : e.getErrors()) errorBag.add(error);
        return ValidationOutcome.invalid(object, errorBag);
      }
      if (result.isChanged()) {
        changed = true;
        if (record) {
          recordChanges.put(field.getName(), result.getValue());
        } else {
          FieldAccess.write(object, field, result.getValue());
        }
      }
      errorBag.merge(result.getErrorBag());
      if (abortEarly && !errorBag.isEmpty()) return ValidationOutcome.invalid(object, errorBag);
    }

    for (ConditionalValidation conditional : conditionals) {
      if (conditional.test(object)) {
        for (Validatable validatable : conditional.validatables()) {
          Field field = FieldAccess.findField(fieldSet, validatable.getField());
          if (field == null) continue;
          Object value = FieldAccess.read(object, field);
          ValidationResult result;
          try {
            result = validatable.validate("", value, abortEarly, resolver);
          } catch (ValidationException e) {
            for (var error : e.getErrors()) errorBag.add(error);
            return ValidationOutcome.invalid(object, errorBag);
          }
          if (result.isChanged()) {
            changed = true;
            if (record) {
              recordChanges.put(field.getName(), result.getValue());
            } else {
              FieldAccess.write(object, field, result.getValue());
            }
          }
          errorBag.merge(result.getErrorBag());
          if (abortEarly && !errorBag.isEmpty()) return ValidationOutcome.invalid(object, errorBag);
        }
      }
    }

    for (CrossFieldConstraint constraint : constraints) {
      if (!constraint.test(object)) {
        String msg =
            resolver != null
                ? resolver.resolve(constraint.field(), constraint.type(), Map.of())
                : null;
        if (msg == null) msg = constraint.message();
        errorBag.add(constraint.field(), constraint.type(), msg);
        if (abortEarly) return ValidationOutcome.invalid(object, errorBag);
      }
    }

    if (!errorBag.isEmpty()) return ValidationOutcome.invalid(object, errorBag);

    if (record && changed) {
      @SuppressWarnings("unchecked")
      T reconstructed = (T) FieldAccess.reconstructRecord(object, recordChanges);
      return ValidationOutcome.valid(reconstructed);
    }
    return ValidationOutcome.valid(object);
  }
}

package dev.ditsche.teki;

import dev.ditsche.teki.annotation.*;
import dev.ditsche.teki.annotation.After;
import dev.ditsche.teki.annotation.Before;
import dev.ditsche.teki.annotation.Future;
import dev.ditsche.teki.annotation.FutureOrPresent;
import dev.ditsche.teki.annotation.Negative;
import dev.ditsche.teki.annotation.NegativeOrZero;
import dev.ditsche.teki.annotation.NotBlank;
import dev.ditsche.teki.annotation.OneOf;
import dev.ditsche.teki.annotation.Past;
import dev.ditsche.teki.annotation.PastOrPresent;
import dev.ditsche.teki.annotation.Positive;
import dev.ditsche.teki.annotation.PositiveOrZero;
import dev.ditsche.teki.annotation.Uuid;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.ruleset.*;
import dev.ditsche.teki.validation.Validatable;
import dev.ditsche.teki.validation.ValidationArray;
import dev.ditsche.teki.validation.ValidationField;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Scans validation annotations into executable Teki schemas.
 *
 * @author Tobias Dittmann
 */
final class AnnotationScanner {

  private AnnotationScanner() {}

  static Teki scan(Class<?> type) {
    Teki teki = Teki.empty();
    for (Field field : getAllFields(type)) {
      Validatable validatable = buildValidatable(field);
      if (validatable != null) {
        teki.addField(validatable);
      }
    }
    return teki;
  }

  private static Validatable buildValidatable(Field field) {
    boolean isCollection = isCollectionType(field.getType());
    boolean hasRequired = field.isAnnotationPresent(Required.class);

    if (isCollection) {
      return buildCollectionValidatable(field, hasRequired);
    }

    List<Rule> rules = new LinkedList<>();
    boolean hasConstraints = false;

    if (hasRequired) {
      rules.add(new RequiredRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Trim.class)) {
      rules.add(new TrimRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Email.class)) {
      rules.add(new EmailRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Url.class)) {
      rules.add(new UrlRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(IpAddress.class)) {
      rules.add(ipAddressRule(field.getAnnotation(IpAddress.class)));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(AlphaNumeric.class)) {
      rules.add(new AlphaNumericRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(CreditCard.class)) {
      rules.add(new CreditCardRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Uuid.class)) {
      rules.add(uuidRule(field.getAnnotation(Uuid.class)));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(NotBlank.class)) {
      rules.add(new NotBlankRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(OneOf.class)) {
      rules.add(new OneOfRule(field.getAnnotation(OneOf.class)));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Positive.class)) {
      rules.add(new PositiveRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(PositiveOrZero.class)) {
      rules.add(new PositiveOrZeroRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Negative.class)) {
      rules.add(new NegativeRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(NegativeOrZero.class)) {
      rules.add(new NegativeOrZeroRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Past.class)) {
      rules.add(new PastRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Future.class)) {
      rules.add(new FutureRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(PastOrPresent.class)) {
      rules.add(new PastOrPresentRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(FutureOrPresent.class)) {
      rules.add(new FutureOrPresentRule());
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Before.class)) {
      rules.add(new BeforeRule(field.getAnnotation(Before.class)));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(After.class)) {
      rules.add(new AfterRule(field.getAnnotation(After.class)));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Pattern.class)) {
      rules.add(new PatternRule(field.getAnnotation(Pattern.class).value()));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Min.class)) {
      rules.add(new MinRule(field.getAnnotation(Min.class).value()));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Max.class)) {
      rules.add(new MaxRule(field.getAnnotation(Max.class).value()));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Length.class)) {
      rules.add(new LengthRule(field.getAnnotation(Length.class).value()));
      hasConstraints = true;
    }
    if (field.isAnnotationPresent(Between.class)) {
      Between between = field.getAnnotation(Between.class);
      rules.add(new BetweenRule(between.min(), between.max()));
      hasConstraints = true;
    }
    boolean hasDefault = field.isAnnotationPresent(Default.class);
    if (hasDefault) {
      rules.add(new DefaultRule(resolveDefault(field)));
      hasConstraints = true;
    }

    // Custom @TekiConstraint annotations
    for (Annotation annotation : field.getAnnotations()) {
      TekiConstraint meta = annotation.annotationType().getAnnotation(TekiConstraint.class);
      if (meta != null) {
        rules.add(instantiateRule(meta.rule(), annotation));
        hasConstraints = true;
      }
    }

    if (!hasConstraints) return null;

    // @NotBlank implies presence; treat it like @Required for optionality.
    // Fields with @Default must also not be optional — DefaultRule needs to run on null.
    // @Optional is a no-op here: fields without @Required/@Default/@NotBlank are already optional.
    // It exists purely as a self-documenting marker.
    boolean hasNotBlank = field.isAnnotationPresent(NotBlank.class);
    return new ValidationField(field.getName(), rules, !hasRequired && !hasDefault && !hasNotBlank);
  }

  // -------------------------------------------------------------------------
  // Collection fields
  // -------------------------------------------------------------------------

  private static Validatable buildCollectionValidatable(Field field, boolean hasRequired) {
    // Collection-level rules: type check + cardinality constraints
    List<Rule> collectionRules = new LinkedList<>();
    collectionRules.add(new ArrayRule());
    if (hasRequired) collectionRules.add(new RequiredRule());
    if (field.isAnnotationPresent(Min.class))
      collectionRules.add(new MinRule(field.getAnnotation(Min.class).value()));
    if (field.isAnnotationPresent(Max.class))
      collectionRules.add(new MaxRule(field.getAnnotation(Max.class).value()));
    if (field.isAnnotationPresent(Length.class))
      collectionRules.add(new LengthRule(field.getAnnotation(Length.class).value()));
    if (field.isAnnotationPresent(Between.class)) {
      Between between = field.getAnnotation(Between.class);
      collectionRules.add(new BetweenRule(between.min(), between.max()));
    }

    // Element-level rules from TYPE_USE annotations on the generic type argument
    List<Rule> elementRules = buildElementRules(field);

    boolean hasAnyConstraint =
        hasRequired
            || field.isAnnotationPresent(Min.class)
            || field.isAnnotationPresent(Max.class)
            || field.isAnnotationPresent(Length.class)
            || field.isAnnotationPresent(Between.class)
            || !elementRules.isEmpty();

    if (!hasAnyConstraint) return null;

    return new ValidationArray(
        field.getName(),
        collectionRules,
        elementRules.isEmpty() ? null : elementRules,
        null,
        !hasRequired);
  }

  /**
   * Reads TYPE_USE annotations from the first generic type argument of a collection field and
   * converts them to element-level rules.
   *
   * <pre>{@code
   * List<@Email String> recipients  →  [EmailRule]
   * List<@Min(1) @Max(100) Integer> →  [MinRule(1), MaxRule(100)]
   * }</pre>
   */
  private static List<Rule> buildElementRules(Field field) {
    List<Rule> elementRules = new LinkedList<>();
    AnnotatedType annotatedType = field.getAnnotatedType();
    if (!(annotatedType instanceof AnnotatedParameterizedType paramType)) return elementRules;

    AnnotatedType[] typeArgs = paramType.getAnnotatedActualTypeArguments();
    if (typeArgs.length == 0) return elementRules;

    for (Annotation annotation : typeArgs[0].getAnnotations()) {
      Rule rule = annotationToRule(annotation);
      if (rule != null) {
        elementRules.add(rule);
        continue;
      }
      // Also support custom @TekiConstraint at the type-use position
      TekiConstraint meta = annotation.annotationType().getAnnotation(TekiConstraint.class);
      if (meta != null) {
        elementRules.add(instantiateRule(meta.rule(), annotation));
      }
    }

    return elementRules;
  }

  // -------------------------------------------------------------------------
  // Annotation → Rule mapping (used for both field-level and TYPE_USE positions)
  // -------------------------------------------------------------------------

  private static Rule annotationToRule(Annotation a) {
    if (a instanceof Required) return new RequiredRule();
    if (a instanceof Trim) return new TrimRule();
    if (a instanceof Email) return new EmailRule();
    if (a instanceof Url) return new UrlRule();
    if (a instanceof IpAddress ip) return ipAddressRule(ip);
    if (a instanceof AlphaNumeric) return new AlphaNumericRule();
    if (a instanceof CreditCard) return new CreditCardRule();
    if (a instanceof Uuid u) return uuidRule(u);
    if (a instanceof NotBlank) return new NotBlankRule();
    if (a instanceof OneOf o) return new OneOfRule(o);
    if (a instanceof Positive) return new PositiveRule();
    if (a instanceof PositiveOrZero) return new PositiveOrZeroRule();
    if (a instanceof Negative) return new NegativeRule();
    if (a instanceof NegativeOrZero) return new NegativeOrZeroRule();
    if (a instanceof Past) return new PastRule();
    if (a instanceof Future) return new FutureRule();
    if (a instanceof PastOrPresent) return new PastOrPresentRule();
    if (a instanceof FutureOrPresent) return new FutureOrPresentRule();
    if (a instanceof Before b) return new BeforeRule(b);
    if (a instanceof After af) return new AfterRule(af);
    if (a instanceof Pattern p) return new PatternRule(p.value());
    if (a instanceof Min m) return new MinRule(m.value());
    if (a instanceof Max m) return new MaxRule(m.value());
    if (a instanceof Length l) return new LengthRule(l.value());
    if (a instanceof Between s) return new BetweenRule(s.min(), s.max());
    return null;
  }

  // -------------------------------------------------------------------------
  // Custom constraint instantiation
  // -------------------------------------------------------------------------

  private static Rule instantiateRule(Class<? extends Rule> ruleClass, Annotation annotation) {
    // Prefer a constructor that accepts the annotation (allows rules to read annotation params)
    try {
      Constructor<? extends Rule> ctor =
          ruleClass.getDeclaredConstructor(annotation.annotationType());
      ctor.setAccessible(true);
      return ctor.newInstance(annotation);
    } catch (NoSuchMethodException ignored) {
      // fall through to no-arg
    } catch (Exception e) {
      throw new IllegalStateException(
          "Failed to instantiate " + ruleClass.getName() + " with annotation " + annotation, e);
    }

    try {
      Constructor<? extends Rule> ctor = ruleClass.getDeclaredConstructor();
      ctor.setAccessible(true);
      return ctor.newInstance();
    } catch (Exception e) {
      throw new IllegalStateException(
          ruleClass.getName()
              + " must have either a no-arg constructor or a constructor accepting "
              + annotation.annotationType().getName(),
          e);
    }
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private static UuidRule uuidRule(Uuid annotation) {
    return annotation.version() == 0 ? new UuidRule() : new UuidRule(annotation.version());
  }

  private static IpAddressRule ipAddressRule(IpAddress annotation) {
    return annotation.version() == 0
        ? new IpAddressRule()
        : new IpAddressRule(annotation.version());
  }

  private static boolean isCollectionType(Class<?> type) {
    return Collection.class.isAssignableFrom(type) || type.isArray();
  }

  private static Object resolveDefault(Field field) {
    String raw = field.getAnnotation(Default.class).value();
    Class<?> type = field.getType();
    if (type == int.class || type == Integer.class) return Integer.parseInt(raw);
    if (type == long.class || type == Long.class) return Long.parseLong(raw);
    if (type == double.class || type == Double.class) return Double.parseDouble(raw);
    if (type == float.class || type == Float.class) return Float.parseFloat(raw);
    if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(raw);
    return raw;
  }

  private static List<Field> getAllFields(Class<?> type) {
    List<Field> fields = new ArrayList<>();
    for (Class<?> c = type; c != null; c = c.getSuperclass()) {
      fields.addAll(Arrays.asList(c.getDeclaredFields()));
    }
    return fields;
  }
}

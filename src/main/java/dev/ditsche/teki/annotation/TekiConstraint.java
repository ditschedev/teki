package dev.ditsche.teki.annotation;

import dev.ditsche.teki.rule.Rule;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation that marks a custom annotation as a Teki validation constraint.
 *
 * <p>Annotate your own annotation with {@code @TekiConstraint(rule = MyRule.class)} and the
 * scanner will automatically pick it up on any annotated field:
 *
 * <pre>{@code
 * @TekiConstraint(rule = PhoneNumberRule.class)
 * @Target(ElementType.FIELD)
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface PhoneNumber {}
 * }</pre>
 *
 * <p>The referenced {@link Rule} class is instantiated in the following order:
 *
 * <ol>
 *   <li>A constructor that accepts the annotation instance — use this when your rule needs
 *       annotation parameters (e.g., {@code public MyRule(MyAnnotation annotation) {}}).
 *   <li>A no-arg constructor — use this for stateless rules.
 * </ol>
 *
 * @author Tobias Dittmann
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TekiConstraint {

  /**
   * Rule implementation to instantiate for fields carrying the annotated annotation.
   *
   * @return rule class
   */
  Class<? extends Rule> rule();
}

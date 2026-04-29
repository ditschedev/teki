package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.text.Normalizer;

/**
 * Transforms a string into a URL-safe slug and resolves the field with the normalized value.
 *
 * <p>The transformation pipeline:
 *
 * <ol>
 *   <li>NFD-normalize the input to decompose accented characters (e.g. {@code é} → {@code e +
 *       combining accent})
 *   <li>Strip non-ASCII codepoints
 *   <li>Lowercase
 *   <li>Replace every run of non-alphanumeric characters with a single hyphen
 *   <li>Remove leading and trailing hyphens
 * </ol>
 *
 * <p>An input that produces an empty string after the transformation (e.g. {@code "!!!"}) is
 * rejected. Successful output always satisfies {@link SlugRule}.
 *
 * @author Tobias Dittmann
 */
public final class SlugifyRule implements Rule {

  /** Creates a rule instance. */
  public SlugifyRule() {}

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    if (!(value instanceof String s)) return RuleResult.reject();
    String slug =
        Normalizer.normalize(s, Normalizer.Form.NFD)
            .replaceAll("[^\\x00-\\x7F]", "")
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-+|-+$", "");
    if (slug.isEmpty()) return RuleResult.reject();
    return RuleResult.resolve(slug);
  }

  @Override
  public String getType() {
    return TekiErrors.SLUGIFY;
  }
}

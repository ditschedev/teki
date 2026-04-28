package dev.ditsche.teki.rule.ruleset;

import dev.ditsche.teki.TekiErrors;
import dev.ditsche.teki.annotation.OneOf;
import dev.ditsche.teki.rule.Rule;
import dev.ditsche.teki.rule.RuleResult;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates that the value is one of a fixed set of allowed values.
 *
 * @author Tobias Dittmann
 */
public final class OneOfRule implements Rule {

  private final Set<String> allowed;

  public OneOfRule(String... allowed) {
    this.allowed = Set.of(allowed);
  }

  public OneOfRule(OneOf annotation) {
    this.allowed = Arrays.stream(annotation.value()).collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public RuleResult test(Object value) {
    if (value == null) return RuleResult.reject();
    return RuleResult.passes(allowed.contains(value.toString()));
  }

  @Override
  public Map<String, Object> params() {
    return Map.of("allowed", String.join(", ", allowed));
  }

  @Override
  public String getType() {
    return TekiErrors.ONE_OF;
  }
}

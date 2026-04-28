# Teki

**Fluent schema validation for Java.**

Teki is an open source Java library for validating objects with a fluent builder API or annotations.
Define field-level constraints on DTOs, records, and nested objects and get structured, human-readable errors back without framework dependencies.

[Read the documentation](https://teki.ditsche.dev) • [Getting Started](https://teki.ditsche.dev/docs/getting-started) • [GitHub Releases](https://github.com/ditschedev/teki/releases)

## Why Teki

- Fluent builder API and annotation-based validation in one library
- Structured field errors with stable type identifiers
- Nested objects, arrays, and conditional rules
- Value normalization — trim, default, and transform during validation
- No framework dependencies, works with plain Java, Spring Boot, and more

## Installation

```xml
<dependency>
  <groupId>dev.ditsche</groupId>
  <artifactId>teki</artifactId>
  <version>1.0.0</version>
</dependency>
```

Requires Java 17+.

## Quick Example

```java
import static dev.ditsche.teki.rule.builder.Rules.*;

Teki schema = Teki.fromRules(
    string(Request::getEmail).required().email().trim(),
    string(Request::password).required().min(8),
    number("age").required().positive()
);

schema.validate(request);
// or
ValidationOutcome<Request> result = schema.check(request);

```

## Development

Contributions are welcome.

- Run the Java tests with `mvn test`
- Start the docs app with `cd docs && pnpm dev`
- Use the docs site for the full guides and API explanations: [teki.ditsche.dev](https://teki.ditsche.dev)

If you want to contribute, opening an issue or pull request with a small focused change is the easiest way to get started.

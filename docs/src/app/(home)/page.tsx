"use client";

import Link from "next/link";
import posthog from "posthog-js";
import { DynamicCodeBlock } from "fumadocs-ui/components/dynamic-codeblock";
import {
  ArrowRight,
  BookOpenText,
  Braces,
  CheckCircle2,
  ListChecks,
  ShieldCheck,
} from "lucide-react";

const schemaExample = `// DTO mapped from JSON request data
record SignupRequest(
    @Required @Email @Trim String email,
    @Required @Min(8) String password,
    @Min(13) int age) {}

// Chain rules fluently, then validate the DTO
Teki signup = Teki.fromRules(
    string("email").required().email().trim(),
    string("password").required().min(8),
    number("age").min(13)
);

// Or scan annotations from a Java class
Teki signup = Teki.from(SignupRequest.class);

var request = new SignupRequest("ada@example.dev", "super_secret", 21);
var result = signup.validate(request);

if (!result.isValid()) {
    result.errors().forEach(System.out::println);
}`;

const features = [
  {
    title: "Fluent schemas",
    description:
      "Describe Java object validation with chainable rules that feel familiar if you have used Zod or Yup.",
    icon: Braces,
  },
  {
    title: "Annotation support",
    description:
      "Put constraints on DTO fields and let Teki scan them into cached validators when the model should own the contract.",
    icon: ListChecks,
  },
  {
    title: "Structured errors",
    description:
      "Collect field-level failures with stable rule types and readable messages, or abort early for fast rejection.",
    icon: ShieldCheck,
  },
];

const quickLinks = [
  {
    href: "/docs/getting-started",
    title: "Getting started",
    description: "Install Teki, define a schema, and validate your first object.",
  },
  {
    href: "/docs/schemas",
    title: "Schema builder",
    description: "Use string, number, boolean, object, and array rules fluently.",
  },
  {
    href: "/docs/annotations",
    title: "Annotations",
    description: "Scan constraints from Java classes with Teki's annotation API.",
  },
];

function SchemaPreview({ className = "" }: { className?: string }) {
  return (
    <div
      className={`overflow-hidden rounded-lg bg-white shadow-2xl ring-1 shadow-neutral-950/10 ring-neutral-950/10 dark:bg-neutral-900 dark:shadow-none dark:ring-white/10 ${className}`}
    >
      <DynamicCodeBlock
        lang="java"
        code={schemaExample}
        codeblock={{
          allowCopy: false,
          keepBackground: true,
          "data-line-numbers": true,
          className:
            "my-0 rounded-none border-0 bg-transparent shadow-none text-[0.8125rem] [&_pre]:px-4 sm:[&_pre]:px-6",
          viewportProps: {
            className:
              "max-h-none overflow-x-auto bg-white py-4 dark:bg-neutral-900 [&_.line]:px-0 [&_.line:before]:text-neutral-400 dark:[&_.line:before]:text-neutral-600",
          },
        }}
      />
    </div>
  );
}

export default function HomePage() {
  return (
    <main className="isolate bg-white text-neutral-950 dark:bg-neutral-950 dark:text-white">
      <section className="relative overflow-hidden border-b border-neutral-200 bg-neutral-50 text-neutral-950 dark:border-white/10 dark:bg-neutral-950 dark:text-white">
        <div className="mx-auto max-w-(--fd-layout-width) px-6 py-16 lg:px-10 lg:py-24">
          <div className="grid gap-12 xl:grid-cols-[9fr_11fr] xl:items-center">
            <div className="relative max-w-3xl xl:max-w-none">
              <p className="font-mono text-sm text-emerald-700 dark:text-emerald-300">
                Schema validation for Java
              </p>
              <h1 className="mt-4 max-w-[13ch] text-5xl font-semibold tracking-tight text-balance sm:text-6xl">
                Teki is schema validation for Java DTOs.
              </h1>
              <p className="mt-6 max-w-[42ch] text-base text-pretty text-neutral-600 sm:text-lg dark:text-neutral-300">
                Validate JSON-mapped requests and configuration objects with fluent rules,
                annotations, nested schemas, and structured field errors.
              </p>

              <div className="mt-8 flex flex-col gap-2 sm:flex-row">
                <Link
                  href="/docs/getting-started"
                  className="inline-flex h-[38px] cursor-pointer items-center justify-center gap-2 rounded-md border border-emerald-800/75 bg-emerald-700 px-4 text-base font-normal text-white outline-0 transition-all duration-200 ease-out hover:border-emerald-900 hover:bg-emerald-800 focus-visible:outline-4 focus-visible:outline-offset-1 focus-visible:outline-emerald-700 sm:text-sm dark:border-emerald-700 dark:bg-emerald-800 dark:hover:border-emerald-600 dark:hover:bg-emerald-900 dark:focus-visible:outline-emerald-600"
                  onClick={() =>
                    posthog.capture("docs_cta_clicked", { destination: "/docs/getting-started" })
                  }
                >
                  <span className="truncate">Read the docs</span>
                  <ArrowRight className="size-4" />
                </Link>
                <Link
                  href="https://github.com/ditschedev/teki"
                  className="inline-flex h-[38px] cursor-pointer items-center justify-center rounded-md border border-neutral-300 bg-white px-4 text-base font-normal text-neutral-950 outline-0 transition-all duration-200 ease-out hover:border-neutral-400 hover:bg-neutral-100 focus-visible:outline-4 focus-visible:outline-offset-1 focus-visible:outline-emerald-700 sm:text-sm dark:border-white/15 dark:bg-white/10 dark:text-white dark:hover:border-white/25 dark:hover:bg-white/15 dark:focus-visible:outline-emerald-500"
                  onClick={() => posthog.capture("github_link_clicked", { location: "hero" })}
                >
                  <span className="truncate">View on GitHub</span>
                </Link>
              </div>

              <div className="mt-5 flex gap-8 text-base text-neutral-500 sm:text-sm dark:text-neutral-400">
                <p className="font-mono">Java 17+</p>
                <p className="font-mono">Fluent API</p>
                <p className="font-mono">MIT licensed</p>
              </div>
            </div>

            <SchemaPreview className="min-w-0 xl:-mr-10 xl:rounded-r-none 2xl:-mr-[calc((100vw-var(--fd-layout-width))/2+2.5rem)]" />
          </div>
        </div>
      </section>

      <section className="border-b border-neutral-200 bg-white dark:border-white/10 dark:bg-neutral-950">
        <div className="mx-auto max-w-(--fd-layout-width) px-6 py-14 lg:px-10 lg:py-16">
          <div className="grid gap-5 lg:grid-cols-[8fr_12fr] lg:items-end">
            <div>
              <p className="font-mono text-sm text-emerald-700 dark:text-emerald-300">Why Teki</p>
              <h2 className="mt-3 max-w-[18ch] text-4xl font-semibold tracking-tight text-balance sm:text-5xl">
                Java validation with schema-shaped ergonomics.
              </h2>
            </div>
            <p className="max-w-[58ch] text-base text-pretty text-neutral-600 sm:text-lg dark:text-neutral-300">
              Use fluent rules when validation belongs near request handling, or annotations when
              the DTO should carry its own contract. Both paths produce the same structured errors.
            </p>
          </div>

          <dl className="mt-10 grid border-y border-neutral-950/10 md:grid-cols-3 md:divide-x md:divide-neutral-950/10 dark:border-white/10 dark:md:divide-white/10">
            {features.map((feature) => {
              const Icon = feature.icon;

              return (
                <div
                  key={feature.title}
                  className="border-t border-neutral-950/10 py-6 first:border-t-0 md:border-t-0 md:px-6 md:first:pl-0 md:last:pr-0 dark:border-white/10"
                >
                  <dt className="flex items-start gap-4 text-lg font-semibold">
                    <span className="mt-1 flex size-10 shrink-0 items-center justify-center rounded-md border border-emerald-700/15 bg-emerald-50 text-emerald-700 dark:border-emerald-300/15 dark:bg-emerald-400/10 dark:text-emerald-300">
                      <Icon className="size-5" />
                    </span>
                    <span>
                      <span>{feature.title}</span>
                      <span className="mt-2 flex h-px w-10 bg-emerald-700/50 dark:bg-emerald-300/50" />
                    </span>
                  </dt>
                  <dd className="mt-5 text-base text-pretty text-neutral-600 sm:text-sm dark:text-neutral-300">
                    {feature.description}
                  </dd>
                </div>
              );
            })}
          </dl>
        </div>
      </section>

      <section className="bg-neutral-50 dark:bg-neutral-900">
        <div className="mx-auto max-w-(--fd-layout-width) px-6 py-14 lg:px-10 lg:py-16">
          <div className="grid gap-10 lg:grid-cols-[9fr_11fr] lg:items-start">
            <div>
              <p className="font-mono text-sm text-emerald-700 dark:text-emerald-300">
                Explore the docs
              </p>
              <h2 className="mt-3 max-w-[20ch] text-4xl font-semibold tracking-tight text-balance sm:text-5xl">
                Start with rules, then choose schemas or annotations.
              </h2>
              <p className="mt-5 max-w-[42ch] text-base text-pretty text-neutral-600 sm:text-lg dark:text-neutral-300">
                The docs cover installation, builder rules, annotation scanning, custom rules, and
                structured validation errors.
              </p>
            </div>

            <div className="divide-y divide-neutral-950/10 border-y border-neutral-950/10 dark:divide-white/10 dark:border-white/10">
              {quickLinks.map((link, index) => (
                <Link
                  key={link.href}
                  href={link.href}
                  className="group -mx-4 flex items-start gap-5 rounded-lg border border-transparent p-4 transition hover:border-emerald-700/25 hover:bg-white dark:hover:border-emerald-300/25 dark:hover:bg-white/5"
                  onClick={() =>
                    posthog.capture("quick_link_clicked", { title: link.title, href: link.href })
                  }
                >
                  <span className="font-mono text-sm text-neutral-400 tabular-nums dark:text-neutral-500">
                    0{index + 1}
                  </span>
                  <div className="min-w-0 flex-1">
                    <p className="text-xl font-semibold tracking-tight transition group-hover:text-emerald-800 dark:group-hover:text-emerald-300">
                      {link.title}
                    </p>
                    <p className="mt-2 max-w-[44ch] text-base text-pretty text-neutral-600 sm:text-sm dark:text-neutral-300">
                      {link.description}
                    </p>
                  </div>
                  <span className="mt-1 flex size-9 shrink-0 items-center justify-center rounded-md border border-neutral-950/10 text-neutral-500 transition group-hover:border-emerald-700/40 group-hover:text-emerald-800 dark:border-white/10 dark:text-neutral-400 dark:group-hover:border-emerald-300/40 dark:group-hover:text-emerald-300">
                    <ArrowRight className="size-4" />
                  </span>
                </Link>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="border-t border-neutral-200 bg-white dark:border-white/10 dark:bg-neutral-950">
        <div className="mx-auto flex max-w-(--fd-layout-width) flex-col gap-4 px-6 py-8 text-base text-neutral-600 sm:flex-row sm:items-center sm:justify-between sm:text-sm lg:px-10 dark:text-neutral-300">
          <div className="flex items-center gap-3">
            <CheckCircle2 className="size-4 text-emerald-700 dark:text-emerald-300" />
            <span>Fluent rules, annotation scanning, nested validation, and field errors.</span>
          </div>
          <div className="flex items-center gap-5">
            <Link
              href="/docs"
              className="inline-flex items-center gap-2 transition hover:text-neutral-950 dark:hover:text-white"
            >
              <BookOpenText className="size-4" />
              Browse docs
            </Link>
            <Link
              href="https://github.com/ditschedev/teki"
              className="transition hover:text-neutral-950 dark:hover:text-white"
              onClick={() => posthog.capture("github_link_clicked", { location: "footer" })}
            >
              GitHub
            </Link>
          </div>
        </div>
      </section>
    </main>
  );
}

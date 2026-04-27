import { DocsBody, DocsPage } from "fumadocs-ui/layouts/docs/page";
import { Card, Cards } from "fumadocs-ui/components/card";
import { ArrowRight, BookOpenText } from "lucide-react";

export default function DocsNotFound() {
  return (
    <DocsPage>
      <DocsBody>
        <div className="flex flex-col items-start py-8">
          <p className="font-mono text-sm font-medium text-emerald-700 tabular-nums dark:text-emerald-300">
            404
          </p>

          <h1 className="mt-3 max-w-[22ch] text-3xl font-semibold tracking-tight text-balance sm:text-4xl">
            This page doesn&apos;t exist.
          </h1>

          <p className="mt-4 max-w-[48ch] text-base text-pretty text-neutral-600 dark:text-neutral-300">
            The page you&apos;re looking for may have been moved, renamed, or removed. Use the
            sidebar to navigate, or start from the beginning.
          </p>

          <Cards className="mt-8 w-full">
            <Card
              href="/docs/getting-started"
              icon={<ArrowRight />}
              title="Getting started"
              description="Install Teki and validate your first object."
            />
            <Card
              href="/docs"
              icon={<BookOpenText />}
              title="Browse all docs"
              description="Find what you need from the full documentation."
            />
          </Cards>
        </div>
      </DocsBody>
    </DocsPage>
  );
}

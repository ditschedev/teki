import Link from "next/link";
import { ArrowRight, BookOpenText } from "lucide-react";

export default function NotFound() {
  return (
    <main className="isolate flex min-h-dvh flex-col bg-white text-neutral-950 dark:bg-neutral-950 dark:text-white">
      <div className="relative flex flex-1 flex-col items-center justify-center overflow-hidden px-6 py-24">
        <div
          aria-hidden="true"
          className="pointer-events-none absolute inset-0 [background-image:radial-gradient(circle,rgba(0,0,0,0.07)_1px,transparent_1px)] [background-size:22px_22px] [mask-image:radial-gradient(ellipse_60%_60%_at_50%_50%,black,transparent)] dark:[background-image:radial-gradient(circle,rgba(255,255,255,0.05)_1px,transparent_1px)]"
        />

        <div className="relative flex flex-col items-center text-center">
          <p className="font-mono text-sm font-medium text-emerald-700 tabular-nums dark:text-emerald-300">
            404
          </p>

          <h1 className="mt-4 max-w-[18ch] text-4xl font-semibold tracking-tight text-balance sm:text-5xl">
            This page doesn&apos;t exist.
          </h1>

          <p className="mt-5 max-w-[42ch] text-base text-pretty text-neutral-600 dark:text-neutral-300">
            The URL you followed may be broken, or the page has been moved. Head back to the docs to
            find what you&apos;re looking for.
          </p>

          <div className="mt-8 flex flex-col gap-2 sm:flex-row sm:items-center">
            <Link
              href="/"
              className="inline-flex h-9.5 cursor-pointer items-center justify-center gap-2 rounded-md border border-emerald-800/75 bg-emerald-700 px-4 text-base font-normal text-white outline-0 transition-all duration-200 ease-out hover:border-emerald-900 hover:bg-emerald-800 focus-visible:outline-4 focus-visible:outline-offset-1 focus-visible:outline-emerald-700 sm:text-sm dark:border-emerald-700 dark:bg-emerald-800 dark:hover:border-emerald-600 dark:hover:bg-emerald-900 dark:focus-visible:outline-emerald-600"
            >
              <span className="font-medium">Back to home</span>
              <ArrowRight className="size-4" />
            </Link>
            <Link
              href="/docs"
              className="inline-flex h-9.5 cursor-pointer items-center justify-center gap-2 rounded-md border border-neutral-300 bg-white px-4 text-base font-normal text-neutral-950 outline-0 transition-all duration-200 ease-out hover:border-neutral-400 hover:bg-neutral-100 focus-visible:outline-4 focus-visible:outline-offset-1 focus-visible:outline-emerald-700 sm:text-sm dark:border-white/15 dark:bg-white/10 dark:text-white dark:hover:border-white/25 dark:hover:bg-white/15 dark:focus-visible:outline-emerald-500"
            >
              <BookOpenText className="size-4" />
              <span className="font-medium">Browse the docs</span>
            </Link>
          </div>
        </div>
      </div>

      <footer className="border-t border-neutral-200 dark:border-white/10">
        <div className="mx-auto flex max-w-(--fd-layout-width) items-center justify-between px-6 py-6 text-sm text-neutral-500 lg:px-10 dark:text-neutral-400">
          <span className="font-mono">teki</span>
          <Link
            href="https://github.com/ditschedev/teki"
            target="_blank"
            className="transition hover:text-neutral-950 dark:hover:text-white"
          >
            GitHub
          </Link>
        </div>
      </footer>
    </main>
  );
}

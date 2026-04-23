import type { BaseLayoutProps } from "fumadocs-ui/layouts/shared";
import { appName, gitConfig } from "./shared";

function TekiLogo(props: React.ComponentProps<"svg">) {
  return (
    <svg viewBox="0 0 33 48" fill="none" aria-hidden="true" {...props}>
      <g fill="currentColor">
        <path d="m8.90776 17.2143 14.84614-8.57144-8.9077-5.14286-14.84586752 8.5713-.00033248 17.1426 8.90769 5.1429z" />
        <path d="m32.8965 17.7859v17.1429l-14.8462 8.5711-8.90772-5.1429 14.84612-8.5714.0001-17.1425z" />
      </g>
    </svg>
  );
}

function NavTitle() {
  return (
    <div className="text-fd-foreground flex items-center gap-2">
      <TekiLogo className="h-5 w-3.5 text-emerald-700 dark:text-emerald-400" />
      <span className="text-base font-medium tracking-tight">{appName}</span>
    </div>
  );
}

export function baseOptions(): BaseLayoutProps {
  return {
    nav: {
      // JSX supported
      title: <NavTitle />,
    },
    githubUrl: `https://github.com/${gitConfig.user}/${gitConfig.repo}`,
  };
}

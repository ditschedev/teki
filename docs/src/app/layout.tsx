import { RootProvider } from "fumadocs-ui/provider/next";
import "./global.css";
import { Inter } from "next/font/google";
import type { Metadata } from "next";
import { appName } from "@/lib/shared";

const inter = Inter({
  subsets: ["latin"],
});

const title = `${appName} - Java Schema Validation for DTOs and JSON Data`;
const description =
  "Teki is a Java validation library for JSON-shaped data, request DTOs, and configuration objects with fluent schemas, annotations, and structured field errors.";

export const metadata: Metadata = {
  metadataBase: new URL("https://teki.ditsche.dev"),
  title: {
    default: title,
    template: `%s - ${appName}`,
  },
  description,
  applicationName: appName,
  keywords: [
    "Java validation",
    "Java schema validation",
    "DTO validation",
    "JSON validation",
    "fluent validation",
    "Java annotations",
    "structured validation errors",
    "request validation",
  ],
  authors: [{ name: "ditschedev", url: "https://github.com/ditschedev" }],
  creator: "ditschedev",
  publisher: "ditschedev",
  alternates: {
    canonical: "/",
  },
  openGraph: {
    type: "website",
    url: "/",
    siteName: appName,
    title,
    description,
    images: [
      {
        url: "/logo.svg",
        width: 33,
        height: 48,
        alt: `${appName} logo`,
      },
    ],
  },
  twitter: {
    card: "summary",
    title,
    description,
    images: ["/logo.svg"],
  },
  robots: {
    index: true,
    follow: true,
  },
};

export default function Layout({ children }: LayoutProps<"/">) {
  return (
    <html lang="en" className={`${inter.className} antialiased`} suppressHydrationWarning>
      <body className="flex min-h-dvh flex-col">
        <RootProvider>{children}</RootProvider>
      </body>
    </html>
  );
}

# Repository Guidelines

## Project Structure & Module Organization
- `app/` contains the primary Android module. Source lives under `app/src/main/java/com/yoyicue/chinesechecker`, and UI resources are in `app/src/main/res`.
- `docs/` holds reference material shipped with the project; update it when behaviour changes.
- `scripts/` is reserved for helper automation (release packaging, localisation sync). Reuse these scripts rather than duplicating logic.
- Build artefacts (for example `chinesechecker_v0.1.2.apk`) are produced at the repository root; keep only the latest signed package in version control.

## Build, Test, and Development Commands
- `./gradlew assembleDebug` – compile the app and package a debuggable APK into `app/build/outputs/apk/debug/`. The wrapper in `gradle/wrapper/` targets Android SDK 34, so ensure a Java 17 runtime is available in `$JAVA_HOME`.
- `./gradlew installDebug` – push the current debug build onto a connected device or emulator.
- `./gradlew lint` – run Android Lint; fix warnings before opening a pull request.
- `./gradlew test` – execute JVM unit tests in `app/src/test`. Instrumented tests (once added) run via `./gradlew connectedAndroidTest`.

## Coding Style & Naming Conventions
- Kotlin sources use 4-space indentation, no tabs. Rely on Android Studio’s “Reformat Code” with default Kotlin style.
- Compose functions stay in PascalCase, while local variables and parameters use camelCase. Prefer immutable `val` and scoped `remember` blocks.
- Resource identifiers follow lowercase underscore names (`howto_rules_long_heading`), and drawable densities live under the standard `mipmap-`/`drawable-` folders.
- Keep localisation keys shared across languages; new strings must land in every `values-*/strings.xml` folder.
- For the French (`values-fr`) and Italian (`values-it`) bundles, use plain apostrophes (`'`) inside strings and avoid HTML entities or stray backslashes—AAPT2 treats malformed escapes as build-breaking.

## Testing Guidelines
- Place unit tests in `app/src/test/java`, instrumentation tests in `app/src/androidTest/java`. Mirror the main package structure for clarity.
- Name test classes `<Feature>Test` (unit) or `<Feature>InstrumentedTest` (instrumented) and assert behaviour rather than implementation details.
- Aim for coverage on core game logic (AI evaluation, board moves) before UI; include edge cases around localisation and configuration changes.

## Commit & Pull Request Guidelines
- Follow the existing history’s imperative, concise commit style (e.g. `Expand localization support`, `bump: prepare v0.1.2`). Keep subject lines under ~72 characters and add body text for context when needed.
- Each pull request should describe the change, link related issues, and include screenshots or recordings for UI adjustments. Mention required testing (`assembleDebug`, `lint`, `test`) and highlight localisation updates if strings changed.
- Keep branches focused: one feature/fix per PR. Rebase on latest `main` before requesting review and resolve merge conflicts locally.

## Localisation Checklist
- When adding or editing text, update `values/strings.xml` first, then propagate to every `values-*/strings.xml` variant.
- Run `./gradlew lint` to catch missing translations and placeholders, and spot-check the merged resource file under `app/build/intermediates/` if AAPT2 reports escape errors.
- While editing French or Italian strings, double-check the file remains UTF-8 encoded and that multi-line entries use `\n` rather than literal line breaks inside CDATA.

# Chinese Checker (Kotlin/Compose, Offline)

This repository delivers an offline Chinese Checkers experience for Android built with Kotlin and Jetpack Compose, organized around clear domain, AI, and UI layers tuned for native Compose (no Flutter dependency).

## Highlights
- Pure Kotlin + Compose UI with no network requirements.
- Core game model: hex coordinates, board generation (121 nodes), legal steps and multi-jumps.
- Multi-player board logic (2/3/4/6). Player-to-camp mapping and per-player goal camp (opposite triangle).
- Offline config covers per-seat Human/AI (with difficulty) and color selections, plus a resume-from-save flow.
- Compose Canvas board rendering with tap interaction, victory overlay, haptics and tone feedback.
- Localisation spans 18 languages, now including Arabic, Bahasa Indonesia, Bahasa Melayu, and Thai alongside existing releases.

## Project Structure
- app/src/main/java/com/yoyicue/chinesechecker/
  - `game/` – hex math, board topology, Weak/Greedy/Smart bots.
  - `data/` – DataStore settings plus Room repositories (profile, stats, save slot).
  - `ui/` – navigation root, Compose screens, and theming utilities.

## Build
- Uses AGP 8.7.2, Kotlin 1.9.25, and Compose BOM 2024.02.02.
- Open this folder in Android Studio (Giraffe+ recommended) and sync; `gradlew` is checked in for CLI builds.
- Command-line build: `./gradlew :app:assembleDebug`.

## Notes
- Initial scope targets Human (Player A, north) vs AI (Player B, south) but supports 2/3/4/6 players.
- Board generation: center hex radius = 4 plus 6 arms length = 4, total 121 nodes (standard Chinese Checkers).
- Win condition: a player wins when all their pieces occupy the opponent's home triangle.
- Rendering is Compose-first; tone/haptic feedback and confetti overlay highlight wins.

## Next Steps
- Tailor SFX volume handling (respect the slider in real time) and expose music toggle when available.
- Expand AI benchmarking and add unit tests around board/jump generation.
- Polish accessibility: TalkBack labels for board cells and configuration dropdowns.
- Automate Room schema generation path (single `app/schemas` output) and add migration tests.

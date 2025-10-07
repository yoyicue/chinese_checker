# Chinese Checker (Kotlin/Compose, Offline)

This repository delivers an offline Chinese Checkers experience for Android built with Kotlin and Jetpack Compose, organized around clear domain, AI, and UI layers tuned for native Compose (no Flutter dependency).

## Highlights
- Pure Kotlin + Compose UI with no network requirements.
- Core game model: hex coordinates, board generation (121 nodes), legal steps and multi-jumps.
- Multi-player board logic (2/3/4/6). Player-to-camp mapping and per-player goal camp (opposite triangle).
- Offline config supports 2/3/4/6 players, per-seat Human/AI (with difficulty), and per-seat color. In-game UI currently shows current player and renders with chosen colors.
- Compose Canvas board rendering with tap interaction.

## Project Structure
- app/src/main/java/com/yoyicue/chinesechecker/
  - `game/Hex.kt` – cube hex coordinates and helpers.
  - `game/Board.kt` – board topology, pieces, legal moves, win check.
  - `game/AI.kt` – Weak and Greedy bots.
  - `ui/` – `AppRoot`, `GameScreen`, and theme.

## Build
- Uses AGP 8.7.2 and Kotlin 1.9.25 (matching reference `com.lay.bricks`).
- Open this folder in Android Studio (Giraffe+ recommended) and build.
- Or run via Gradle if available locally:
  - `./gradlew :app:assembleDebug` (wrapper not included; Android Studio can generate wrapper if needed).

## Notes
- Initial scope targets Human (Player A, north) vs AI (Player B, south).
- Board generation: center hex radius=4 plus 6 arms of length=4, total 121 nodes (standard Chinese Checkers).
- Win condition: a player wins when all their pieces occupy the opponent's home triangle.
- Rendering and interaction are intentionally simple; no external assets or audio yet.

## Next Steps
- Settings (music/volume/haptics/theme) and persistence via DataStore/SharedPreferences.
- Additional screens: Start/Offline config, Profiles, How-To-Play.
- Multiple AIs and difficulties; optional Minimax.
- Sound effects (SoundPool) and haptic feedback integration.
- Wire multi-player UI (3/4/6 players) using existing Board support.

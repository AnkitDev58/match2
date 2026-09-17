# Implementation Plan - Match Up Emoji Memory Game

Build a 2-player local pass-and-play memory matching game using Kotlin Multiplatform and Compose Multiplatform.

## User Review Required

> [!IMPORTANT]
> The game logic will be implemented in `commonMain` to ensure platform independence and testability. The UI will also be shared using Compose Multiplatform.

## Proposed Changes

### [Data Models]

Create data classes and enums to represent the game state.

#### [NEW] [CardState.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/model/CardState.kt)
Represents a single card's state (id, emoji, flipped status, matched status).

#### [NEW] [Player.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/model/Player.kt)
Enum for Player A and Player B.

#### [NEW] [GameState.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/model/GameState.kt)
Holds the entire game state, including cards, scores, current player, and game status.

---

### [Logic]

Implement the game engine using a ViewModel.

#### [NEW] [MemoryGameViewModel.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/logic/MemoryGameViewModel.kt)
Handles card clicks, matching logic with delays, shuffling, and state updates using `StateFlow`.

---

### [UI Components]

Shared Compose components for the game.

#### [NEW] [CardComposable.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/ui/CardComposable.kt)
Individual card UI with flip animation.

#### [NEW] [ScoreBoard.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/ui/ScoreBoard.kt)
Displays scores and current turn info.

#### [MODIFY] [App.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/App.kt)
Main entry point for the shared UI, hosting the game board and controls.

---

### [Tests]

#### [NEW] [MemoryGameViewModelTest.kt](file:///D:/KMP/Match%202/shared/src/commonTest/kotlin/org/example/project/logic/MemoryGameViewModelTest.kt)
Unit tests for game logic (shuffling, matching, turn passing, game over).

## Verification Plan

### Automated Tests
- Run `shared:commonTest` to verify game logic.
  - Test initial state.
  - Test matching logic (same player goes again).
  - Test non-matching logic (turn passes, delay works).
  - Test game over condition.

### Manual Verification
- Deploy to Android emulator/device and verify:
  - Cards flip on tap.
  - Matches stay face-up.
  - Mismatches flip back after delay.
  - Scores update correctly.
  - "New Game" resets everything.
  - Animations are smooth.

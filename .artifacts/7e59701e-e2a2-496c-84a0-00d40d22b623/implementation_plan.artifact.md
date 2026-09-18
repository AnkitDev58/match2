# Implementation Plan - Unify Trap Grid UI with Match Up

This plan aims to align the UI of the **Trap Grid** game with the **Match Up** game, utilizing common components like `ScoreBoard`, `SetupScreen`, and adding flip animations to the cards.

## User Review Required

> [!IMPORTANT]
> The `Trap Grid` setup will now include a "Content Mode" selection (Emoji, Number, Color) to match `Match Up`, even though `Trap Grid` previously only used numbers. This provides a more consistent experience across both games.

> [!NOTE]
> `Trap Grid` has additional game phases (`SecretPick`, `Handoff`) that `Match Up` doesn't have. These will be styled to fit the new unified layout.

## Proposed Changes

### 1. Common UI Components

#### [MODIFY] [SetupScreen.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/ui/SetupScreen.kt)
- Make `SetupScreen` reusable by adding `title` and `buttonText` parameters.
- Default them to "Game Setup" and "Start Match Up!" respectively.

#### [NEW] [GameCard.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/ui/GameCard.kt)
- Create a generic `GameCard` component that handles the flip animation and styling.
- This will be used by both `MatchUpScreen` and `TrapGridScreen`.
- It will support `CardContent` and different states (revealed, matched, etc.).

### 2. Trap Grid State & ViewModel

#### [MODIFY] [TrapGridState.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/trapgrid/TrapGridState.kt)
- Update `GameState` to use `GridSize` enum and `ContentMode`.
- Add `scoreA` and `scoreB` to `GameState` (can be used for total wins or just for turn display).
- Update `Cell` to use `CardContent` instead of just a number.
- Update `reduce` function to handle the new `GridSize` and `ContentMode` during initialization.

#### [MODIFY] [TrapGridViewModel.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/trapgrid/TrapGridViewModel.kt)
- Add `startGame` and `goToSetup` methods similar to `MatchUpViewModel`.
- Update `dispatch` calls to handle the new state structure.

### 3. Trap Grid Screen Layout

#### [MODIFY] [TrapGridScreen.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/trapgrid/TrapGridScreen.kt)
- Refactor the main layout to use `Surface` with `safeContentPadding`.
- Integrate the common `SetupScreen` for the setup phase.
- Use `ScoreBoard` in the `Playing` phase.
- Update `BoardGrid` to use the new `GameCard` with flip animations.
- Add "Restart" and "Settings" buttons at the bottom of the `Playing` and `GameOver` screens.
- Style `SecretPickScreen` and `HandoffScreen` to be consistent with the overall theme.

### 4. Match Up Screen Refactor

#### [MODIFY] [MatchUpScreen.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/matchup/MatchUpScreen.kt)
- Update to use the new generic `GameCard` component instead of the local `CardComposable`.

## Verification Plan

### Automated Tests
- Run existing tests for `MatchUp` and `TrapGrid` logic to ensure no regressions in game rules.
- (Optional) Add a new test for `TrapGrid` to verify content generation with different `ContentMode`s.

### Manual Verification
- Deploy to Android/Desktop.
- Verify that `TrapGrid` setup screen looks like `MatchUp`.
- Verify that `TrapGrid` cards flip when revealed.
- Verify that `ScoreBoard` correctly shows turns in `TrapGrid`.
- Verify that the layout is consistent (padding, buttons, colors).

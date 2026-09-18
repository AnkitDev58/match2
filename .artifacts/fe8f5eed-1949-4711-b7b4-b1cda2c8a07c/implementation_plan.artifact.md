# Architectural Restructuring: DI, Navigation, and Presenter Pattern

This plan outlines the steps to introduce a more structured architecture into the Match 2 KMP project, including Dependency Injection (DI), type-safe Navigation, a `BaseViewModel`, and a `presenter`-based package organization.

## User Review Required

> [!IMPORTANT]
> - **Navigation Strategy**: We will implement a custom, type-safe `Navigator` class that uses a `Screen` sealed interface. This avoids string-based routes and is fully multiplatform-safe.
> - **DI Strategy**: We will use a lightweight `AppModule` pattern for manual dependency injection. This is highly portable across Android, Desktop, and Web targets without introducing complex DI framework overhead.
> - **Presenter Structure**: Business logic and UI components will be moved into screen-specific packages under a `presenter` directory (e.g., `presenter.home`, `presenter.matchup`).

## Proposed Changes

### [Base & Infrastructure]

#### [NEW] [BaseViewModel](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/base/BaseViewModel.kt)
Create a generic `BaseViewModel<State, Event>` to standardize how UI state and one-time events are handled.

#### [NEW] [Navigator](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/navigation/Navigator.kt)
Implement the class-based, type-safe navigation system.

#### [NEW] [AppModule](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/di/AppModule.kt)
Create a central dependency provider to manage instances of ViewModels and other services.

---

### [Home Screen]

#### [NEW] [HomeViewModel](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/home/HomeViewModel.kt)
A ViewModel for the Home screen to manage the list of available games.

#### [NEW] [HomeScreen](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/home/HomeScreen.kt)
A screen displaying the list of available games (currently just "Match Up").

---

### [Match Up Screen]

#### [MODIFY] [MemoryGameViewModel](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/logic/MemoryGameViewModel.kt) -> [MatchUpViewModel](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/matchup/MatchUpViewModel.kt)
Refactor and move the existing `MemoryGameViewModel` to the `presenter/matchup` package, inheriting from `BaseViewModel`.

#### [NEW] [MatchUpScreen](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/presenter/matchup/MatchUpScreen.kt)
Move the game-specific UI logic from `App.kt` to its own dedicated Screen composable.

---

### [App Integration]

#### [MODIFY] [App.kt](file:///D:/KMP/Match%202/shared/src/commonMain/kotlin/org/example/project/App.kt)
Simplify `App.kt` to handle the top-level `MaterialTheme` and the main `Navigator` entry point.

## Verification Plan

### Automated Tests
- Build all targets (Android, Desktop) to ensure architectural changes don't break the multiplatform build.
- (Optional) Refactor existing `MemoryGameViewModelTest` to `MatchUpViewModelTest`.

### Manual Verification
- Launch the app and verify the new Home Screen appears first.
- Navigate to the "Match Up" game from the Home Screen.
- Ensure the game functions as before (flipping cards, scoring, settings).
- Verify the "Back" functionality works through the Navigator.

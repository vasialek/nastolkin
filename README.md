# nastolkin

## Game logic

The `games` Kotlin/JVM library contains the classes for Захват Колоний under
`com.nastolkin.games.zakhvatkoloniy`. Each game's models, repositories, logic,
and presentation state stay in its own package. The `app` module adds the Android
Compose screens, the game drawer, and light/dark/system themes. It supports
Android 8.0 (API 26) and later.

Run the unit tests with JDK 21 installed:

```sh
./gradlew :games:test
```

The Gradle wrapper pins and verifies Gradle 8.14.3. The library targets JVM 17
bytecode and uses AndroidX ViewModel and Kotlin StateFlow.

## Android app

Install JDK 21 and Android SDK platform 35 / build tools 35.0.0. Set `ANDROID_HOME`
to the SDK directory, or use Android Studio's local SDK configuration.

```sh
./gradlew :app:testDebugUnitTest :games:test :app:lintDebug :app:assembleDebug
```

The debug APK is `app/build/outputs/apk/debug/app-debug.apk`. The app starts with
the game list. Select Захват Колоний to open the city picker, or use the hamburger
menu to switch games and choose a theme. The header reads the version from the
APK build configuration. Interface text is available in English and Russian.

UI tests run as local Robolectric unit tests; no emulator is required. They cover
navigation, visibility, theme controls, and Russian text. Theme selection survives
activity recreation but is not yet saved as a permanent preference.

## Calling from Android UI

Construct the dependencies in the app's ViewModel factory:

```kotlin
CityViewModel(RandomCityProvider(CityRepositoryMemory()))
```

Obtain `CityViewModel` through Android's ViewModel provider (for example Compose's
`viewModel` with a factory), rather than constructing it on every recomposition.
This retains the selected city and visibility across configuration changes.
Process-death persistence is not implemented.

Collect `cityViewModel.state` with `collectAsStateWithLifecycle()` and render it.
The button callbacks contain only these calls:

```kotlin
onClick = { cityViewModel.selectRandomCity() }
onClick = { cityViewModel.toggleCityVisibility() }
```

A selection immediately reveals the result; tapping the display hides or reveals
the same city. Toggling before selection does nothing. Each city has an equal
chance on each selection, and consecutive repeats are allowed.

`ICityRepository` supplies data, and `IRandomCityProvider` selects a city. The
memory repository preserves all 11 historical names exactly, in every locale.
UI labels and accessibility text use Android string resources; no localized UI
messages are stored in the game logic classes.

An empty repository causes `NoSuchElementException` without changing the previous
screen state. This is a data configuration error; the supplied memory repository
is nonempty. Tests inject repository/provider implementations with a `Mock`
suffix and a controlled Kotlin random source.

## Arkham Horror campaign journals

Open Arkham Horror from the game list. The common `CampaignScreen` supplies
investigators, campaign notes, and retired investigators for every campaign.
`CampaignRepositoryMemory` registers the available campaigns and any extra
sections. The Drowned City intentionally has only the standard sections.
The Feast of Hemlock Vale adds explored locations, the three-day tracker, and
residents with shared party trust from 0 to 6 and collapsible notes.

Campaign models and the ViewModel live under `games/.../arkhamhorror`; Android
screens and Room storage live in the corresponding `app/.../arkhamhorror` package.
The ViewModel depends on `ICampaignJournalStorage`, not on Room. Each campaign
has one record keyed by its stable ID. Room stores an atomic JSON snapshot of the
journal, including inactive investigators, in the private `arkham-journals.db`.
The exported Room schema is checked into `app/schemas`; future database changes
must supply migrations rather than destructive resets.

Only added investigators are displayed, with at most four active investigators
per campaign. Edit opens a form for remaining XP, trauma, and story cards.
Retirement requires confirmation and retains the investigator's record. Duplicate
identities are matched after trimming and ignoring case, including retired ones.
There is no card catalogue yet: a card number and its character name cannot be
recognized as the same investigator, so use a consistent identifier.

Notes, checkboxes, and trust changes save automatically. Investigator forms save
with the Save button. The save indicator confirms persistence; a failed write
keeps the edits in memory and offers Retry. A failed read never replaces the
stored journal with an empty one. Data survives app restarts, but export, cloud
sync, and the chaos bag are not implemented. Clearing app data or uninstalling
removes the journals. Android updates must use the same signing key to preserve
the existing installation; ephemeral CI debug keys are not a release strategy.

Local unit tests cover journal rules, save failures/retries, Room database
reopening, independent campaigns, and the Compose screens. The new Compose tests
use Robolectric native graphics to exercise Android text layout faithfully.

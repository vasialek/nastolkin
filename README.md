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

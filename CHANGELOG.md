# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
- Add support for Android 12
- Update to use latest LeakCanary

## [0.9.7] - 2022-04-11

- Add support for gesture navigation (you can now swipe on the top edge of the screen to open the debug drawer)
- Hopefully fix sources not getting uploaded to Maven Central
- Add `DebugDrawer.openIn()` and `closeIn()` functions
- Consolidate LeakCanary modules back into one, now supporting `2.1`
- Add heap dump toggle switch to LeakCanary module
- Fix appearance of button in LeakCanary module

## [0.9.6]

- Add support for window insets on all edges (great for apps supporting edge-to-edge).
- Remove the need to call `LumberYard.install()` when using the Timber module.
- Remove the `timber-no-op` module; there's no code that needs to be included in release builds now.
- Add temporary LeakCanary2 module to make use of it while it's in beta.
- Fix bug where resource IDs would display as errors in Android Studio.
- Migrate deployment to Maven Central.

## [0.9.5]

- Add pretty printing JSON option in HTTP logger module

## [0.9.4]

- Fix crash on older versions of Android (at least on API 21 and 22)
- Fix state restoration of drawer and scroll view
- Bump various dependencies

## [0.9.3]

- Fix bug where Timber logs were not sharing
- Fix Timber log FileProvider [potentially clashing](https://commonsware.com/blog/2017/06/27/fileprovider-libraries.html) with consuming application

## [0.9.2]

- Add OkHttp log interceptor module
- Bump various dependencies, including AndroidX to the new stable release

## [0.9.1]

- Fix drawer not building correctly when no custom container provided in builder.

## [0.9.0]

Initial release of DebugDrawer, including modules for
 - Device info: Things like resolution and API level.
 - LeakCanary: Hides the launcher icon and provides access in the drawer.
 - Retrofit: Allows for changing endpoints and configuring mock behaviour.
 - Timber: Collects and displays Timber logs in a dialog.

# Yapbam update process

This document describes how Yapbam checks for, downloads, and installs updates.

## Overview

```
CheckNewReleaseAction / CheckUpdateDialog
        |  (update available + user accepts)
        v
InstallUpdateDialog.DownloadSwingWorker
        |  downloads update.zip + updater.jar into <dataDir>/update/
        |  MainFrame.updater = <dataDir>/update/updater.jar
        |  user clicks "Quit now"
        v
MainFrame.windowClosing
        |  launches: java -jar <dataDir>/update/updater.jar
        v
Updater.main()  (in a separate JVM)
        |  extracts update.zip into the launch directory
        |  deletes <dataDir>/update/
        |  shows success/error dialog
```

## Step 1: Checking for updates

### Trigger

Update checks are triggered by `CheckNewReleaseAction`:

- **Automatic**: `doAutoCheck(Window)` is called at startup. It checks whether
  enough time has elapsed since the last check (based on user preferences) or
  whether the current release is too old (> 30 days since last check). If so,
  `CheckUpdateDialog.check(owner, true, forced)` is called.
- **Manual**: the user selects "Check for updates" in the Help menu, which
  calls `actionPerformed(ActionEvent)` → `CheckUpdateDialog.check(owner, false, false)`.

### Beta channel trick

If the user holds **Shift** while clicking "Check for updates" in the menu,
a dialog appears asking to choose between the **Release** and **Beta** channels.

This is implemented in `CheckNewReleaseAction.actionPerformed` by testing
`ActionEvent.SHIFT_MASK` (same pattern as the About dialog in `MainMenuBar`).

The chosen channel is passed to `CheckUpdateDialog.check(owner, auto, forced, beta)`,
which passes it to `VersionManager.getUpdateInformation(beta)`.

`VersionManager` has two URLs:
- Release: `http://yapbam.sourceforge.net/updateInfo.php`
- Beta: `http://yapbam.sourceforge.net/updateInfoBeta.php`

The old `BetaUpdating` system property is no longer used.

### Server response

The server returns a `.properties` file (loaded via `UpdateInformation`) containing:

| Property | Description |
|----------|-------------|
| `serialNumber` | A serial number stored in Yapbam state |
| `lastestRelease` | The latest available version (see [Version format](#version-format) below) |
| `updateURL` | URL of the download page (for manual download) |
| `autoUpdateURL` | URL of the `update.zip` file |
| `autoUpdateCHKSUM` | Checksum of `update.zip` |
| `autoUpdateSize` | Size of `update.zip` in bytes |
| `autoUpdateUpdaterURL` | URL of the `updater.jar` file |
| `autoUpdateUpdaterCHKSUM` | Checksum of `updater.jar` |
| `autoUpdateUpdaterSize` | Size of `updater.jar` in bytes |

The request URL also carries analytics parameters (version, country, language,
OS, Java version, portable mode, JNLP, serial number) appended by
`ApplicationContext.toURL()`. This information is used for analytics purposes
and can be useful to return a different update package based on the user's
configuration (typically if the user uses a specific Java version or operating
system not supported by the latest release).

### Version comparison

`CheckUpdateDialog` compares the latest release with the current version using
`ReleaseInfo.compareTo()`. If the latest release is newer, the user is offered
to install it (or it is installed automatically if auto-install is enabled and
the install directory is writable).

## Step 2: Downloading the update

`InstallUpdateDialog.DownloadSwingWorker.doInBackground()`:

1. Deletes and recreates `Portable.getUpdateFileDirectory()` (usually
   `<dataDir>/update/`).
2. Downloads `update.zip` from `autoUpdateURL` into `<dataDir>/update/update.zip`.
3. Verifies its checksum against `autoUpdateCHKSUM`.
4. Downloads `updater.jar` from `autoUpdateUpdaterURL` into
   `<dataDir>/update/updater.jar`.
5. Verifies its checksum against `autoUpdateUpdaterCHKSUM`.
6. If either checksum fails, the user is offered to retry (manual mode) or the
   download is retried automatically (auto mode).

On success, `MainFrame.updater` is set to the `updater.jar` file path, and the
user is invited to close Yapbam to apply the update.

## Step 3: Installing the update

### Launching the updater

When the main window closes (`MainFrame.windowClosing`), if `MainFrame.updater`
is set and the file exists, Yapbam launches a **separate JVM**:

```
java -jar <dataDir>/update/updater.jar
```

The launching JVM consumes the child's stderr in a blocking `readLine()` loop
to stay alive until the update completes. **stdout is not consumed at all.**

> **Important**: the updater must **never** write to stdout or stderr.
> Writing to stdout can fill the OS pipe buffer (~4 KB on Windows) and deadlock
> the process. See [yapbam-updater/README.md](../yapbam-updater/README.md) for
> details.

### What the updater does

`Updater.main()` (in the `yapbam-updater` project):

1. Opens a log file (`updater.log` in the launch directory).
2. Reads `update.zip` from `Portable.getUpdateFileDirectory()`.
3. Extracts each entry into `Portable.getLaunchDirectory()` (the installation
   directory):
   - Directories are created (replacing any file with the same name).
   - Files are written (replacing any directory with the same name).
   - If a target file is **locked** (e.g. `Yapbam.exe` still running on
     Windows), it is renamed to `<name>.old` before writing the new file.
     Windows allows renaming a running executable but not overwriting it.
   - Entries ending with `.sh` are made executable.
4. On success, deletes the log file and shows a success dialog.
5. On failure, the exception stack trace remains in `updater.log` and an error
   dialog is shown.
6. Deletes the temporary update directory.

### Why a separate JVM?

Changing a jar (or exe) on the fly while the JVM is running can cause serious
problems. On Windows, running executables and jars are locked by the OS and
cannot be overwritten. The two-phase approach (download while running, install
after closing via a separate process) avoids these issues.

## Version format

### Source

The version is defined in the Maven `pom.xml` (`<version>` tag) and the build
date is generated by the `buildnumber-maven-plugin`.

### Template

`filteredResources/net/yapbam/update/version.txt`:

```
version=${project.version} (${build.date})
```

Maven filters this at build time, replacing `${project.version}` and
`${build.date}` (format `dd/MM/yyyy`). The result is packaged in the jar at
`/net/yapbam/update/version.txt`.

### Runtime reading

`ApplicationContext.getVersion()` reads `version.txt` as a `.properties` file
and constructs a `ReleaseInfo` from the `version` property.

### Format

```
<major>.<minor>.<build>[.<preReleaseComment>] (<day>/<month>/<year>)
```

Examples:
- `0.21.6 (27/08/2026)`
- `0.21.7.beta (27/08/2026)`

### Comparison

`ReleaseInfo.compareTo()` compares in this order:
1. `majorRevision`
2. `minorRevision`
3. `buildId`
4. `releaseDate` (as a tie-breaker)

The `preReleaseComment` (e.g. `.beta`) is **not** used in comparisons. A
`0.21.7.beta` with the same date as `0.21.7` would compare as equal (in this case the release is used as a tie-breaker).

## Key classes

| Class | Project | Role |
|-------|---------|------|
| `CheckNewReleaseAction` | yapbam | Menu action, auto-check at startup, Shift trick |
| `CheckUpdateDialog` | yapbam | Dialog showing update search progress |
| `InstallUpdateDialog` | yapbam | Dialog showing download progress |
| `MainFrame` | yapbam | Launches the updater on window close |
| `VersionManager` | yapbam | Returns the update info URL (release or beta) |
| `UpdateInformation` | yapbam | Parses the server response |
| `ReleaseInfo` | yapbam | Version parsing and comparison |
| `ApplicationContext` | yapbam | Reads the current version from `version.txt` |
| `Portable` | yapbam / yapbam-updater | Resolves launch and data directories |
| `Updater` | yapbam-updater | Extracts `update.zip` into the install directory |
| `Log` | yapbam-updater | File logger for update diagnostics |

## Testing auto-update before deployment

The safer way is to [deploy](deliveryProcess.html) the update to the beta channel and use the **Shift + "Check for updates"** trick to select the beta
channel, and publish a beta on the beta update info URL.

# The Yapbam desktop application

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=yapbam_yapbam&metric=alert_status)](https://sonarcloud.io/dashboard?id=yapbam_yapbam)
[![DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/yapbam/yapbam)

Yapbam — **Yet Another Bank Account Manager** — is a simple but powerful personal account manager.
It lets you manage your bank accounts, track transactions, build reports and budgets, and synchronize your data
across machines (e.g. via Dropbox). It is written in Java and runs on any system with a Java Runtime Environment.

# Developer notes
### FAQ

* I try to compile the project in Eclipse, but Eclipse reports compilation errors.
This project uses the Lombok library.
In order to have Eclipse recognize the annotations used by this library, you should install it in Eclipse as explained [here](https://howtodoinjava.com/automation/lombok-eclipse-installation-examples/).

* How is the Windows executable `Yapbam.exe` built?
The executable is generated with [Launch4j](https://launch4j.sourceforge.net/), using the configuration file
[launch4JDefinitionFiles/yapbam.xml](launch4JDefinitionFiles/yapbam.xml). Launch4j can be run from its GUI, but also
from the command line with the `launch4jc.exe` binary, which is convenient for build scripts:

  ```
  "C:\Program Files (x86)\Launch4j\launch4jc.exe" launch4JDefinitionFiles\yapbam.xml
  ```

  The `headerType` node in the XML configuration controls the kind of executable produced:
  - `gui` (recommended for Yapbam) builds a Windows GUI application that starts without opening a console window;
  - `console` builds a console application that opens a command prompt on startup (useful for debugging, as it
    displays `stdout`/`stderr`).

  When `headerType=gui`, the standard output and error streams are not visible to the user. To diagnose a problem
  on a user's machine, ask them to open a command prompt and run:

  ```
  cd yapbam
  java -jar ./App/program.jar > log.txt 2>&1
  ```

  This captures both `stdout` and `stderr` into `log.txt`, which the user can then send for analysis — this keeps
  the installation folder clean (no log files are written to disk unless explicitly requested).

### You want to provide us with a pull request. Thank you :-)

Nevertheless here are some advices:
- Read [The (written) unwritten guide to pull requests](https://www.atlassian.com/blog/git/written-unwritten-guide-pull-requests).
- Read [How to Split Pull Requests - Good Practices, Methods and Git Strategies](https://www.thedroidsonroids.com/blog/splitting-pull-request)
- Please don't forget to implement JUnit tests of the code you propose.  
I agree JUnit testing of Swing components is not easy. But for non GUI code, it is mandatory.
- Please use [Sonar](https://www.sonarqube.org/) to check your code's quality (with the standards rules). Use the same code formatting as you see in the sources of this project (its quite common).  
If you're using Eclipse, the [SonarLint](https://www.sonarlint.org/eclipse) plugin will help you. A submission that fails Sonar quality gate could be refused, even if it works well ... Remember, you write the code once, the maintenance team will support it a long time.
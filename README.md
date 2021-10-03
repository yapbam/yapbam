# The Yapbam desktop application

Work in progress

# Developer notes
You want to provide us with a pull request. Thank you :-)

Nevertheless here are some advices:
- Read [The (written) unwritten guide to pull requests](https://www.atlassian.com/blog/git/written-unwritten-guide-pull-requests).
- Read [How to Split Pull Requests - Good Practices, Methods and Git Strategies](https://www.thedroidsonroids.com/blog/splitting-pull-request)
- Please don't forget to implement JUnit tests of the code you propose.  
I agree JUnit testing of Swing components is not easy. But for non GUI code, it is mandatory.
- Please use [Sonar](https://www.sonarqube.org/) to check your code's quality (with the standards rules). Use the same code formatting as you see in the sources of this project (its quite common).  
If you're using Eclipse, the [SonarLint](https://www.sonarlint.org/eclipse) plugin will help you. A submission that fails Sonar quality gate could be refused, even if it works well ... Remember, you write the code once, the maintenance team will support it a long time.
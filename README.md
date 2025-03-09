# Team Explorer Everywhere Cross-platform Command-line Client for Team Foundation Server
This project contains:
- Cross-platform Command-line Client for Team Foundation Server and Team Services
- Team Foundation Server/Team Services SDK for Java

The purpose of this client is mainly to support [Azure DevOps Plugin for IntelliJ][azure-devops-intellij].

## What is the Command-line Client for TFS?
The CLC for TFS allows you run version control commands from a console/terminal window against a TFS server on any operating system.
This tool is for use with Team Foundation Version Control (TFVC), a centralized version control system.
If you prefer to use Git, you can use any Git client with TFS or Team Services as well.

## Where Can I Get The Command-line Client?
Download the TEE-CLC-*.zip file in the [Releases](https://github.com/JetBrains/team-explorer-everywhere/releases) area of this repo.

## Building
### Install Java 8
1. We use Java 8 as the minimal supported Java execution environment.
2. Download and install the JDK for [Java 8][adoptopenjdk]
3. Set the JAVA_HOME environment variable to point to the install, e.g.
   * (Windows) `SET JAVA_HOME=C:\dev\java\jdk8u192-b12`
   * (Linux) `JAVA_HOME=~/dev/java/jdk8u192-b12`
   * (Mac) `JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_60.jdk/Contents/Home`
4. Add JAVA_HOME bin directory to the path
   * (Windows) `SET PATH=%JAVA_HOME%\bin;%PATH%`
   * (Linux) `PATH=$JAVA_HOME/bin:$PATH`
   * (Mac) `PATH=$JAVA_HOME/bin:$PATH`

### Build

Run the following shell command:
```console
$ ./gradlew build
```

Build results can be found in `build\output\clc` and `build\output\sdk`.

Test results are available in `build\output\bin\testresults`.

## Contributing
We welcome pull requests. Please fork this repo and send us your contributions.
See [Contributing][docs.contributing] for details.

## Localization / Translation
Your language, your words, your plug-in for you!

Along with open-sourced Team Explorer Everywhere (TEE) source code, we are making it possible for anyone to contribute translations in your native language. With these changes, you can now improve existing translated resources, translate updated resources, or even provide new language support TEE did not have before. Your contribution will be part of the TEE Plug-in in your language for everyone to use. We highly appreciate your efforts, and we welcome your feedback and suggestions on the TEE community localization process. Your contribution could be in next release!

Please click [Localization](./Localization.md) for details on how to contribute in TEE community translation effort. Feel free to contact [us](mailto:kevinli@microsoft.com) if you have any questions.

**Happy contributing!**

[adoptopenjdk]: https://adoptopenjdk.net/
[azure-devops-intellij]: https://github.com/microsoft/azure-devops-intellij
[docs.contributing]: CONTRIBUTING.md

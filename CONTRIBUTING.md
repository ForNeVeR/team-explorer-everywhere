# TEE Contributor Guide
This instructions below will help you get your development environment setup so that you can contribute to this repository.

## Ways to Contribute
Interested in contributing to the team-explorer-everywhere project? There are plenty of ways you can contribute, all of which help make the project better.
* Submit a [bug report](https://github.com/JetBrains/team-explorer-everywhere/issues/new) through the Issue Tracker 
* Review the [source code changes](https://github.com/JetBrains/team-explorer-everywhere/pulls).
* Submit a code fix for a bug 
* Submit a [feature request](https://github.com/JetBrains/team-explorer-everywhere/issues/new) 

## Java Requirements
We use Java 8 as the minimal supported Java execution environment. 

You can find the JDK downloads on [AdoptOpenJDK web site][adoptopenjdk].

## Testing

Our Integration tests are in the `integrationTests` folder. In order to run them correctly, you have to set up the environment and have an Azure DevOps organization to run against.

You'll need to add a test project into your account, and add a TFVC repository into it. TFVC repository should include a `readme.txt` file.

Here are the steps to setup your environment:
1. Enable the following environment variables:
    * `TEE_VSO_SERVER_URL=https://organization.visualstudio.com` (make sure no trailing slash here)
    * `TEE_VSO_TEAM_PROJECT=projectName`
    * `TEE_VSO_USER=EmailAddressForUser`
    * `TEE_VSO_PASS=PersonalAccessTokenGeneratedFromTheUserSecurityPage`

   _Note_: Do not use https://dev.azure.com/account/ addresses in these environment variables, make sure to use https://account.visualstudio.com/

[adoptopenjdk]: https://adoptopenjdk.net/
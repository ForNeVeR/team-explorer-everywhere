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

Testing
-------
### Create the Test Environment
To run the integration tests, you'll need a (free) Azure account.

1. [Register on Azure DevOps][azure.free] (as of 2025-03-10, this is free but will require bank card verification).
2. Go through the instruction on [how to create an organization][azure.organization].
3. In the organization settings (`https://dev.azure.com/<organization>/_settings/organizationOverview`), **disable** the checkbox **Use the new URL**.

   This will switch your organization from URL `https://dev.azure.com/<organization>/` to the legacy scheme `https://<organization>.visualstudio.com/`.
4. In the **Repositories** settings (`https://<organization>.visualstudio.com/_settings/repositories`), **disable** the checkbox **Disable creation of TFVC repositories**.

   Thus you will enable yourself to create new TFVC repositories in said organization.
5. Create a new project in your organization. When creating a project, expand the **Advanced** settings and choose **Version control**: **Team Foundation Version Control**.
6. In your project's repository (`https://dev.azure.com/<organization>/<project>/_versionControl`), create a new file `readme.txt` manually. Leave it with the empty content, press **Check in**.

### Set Up the Test Parameters
Our Integration tests are in the `integrationTests` folder.
Tests will use the aforementioned account, and will use the data from the environment variables to access the account.

First, you'll need a personal access token.

Go to the user's **Personal Access Tokens** page in your organization
(`https://dev.azure.com/<organization>/_usersSettings/tokens`),
and create one.
Make sure to give the token the **Full access** to the organization.

Then, set up the following environment variables:
- `TEE_SERVER_URL=https://<organization>.visualstudio.com` (make sure no trailing slash here),
- `TEE_TEAM_PROJECT=<project>`,
- `TEE_USER=<your-user@email.com>`,
- `TEE_PASS=<personal-access-token>`.

_Note_: Do not use https://dev.azure.com/account/ addresses in these environment variables, make sure to use https://account.visualstudio.com/

Then, run tests via the following shell command:
```console
$ ./gradlew :integrationTests:integrationTest
```

[adoptopenjdk]: https://adoptopenjdk.net/
[azure.free]: https://azure.microsoft.com/en-us/free/
[azure.organization]: https://learn.microsoft.com/en-us/azure/devops/organizations/accounts/create-organization?view=azure-devops

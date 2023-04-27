# Derp Server
Derp Company website 

[![Release](https://github.com/DerpCompany/DerpServer/actions/workflows/Release.yml/badge.svg)](https://github.com/DerpCompany/DerpServer/actions/workflows/Release.yml)

![Code Coverage](.github/badges/jacoco.svg)

Backend code written in Kotlin, with Sprint Boot.

Frontend code written in Typescript, with Angular.

# Getting Started

## Prerequisites for the Backend
- Configure your GitHub account to [use ssh authentication](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent).
### For the Backend
- Download and install [MongoDB](https://www.mongodb.com/try/download/community). We will use this for local development and testing.
- Install [JDK 17](https://www.oracle.com/java/technologies/downloads/).
- Install [IntelliJ 2022.1.3](https://www.jetbrains.com/idea/download/) or later.
### For the Frontend
- Install [Visual Studio Code](https://code.visualstudio.com/). 
- Install the [Angular CLI](https://angular.io/cli).

## Configuring your dev environment

To ease with development, here are some tools that you may want to install:
- [IntelliJ Detekt Plugin](https://plugins.jetbrains.com/plugin/10761-detekt)
- [IntelliJ OpenApi Editor Plugin](https://plugins.jetbrains.com/plugin/14837-openapi-swagger-editor)

### Discord Integration

This server can also host discord bots. This means that for running the server, you will need to provide an API token. 

You can follow the instructions here for how to create a Bot application and how to get the Token.
https://discordpy.readthedocs.io/en/stable/discord.html

Once you have a token, KEEP IT SAFE. Do NOT PUT THE CODE INTO THE CODE/REPO. In order to be able to use the token you
will pass it as environment variable when running the process. The way to do this in IntelliJ is to update your
run configurations and add the token as an env variable, for example `SHUFFLE_BOT_TOKEN=YOUR TOKEN`

#### Discord Testing

To test changes for Discord you will need to follow some more steps to ensure your bot is ready to be called.

First of all you will need to add the bot to a discord server. For this, make sure you have a server to test your changes.
To add the bot to the server, you will use a URL to invite the bot. This URL will give specify the permissions granted to 
the bot. 

- Go to https://discord.com/developers/applications
- Select your application
- Oauth2 -> URL Generator
- Grant the following permissions:
  - Scopes: `Bot`
  - Bot Permissions: `Manage Channels`, `Read Messages/View Channel`, `Send Messages`, `Move Member`.
- The URL will be at the bottom, open that URL.
- Select the server where you will be testing your bot.
- Continue -> Review Changes -> Authorize

## Common problems

### IntelliJ is not able to resolve dependencies

If you see that Spring classes are not being resolved by IntelliJ and the `External Libraries` panel displays no 3rd party
dependencies, you may need to reimport your project. Try the following.

- Close IntelliJ
- Run the following commands:
  - `./gradlew --stop`
  - `rm -r .idea/`
  - `rm -r ~/.m2/`
  - `rm -r ~/.gradle/.tmp/ ~/.gradle/caches/`
- Launch IntelliJ and open the project again. Now the classes should show up correctly.

## Launching the server

For more detailed on how to configure and launch the server, go to the [installation](INSTALL.md) page.
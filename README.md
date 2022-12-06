# Derp_Site
Derp Company website 

[![Java CI with Gradle](https://github.com/DerpCompany/Derp_Site/actions/workflows/CI.yml/badge.svg)](https://github.com/DerpCompany/Derp_Site/actions/workflows/CI.yml)

![Coverage](.github/badges/jacoco.svg)


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

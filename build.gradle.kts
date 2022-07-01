
plugins {
    id("org.springframework.boot") version "3.0.0-SNAPSHOT" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.7.0" apply false
    kotlin("multiplatform") version "1.7.0" apply false
    kotlin("plugin.spring") version "1.7.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.20.0" apply false
}

subprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}
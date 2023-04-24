import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("io.gitlab.arturbosch.detekt")
}

group = "com.derpcompany.server-integration"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    testImplementation(project(":network-models"))
    testImplementation(project(":server"))

    implementation("dev.kord:kord-core:0.8.0-M17")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true // preconfigure defaults
    config = files(
        "$rootDir/config/detekt.yml",
        "$projectDir/config/detekt.yml",
    ) // point to your custom config defining rules to run, overwriting default behavior

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events(PASSED, FAILED, STANDARD_OUT, STANDARD_ERROR, SKIPPED)
    }
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
    }
}
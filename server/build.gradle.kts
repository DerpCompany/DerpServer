import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("io.gitlab.arturbosch.detekt")
    id("org.openapi.generator")
}

group = "com.derpcompany.server"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(project(":network-models"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
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

// Configure the tasks for OpenAPI
val openApiSpec = "$rootDir/specs/openapi.yaml"
openApiValidate {
    inputSpec.set(openApiSpec)
}

// Validating a single specification
openApiGenerate {
    generatorName.set("markdown")
    inputSpec.set(openApiSpec)
    outputDir.set("$rootDir/generated/openapi-spec")
}
tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}
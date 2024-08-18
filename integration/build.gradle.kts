val ktorVersion:String by project
@Suppress("PropertyName")
val kotlin_version: String by project
val mockkVersion: String by project
val ktorClientMockVersion: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.serialization")
    id("io.ktor.plugin") version "2.3.9"
}

group = "test.integration"

dependencies {
    testImplementation(project(":applications:data-collector-server"))
    testImplementation(project(":applications:data-analyzer-server"))
    testImplementation(project(":components:data-collector"))

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.ktor:ktor-client-mock:${ktorClientMockVersion}")

}

tasks.named("shadowJar") {
    enabled = false
}

plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22" apply false
    id("com.google.protobuf") version "0.9.4" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    if (listOf("applications", "components", "support", "integration").contains(name)) return@subprojects

    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.slf4j:slf4j-api:2.0.7")
        implementation("org.slf4j:slf4j-simple:2.0.7")

        testImplementation(kotlin("test-junit"))
    }

    tasks.register("stage") {
        dependsOn("clean", "build")
    }
}
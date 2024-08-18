plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "io.initialcapacity.collector"

val ktorVersion: String by project
val mockkVersion: String by project
val ktorClientMockVersion: String by project

dependencies {
    implementation(project(":components:data-collector"))
    implementation(project(":components:data-analyzer"))
    implementation(project(":support:workflow-support"))

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-freemarker-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.ktor:ktor-client-mock:${ktorClientMockVersion}")
}

task<JavaExec>("start") {
    classpath = files(tasks.jar)
}

tasks {
    jar {
        manifest { attributes("Main-Class" to "io.initialcapacity.collector.AppKt") }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map(::zipTree)
        })
    }
}
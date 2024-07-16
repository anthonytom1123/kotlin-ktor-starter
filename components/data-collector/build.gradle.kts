val ktorVersion: String by project

plugins {
//    id("com.google.protobuf")
}

//sourceSets {
//    main {
//        proto {
//            srcDir("src/main/proto")
//            include("**/*.protodevel")
//        }
//    }
//    test {
//        proto {
//            // In addition to the default "src/test/proto"
//            srcDir("src/test/protocolbuffers")
//        }
//    }
//}

//protobuf {
//    protoc {
//        artifact = "com.google.protobuf:protoc:3.0.0"
//    }
//}

dependencies {
    implementation(project(":support:workflow-support"))
    implementation(project(":components:data-analyzer"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-encoding:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-protobuf:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "kotlin-ktor-starter"

include(
    "applications:basic-server",
    "applications:data-analyzer-server",
    "applications:data-collector-server",

    "components:data-collector",
    "components:data-analyzer",
    "components:metrics-support",

    "integration",

    "support:workflow-support"

)
include("components:metrics-support")
findProject(":components:metrics-support")?.name = "metrics-support"

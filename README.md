# Kotlin ktor starter

An [application continuum](https://www.appcontinuum.io/) style example using Kotlin and Ktor
that includes a single web application with two background workers.

* Basic web application
* Data analyzer
* Data collector

### Technology stack

This codebase is written in a language called [Kotlin](https://kotlinlang.org) that is able to run on the JVM with full
Java compatibility.
It uses the [Ktor](https://ktor.io) web framework, and runs on the [Netty](https://netty.io/) web server.
HTML templates are written using [Freemarker](https://freemarker.apache.org).
The codebase is tested with [JUnit](https://junit.org/) and uses [Gradle](https://gradle.org) to build a jarfile.
The data is fetched from [511.org](https://511.org/).

## Getting Started

1. Open Docker.

1. Build with Docker.

    ```bash
    docker build -t kotlin-ktor-starter .
    ```

1.  Run with docker.

    ```bash
    docker run -e PORT=8881 -p 8881:8881 kotlin-ktor-starter
    ```

That's a wrap for now.

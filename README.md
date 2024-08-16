# Kotlin ktor starter

A web application using Kotlin and Ktor that includes a single web application, two background workers, and a postgres database. Data collector fetches data from [511.org](https://511.org/) and sends the data to Data Analyzer. Data Analyzer then parses through it and stores it in a postgres database. The web application calls Data Analyzer to read data from the database and then display the data to the user.

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

1.  Run with docker compose.

    ```bash
    docker-compose up --build
    ```

That's a wrap for now.

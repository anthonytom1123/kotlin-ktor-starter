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

1. Open Docker. There should already be a .jar file in application/basic-server/build/libs, application/data-analyzer-server/build/libs, and application/data-collector-server/build/libs.

1. Build with Docker.

    ```bash
    docker build -t kotlin-ktor-starter .
    ```

1.  Run with docker compose.

    ```bash
    docker-compose up --build
    ```

You will be able to access the data through the kotlin-ktor-server at [http://localhost:8888/](http://localhost:8888/) once all the containers are running.
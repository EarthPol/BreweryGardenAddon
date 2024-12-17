plugins {
    id("java")
}

group = "dev.jsinco.brewery"
version = "BX3.4.5-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.jsinco.dev/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Include the latest release of BreweryX instead of this version.
    compileOnly("com.dre.brewery:BreweryX:3.4.5-SNAPSHOT#2")

    // We need to include our own copy of whatever server software we're writing against!
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}
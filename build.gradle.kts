plugins {
    id("java")
}

group = "dev.jsinco.brewery.garden"
version = "BX3.4.5-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.jsinco.dev/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.dre.brewery:BreweryX:3.4.5-SNAPSHOT#4")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}
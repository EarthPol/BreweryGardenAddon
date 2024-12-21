import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    id("java")
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
    id("com.modrinth.minotaur") version "2.8.7"
}

group = "dev.jsinco.brewery.garden"
version = "BX3.4.5"

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

hangarPublish {
    publications.register("plugin") {
        version.set(project.version.toString())
        channel.set("Release")
        id.set(project.name)
        apiKey.set(System.getenv("HANGAR_TOKEN"))
        platforms {
            register(Platforms.PAPER) {
                jar.set(tasks.jar.flatMap { it.archiveFile })
                platformVersions.set(listOf("1.21.x"))
            }
        }
        changelog.set(readChangeLog())
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set(project.name) // This can be the project ID or the slug. Either will work!
    versionNumber.set(project.version.toString())
    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    uploadFile.set(tasks.jar)
    loaders.addAll("bukkit", "spigot", "paper", "purpur", "folia")
    gameVersions.addAll("1.21.4")
    changelog.set(readChangeLog())
}

fun readChangeLog(): String {
    val text: String = System.getenv("CHANGELOG") ?: file("CHANGELOG.md").run {
        if (exists()) readText() else "No Changelog found."
    }
    return text.replace("\${version}", project.version.toString())
}
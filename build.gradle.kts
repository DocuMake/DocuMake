import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform") version "2.0.21"

    `maven-publish`
}

val v = "0.1.0"

group = "xyz.calcugames.documake"
version = if (project.hasProperty("snapshot")) "$v-SNAPSHOT" else v

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvm()

    js {
        generateTypeScriptDefinitions()
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        generateTypeScriptDefinitions()
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmWasi {
        nodejs()
    }

    mingwX64()
    linuxX64()
    linuxArm64()
    macosArm64()
    macosX64()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

tasks {
    clean {
        delete("kotlin-js-store")
    }
}

publishing {
    publications {
        getByName<MavenPublication>("kotlinMultiplatform") {
            pom {
                name = "LevelZ Kotlin API"
                description = "The Kotlin API for LevelZ"
                url = "https://levelz.calcugames.xyz"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/LevelZ-File/kotlin-bindings.git"
                    developerConnection = "scm:git:ssh://github.com/LevelZ-File/kotlin-bindings.git"
                    url = "https://github.com/LevelZ-File/kotlin-bindings"
                }
            }
        }
    }

    repositories {
        maven {
            credentials {
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_PASSWORD")
            }

            val releases = "https://repo.calcugames.xyz/repository/maven-releases/"
            val snapshots = "https://repo.calcugames.xyz/repository/maven-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshots else releases)
        }
    }
}
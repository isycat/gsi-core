plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"
    `maven-publish`
}

group = "com.github.isycat"
version = "1.0.0"

val ktorVersion = "3.0.0"
val coroutinesVersion = "1.8.1"

repositories {
    mavenCentral()
    google()
    maven("https://jitpack.io")
}

dependencies {
    // Use JitPack dependency for steam-utils
    api("com.github.isycat:steam-utils:v1.0.0")
    
    // Ktor server for GSI
    api("io.ktor:ktor-server-core:$ktorVersion")
    api("io.ktor:ktor-server-cio:$ktorVersion")
    api("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    
    // Serialization
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    
    // Coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.mockk:mockk:1.13.8")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.isycat"
            artifactId = "gsi-core"
            version = project.version.toString()
            
            from(components["java"])
            
            pom {
                name.set("GSI Core")
                description.set("Generic Game State Integration framework for any Source engine game")
                url.set("https://github.com/isycat/gsi-core")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("isycat")
                        name.set("isycat")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/isycat/gsi-core.git")
                    developerConnection.set("scm:git:ssh://github.com/isycat/gsi-core.git")
                    url.set("https://github.com/isycat/gsi-core")
                }
            }
        }
    }
}

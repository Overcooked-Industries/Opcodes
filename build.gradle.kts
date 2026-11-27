plugins {
    id("java")
}

group = "de.overcooked_industries"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.11.1")
}

tasks.test {
    useJUnitPlatform()
}
plugins {
    id("java")
}

group = "de.overcooked_industries"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.9.1")
}

tasks.test {
    useJUnitPlatform()
}
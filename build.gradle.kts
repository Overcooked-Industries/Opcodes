plugins {
    id("java")
}

group = "de.overcooked_industries"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.ow2.asm:asm:9.9.1")
}

tasks.test {
    useJUnitPlatform()
}
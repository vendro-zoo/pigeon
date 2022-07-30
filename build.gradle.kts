import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vers: String by project

plugins {
    kotlin("jvm") version "1.7.10"
    id("maven-publish")  // Used to publish to the local maven repository
    id("org.jetbrains.dokka") version "1.6.21"  // Used to generate the API documentation
}

group = "it.zoo.vendro"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.0.3")
    implementation("io.ktor:ktor-server-netty:2.0.3")
    implementation("io.ktor:ktor-server-status-pages:2.0.3")

    implementation("it.zoo.vendro:racoon:master-e8de0c3")

    testImplementation(kotlin("test"))
}

// Function to build source jar file
val sourcesJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    archiveVersion.set(vers)
    from(sourceSets["main"].allSource)
}

// Function to build javadoc jar file
val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn("dokkaJavadoc")
    archiveClassifier.set("javadoc")
    archiveVersion.set(vers)
    from(tasks.javadoc)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group as String
            artifactId = "pigeon"
            version = vers

            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}
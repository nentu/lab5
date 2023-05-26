plugins {
    id("lab5.app-conventions")
}

group = "ru.bardinpetr.itmo.lab5.server"

application.mainClass.set("$group.Main")

tasks.register<Jar>("fatJar") {
    manifest.attributes["Main-Class"] = application.mainClass
    archiveClassifier.set("fat")
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) } })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


dependencies {
    implementation(project(":models"))
    implementation(project(":common"))
    implementation(project(":network"))
    implementation(project(":db"))

    implementation("org.postgresql:postgresql:42.6.0")
    implementation("commons-cli:commons-cli:1.5.0")
    testImplementation("com.fasterxml.jackson.core:jackson-annotations:${Deps.jacksonVersion}")
}


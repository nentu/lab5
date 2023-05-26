plugins {
    id("lab5.java-conventions")
}

group = "ru.bardinpetr.itmo.lab5.client"

dependencies {
    implementation(project(":models"))
    implementation(project(":common"))
    implementation(project(":network"))

    implementation("commons-cli:commons-cli:1.5.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Deps.jacksonVersion}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Deps.jacksonVersion}")

    implementation("jline:jline:3.0.0.M1")
}

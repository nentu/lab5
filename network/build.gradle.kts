plugins {
    id("lab5.app-conventions")
}

group = "ru.bardinpetr.itmo.lab5.network"

dependencies {
    implementation(project(":common"))
    implementation(project(":models"))

    implementation("io.jsonwebtoken:jjwt-api:${Deps.jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${Deps.jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${Deps.jjwtVersion}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${Deps.jacksonVersion}")
}

plugins {
    id("lab5.java-conventions")
}

group = "ru.bardinpetr.itmo.lab5.db"

dependencies {
    implementation(project(":common"))
    implementation(project(":models"))
    implementation("org.postgresql:postgresql:42.6.0")
}


plugins {
    id("lab5.java-conventions")
    id("application")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
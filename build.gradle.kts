// Top-level build file

buildscript {
    repositories {
        google()        // 🔹 Esto es clave
        mavenCentral()  // 🔹 Esto también
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

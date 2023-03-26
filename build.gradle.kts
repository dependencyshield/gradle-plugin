plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("dependencyShieldPlugin") {
            id = "com.dependencyshield.gradle"
            implementationClass = "com.dependencyshield.gradle.DependencyShieldPlugin"
        }
    }
}



repositories {
    mavenCentral()
}
java.sourceCompatibility = JavaVersion.VERSION_11



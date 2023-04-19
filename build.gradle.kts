plugins {
    `java-gradle-plugin`
    signing
    id("com.gradle.plugin-publish") version "1.1.0"
}


group = "com.dependencyshield"
version = "0.0.6"

dependencies {
    implementation("org.owasp:dependency-check-gradle:8.2.1")
}

gradlePlugin {
    website.set("https://www.dependencyshield.com")
    vcsUrl.set("https://github.com/dependencyshield/gradle-plugin")
    plugins {
        create("dependencyShieldPlugin") {
            id = "com.dependencyshield.gradle"
            displayName = "Plugin for Dependency Shield"
            description = "A plugin that cofigures OWASP dependency check plugin to download suppression file from Dependency Shield and upload the reports."
            implementationClass = "com.dependencyshield.gradle.DependencyShieldPlugin"
            tags.set(listOf("dependencyshield", "security", "vulnerabilities", "owasp", "dependency-check"))
        }
    }
}

signing {
    useGpgCmd()
}


publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}


repositories {
    mavenCentral()
}
java.sourceCompatibility = JavaVersion.VERSION_11



Dependency Shield Gradle plugin
===============================

A plugin that cofigures OWASP dependency check plugin to download suppression file from Dependency Shield and upload the reports.

Consists of two tasks:
* [ConfigureDependencyCheck](src/main/kotlin/com/dependencyshield/gradle/ConfigureDependencyCheck.kt) - configures OWASP dependency check plugin to download suppression file from Dependency Shield and generate JSON report.
* [UploadDependencyCheckReport](src/main/kotlin/com/dependencyshield/gradle/UploadDependencyCheckReport.kt) - uploads the report to Dependency Shield.

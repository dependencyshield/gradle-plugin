Dependency Shield Gradle plugin
===============================

A plugin that cofigures OWASP dependency check plugin to download suppression file from Dependency Shield and upload the reports.

Consists of two tasks:
* [ConfigureDependencyCheck](src/main/java/com/dependencyshield/gradle/ConfigureDependencyCheck.java) - configures OWASP dependency check plugin to download suppression file from Dependency Shield and generate JSON report.
* [UploadReport](src/main/java/com/dependencyshield/gradle/UploadReport.java) - uploads the report to Dependency Shield.

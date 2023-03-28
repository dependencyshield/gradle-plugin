package com.dependencyshield.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

public class DependencyShieldPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        DependencyShieldExtension extension = target.getExtensions().create("dependencyShield", DependencyShieldExtension.class);
        TaskProvider<UploadReport> uploadReport = target.getTasks().register("uploadReport", UploadReport.class, extension.getOrganizationId(), extension.getConfigurationId(), extension.getApiKey(), extension.getUriPrefix());
        uploadReport.configure(task -> task.getReportsDirectory().set(target.file(target.getBuildDir() + "/reports/")));

        TaskProvider<ConfigureDependencyCheck> configureDepCheck = target.getTasks().register("configureDependencyCheck", ConfigureDependencyCheck.class, extension.getOrganizationId(), extension.getConfigurationId(), extension.getApiKey());

        target.getTasks().named("dependencyCheckAnalyze").configure(task -> {
            task.finalizedBy(uploadReport);
            task.dependsOn(configureDepCheck);
        });
        target.getTasks().named("dependencyCheckAggregate").configure(task -> {
            task.finalizedBy(uploadReport);
            task.dependsOn(configureDepCheck);
        });
    }
}

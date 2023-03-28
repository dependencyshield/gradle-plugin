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
        target.getTasks().named("dependencyCheckAnalyze").configure(task -> task.finalizedBy(uploadReport));
        target.getTasks().named("dependencyCheckAggregate").configure(task -> task.finalizedBy(uploadReport));
//        DependencyCheckExtension dependencyCheckExtension = target.getExtensions().getByType(DependencyCheckExtension.class);
//        dependencyCheckExtension.setFormats(Arrays.asList("HTML", "JSON"));
//        dependencyCheckExtension.setSuppressionFiles(List.of("https://suppression.dependencyshield.com/" + extension.getOrganizationId().get() + "/" + extension.getConfigurationId().get() + "/" + extension.getApiKey().get() + "/suppression.xml"));
    }
}

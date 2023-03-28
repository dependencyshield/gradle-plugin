package com.dependencyshield.gradle;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;

import javax.inject.Inject;
import java.util.List;

public abstract class ConfigureDependencyCheck extends DefaultTask {
    private final Property<String> organizationId;
    private final Property<String> configurationId;
    private final Property<String> apiKey;


    @Inject
    public ConfigureDependencyCheck(Property<String> organizationId, Property<String> configurationId, Property<String> apiKey) {
        this.organizationId = organizationId;
        this.configurationId = configurationId;
        this.apiKey = apiKey;
    }

    /**
     * Configures the dependency check plugin.
     */
    @TaskAction
    public void configureDependencyCheck()  {
        Action<DependencyCheckExtension> action = ext -> {
            ext.setSuppressionFiles(List.of(getSuppressionUrl()));
            ext.setFormats(List.of("HTML", "JSON"));
        };
        getProject().getExtensions().configure("dependencyCheck", action);
    }

    @NotNull
    private String getSuppressionUrl() {
        return "https://suppression.dependencyshield.com/" + organizationId.get() + "/" + configurationId.get() + "/" + apiKey.get() + "/suppression.xml";
    }
}


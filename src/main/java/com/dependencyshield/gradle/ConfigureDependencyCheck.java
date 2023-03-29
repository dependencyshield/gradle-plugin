package com.dependencyshield.gradle;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.HTML;
import static org.owasp.dependencycheck.reporting.ReportGenerator.Format.JSON;

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
            ext.setSuppressionFiles(append(ext.getSuppressionFiles(), List.of(getSuppressionUrl())));
            ext.setFormats(append(ext.getFormats(), List.of(HTML.toString(), JSON.toString())));
        };
        getProject().getExtensions().configure("dependencyCheck", action);
    }

    private <T> List<T> append(Collection<T> original, Collection<T> newElements) {
        Set<T> set = new HashSet<>(original);
        set.addAll(newElements);
        return new ArrayList<>(set);
    }

    @NotNull
    private String getSuppressionUrl() {
        return "https://suppression.dependencyshield.com/" + organizationId.get() + "/" + configurationId.get() + "/" + apiKey.get() + "/suppression.xml";
    }
}


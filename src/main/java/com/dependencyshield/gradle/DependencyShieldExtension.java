package com.dependencyshield.gradle;

import org.gradle.api.provider.Property;

public interface DependencyShieldExtension {
    Property<String> getOrganizationId();
    Property<String> getConfigurationId();
    Property<String> getApiKey();
    Property<String> getUriPrefix();
}

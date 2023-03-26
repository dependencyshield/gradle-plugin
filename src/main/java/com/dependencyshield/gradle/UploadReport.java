package com.dependencyshield.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;

public abstract class UploadReport extends DefaultTask {
    private final Property<String> organizationId;
    private final Property<String> configurationId;
    private final Property<String> apiKey;
    private final Property<String> uriPrefix;

    @InputDirectory
    abstract DirectoryProperty getReportsDirectory();


    @Inject
    public UploadReport(Property<String> organizationId, Property<String> configurationId, Property<String> apiKey, Property<String> uriPrefix) {
        this.organizationId = organizationId;
        this.configurationId = configurationId;
        this.apiKey = apiKey;
        this.uriPrefix = uriPrefix;
    }

    @TaskAction
    public void greet() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
            .newBuilder(URI.create(uriPrefix() + "/upload?organizationId="+organizationId.get()+"&configurationId="+configurationId.get()+"&apiKey="+apiKey.get()))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(getReportsDirectory().get() + "/dependency-check-report.json")))
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Upload failed: " + response.body());
        }
    }

    private String uriPrefix() {
        return uriPrefix.getOrElse("https://app.dependencyshield.com");
    }
}


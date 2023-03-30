package com.dependencyshield.gradle;

import com.dependencyshield.gradle.util.GZIPCompressingInputStream;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
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

    /**
     * Uploads the report to Dependency Shield.
     */
    @TaskAction
    public void uploadReport() throws IOException, InterruptedException {
        Path path = Paths.get(getReportsDirectory().get() + "/dependency-check-report.json");

        if (!path.toFile().exists()) {
            getLogger().error("JSON dependency check report {} not found, most likely due to depedency check plugin failing to run", path);
        }

        try (InputStream in = new GZIPCompressingInputStream(new FileInputStream(path.toFile()))) {
            HttpRequest request = HttpRequest
                .newBuilder(URI.create(uriPrefix() + "/upload?organizationId=" + organizationId.get() + "&configurationId=" + configurationId.get() + "&apiKey=" + apiKey.get()))
                .header("Content-Type", "application/json")
                .header("Content-Encoding", "gzip")
                .POST(HttpRequest.BodyPublishers.ofInputStream(() -> in))
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("Upload failed: " + response.body());
            }
            getLogger().warn("Report uploaded, it's available at {}/organizations/{}/configurations/{}/reports", uriPrefix(), organizationId.get(), configurationId.get());
        }
    }

    private String uriPrefix() {
        return uriPrefix.getOrElse("https://app.dependencyshield.com");
    }
}


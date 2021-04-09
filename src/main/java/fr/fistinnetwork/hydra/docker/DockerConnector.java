package fr.fistinnetwork.hydra.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import fr.fistinnetwork.hydra.Hydra;
import fr.fistinnetwork.hydra.util.logger.LogType;

public class DockerConnector {

    private final DockerClientConfig config;
    private final ApacheDockerHttpClient httpClient;
    private final DockerClient dockerClient;

    public DockerConnector(Hydra hydra, String url) {
        this.config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(url).build();
        this.httpClient = new ApacheDockerHttpClient.Builder().dockerHost(this.config.getDockerHost()).sslConfig(this.config.getSSLConfig()).build();
        this.dockerClient = DockerClientImpl.getInstance(this.config, this.httpClient);

        hydra.getLogger().log(LogType.INFO, String.format("Successfully connected to Docker ! (Socket: %s)", url));
    }

    public DockerConnector(Hydra hydra) {
        this(hydra, DockerUrl.get().getUrl());
    }

    public DockerClientConfig getConfig() {
        return this.config;
    }

    public ApacheDockerHttpClient getHttpClient() {
        return this.httpClient;
    }

    public DockerClient getDockerClient() {
        return this.dockerClient;
    }

}

package fr.fistinnetwork.hydra.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;

public class DockerConnector {
    private final DockerClientConfig config;
    private final ApacheDockerHttpClient httpClient;
    private final DockerClient dockerClient;

    public DockerConnector(String url) {
        this.config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(url).build();
        this.httpClient = new ApacheDockerHttpClient.Builder().dockerHost(this.config.getDockerHost()).sslConfig(this.config.getSSLConfig()).build();
        this.dockerClient = DockerClientImpl.getInstance(this.config, this.httpClient);
    }

    public DockerConnector() {
        this("unix:///var/run/docker.sock");
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

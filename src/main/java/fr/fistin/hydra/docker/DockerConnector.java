package fr.fistin.hydra.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;

public class DockerConnector {
    private DockerClientConfig config = null;
    private ApacheDockerHttpClient httpClient = null;
    private DockerClient dockerClient = null;

    public DockerConnector (String url){
        this.config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(url).build();
        this.httpClient = new ApacheDockerHttpClient.Builder().dockerHost(this.config.getDockerHost()).sslConfig(this.config.getSSLConfig()).build();
        this.dockerClient = DockerClientImpl.getInstance(this.config, this.httpClient);
    }

    public DockerConnector () {
        this("unix:///var/run/docker.sock");
    }

    public DockerClientConfig getConfig() {
        return config;
    }

    public ApacheDockerHttpClient getHttpClient() {
        return httpClient;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }
}

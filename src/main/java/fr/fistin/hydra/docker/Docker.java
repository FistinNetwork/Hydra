package fr.fistin.hydra.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import fr.fistin.hydra.docker.image.DockerImageManager;
import fr.fistin.hydra.docker.network.DockerNetworkManager;
import fr.fistin.hydra.docker.swarm.DockerSwarm;
import fr.fistin.hydra.util.References;

public class Docker {

    /** Manager */
    private final DockerImageManager imageManager;
    private final DockerNetworkManager networkManager;
    private final DockerSwarm swarm;

    /** Config and Client */
    private final DockerClientConfig config;
    private final ApacheDockerHttpClient httpClient;
    private final DockerClient dockerClient;

    public Docker() {
        final String url = DockerUrl.get().getUrl();

        this.config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(url)
                .build();
        this.httpClient = new ApacheDockerHttpClient.Builder().dockerHost(this.config.getDockerHost()).sslConfig(this.config.getSSLConfig()).build();
        this.dockerClient = DockerClientImpl.getInstance(this.config, this.httpClient);
        this.imageManager = new DockerImageManager(this);
        this.networkManager = new DockerNetworkManager(this);
        this.swarm = new DockerSwarm(this);

        System.out.println(References.NAME + " is now connected to Docker with url: " + url + ".");
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

    public DockerImageManager getImageManager() {
        return this.imageManager;
    }

    public DockerNetworkManager getNetworkManager() {
        return this.networkManager;
    }

    public DockerSwarm getSwarm() {
        return this.swarm;
    }

}

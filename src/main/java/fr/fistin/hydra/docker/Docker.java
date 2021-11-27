package fr.fistin.hydra.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.docker.container.DockerContainerManager;
import fr.fistin.hydra.docker.image.DockerImageManager;
import fr.fistin.hydra.docker.network.DockerNetworkManager;
import fr.fistin.hydra.docker.swarm.DockerSwarmManager;
import fr.fistin.hydra.util.References;

public class Docker {

    /** Manager */
    private final DockerContainerManager containerManager;
    private final DockerImageManager imageManager;
    private final DockerNetworkManager networkManager;
    private final DockerSwarmManager swarmManager;

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
        this.containerManager = new DockerContainerManager(this);
        this.imageManager = new DockerImageManager(this);
        this.networkManager = new DockerNetworkManager(this);
        this.swarmManager = new DockerSwarmManager(this);

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

    public DockerContainerManager getContainerManager() {
        return this.containerManager;
    }

    public DockerImageManager getImageManager() {
        return this.imageManager;
    }

    public DockerNetworkManager getNetworkManager() {
        return this.networkManager;
    }

    public DockerSwarmManager getSwarmManager() {
        return this.swarmManager;
    }

}

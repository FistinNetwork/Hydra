package fr.fistin.hydra.docker.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import fr.fistin.hydra.docker.image.DockerImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DockerContainer {

    private String name;

    private DockerImage image;

    private String hostname;

    private String id;

    private List<String> envs;

    private int publishedPort;
    private int targetPort;

    private boolean autoRemove;

    public DockerContainer(String name, DockerImage image) {
        this.name = name;
        this.image = image;
        this.envs = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public DockerContainer setName(String name) {
        this.name = name;
        return this;
    }

    public DockerImage getImage() {
        return this.image;
    }

    public DockerContainer setImage(DockerImage image) {
        this.image = image;
        return this;
    }

    public String getHostname() {
        return this.hostname;
    }

    public DockerContainer setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public DockerContainer addEnvs(String... envs) {
        this.envs.addAll(Arrays.asList(envs));
        return this;
    }

    public DockerContainer removeEnvs(String... envs) {
        this.envs.removeAll(Arrays.asList(envs));
        return this;
    }

    public List<String> getEnvs() {
        return this.envs;
    }

    public DockerContainer setEnvs(List<String> envs) {
        this.envs = envs;
        return this;
    }

    public int getPublishedPort() {
        return this.publishedPort;
    }

    public DockerContainer setPublishedPort(int publishedPort) {
        this.publishedPort = publishedPort;
        return this;
    }

    public int getTargetPort() {
        return this.targetPort;
    }

    public DockerContainer setTargetPort(int targetPort) {
        this.targetPort = targetPort;
        return this;
    }

    public boolean isAutoRemove() {
        return this.autoRemove;
    }

    public DockerContainer setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
        return this;
    }

    public String createContainerCmd(DockerClient dockerClient) {
        final Ports portsBindings = new Ports();
        portsBindings.bind(ExposedPort.tcp(this.publishedPort), Ports.Binding.bindPort(this.targetPort));

        final String container = dockerClient
                .createContainerCmd(image.getName() + DockerImage.DOCKER_IMAGE_TAG_SEPARATOR + image.getTag())
                .withName(this.name)
                .withHostName(this.hostname)
                .withEnv(this.envs)
                .withHostConfig(new HostConfig()
                        .withPortBindings(portsBindings)
                        .withAutoRemove(this.autoRemove)
                ).exec().getId();

        return this.id = container;
    }

}

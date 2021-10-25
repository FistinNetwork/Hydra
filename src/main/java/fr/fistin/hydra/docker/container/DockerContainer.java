package fr.fistin.hydra.docker.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.*;
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

    public DockerContainer(String name, DockerImage image) {
        this.name = name;
        this.image = image;
        this.envs = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DockerImage getImage() {
        return this.image;
    }

    public void setImage(DockerImage image) {
        this.image = image;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getId() {
        return this.id;
    }

    public void addEnvs(String... envs) {
        this.envs.addAll(Arrays.asList(envs));
    }

    public void removeEnvs(String... envs) {
        this.envs.removeAll(Arrays.asList(envs));
    }

    public List<String> getEnvs() {
        return this.envs;
    }

    public void setEnvs(List<String> envs) {
        this.envs = envs;
    }

    public int getPublishedPort() {
        return this.publishedPort;
    }

    public void setPublishedPort(int publishedPort) {
        this.publishedPort = publishedPort;
    }

    public int getTargetPort() {
        return this.targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
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
                        .withAutoRemove(true)
                ).exec().getId();

        return this.id = container;
    }

}

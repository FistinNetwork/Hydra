package fr.fistin.hydra.docker.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import fr.fistin.hydra.docker.Docker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerContainerManager {

    private final Map<String, DockerContainer> containers;

    private final DockerClient dockerClient;

    public DockerContainerManager(Docker docker) {
        this.dockerClient = docker.getDockerClient();
        this.containers = new HashMap<>();
    }

    public String runContainer(DockerContainer container) {
        this.dockerClient.startContainerCmd(container.createContainerCmd(this.dockerClient)).exec();

        final String containerId = container.getId();

        this.containers.put(containerId, container);

        return containerId;
    }

    public void stopContainer(DockerContainer container) {
        final String containerId = container.getId();

        this.dockerClient.stopContainerCmd(containerId).exec();

        this.containers.remove(containerId);
    }

    public void stopContainer(String containerId) {
        this.dockerClient.stopContainerCmd(containerId).exec();

        this.containers.remove(containerId);
    }

    public InspectContainerResponse inspectContainer(DockerContainer container) {
        return this.dockerClient.inspectContainerCmd(container.getId()).exec();
    }

    public Map<String, DockerContainer> getContainers() {
        return this.containers;
    }

    public List<Container> listContainers() {
        return this.dockerClient.listContainersCmd().exec();
    }

}

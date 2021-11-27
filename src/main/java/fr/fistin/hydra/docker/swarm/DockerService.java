package fr.fistin.hydra.docker.swarm;

import com.github.dockerjava.api.model.*;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;

import java.util.*;

public class DockerService {

    protected final String name;

    protected final DockerImage image;
    protected final DockerNetwork network;

    protected String hostname;

    protected List<String> envs;

    protected final Map<Integer, Integer> ports;
    protected final Map<String, String> labels;

    protected int publishedPort;
    protected int targetPort;

    public DockerService(String name, DockerImage image, DockerNetwork network) {
        this.name = name;
        this.image = image;
        this.network = network;
        this.envs = new ArrayList<>();
        this.ports = new HashMap<>();
        this.labels = new HashMap<>();
    }


    public String getName() {
        return name;
    }

    public DockerImage getImage() {
        return this.image;
    }

    public DockerNetwork getNetwork() {
        return this.network;
    }

    public DockerService addEnv(String env) {
        this.envs.add(env);
        return this;
    }

    public DockerService addEnv(String key, String value) {
        this.envs.add(key + "=" + value);
        return this;
    }

    public DockerService removeEnv(String env) {
        this.envs.remove(env);
        return this;
    }

    public List<String> getEnvs() {
        return this.envs;
    }

    public DockerService withEnvs(List<String> envs) {
        this.envs = envs;
        return this;
    }

    public DockerService addLabel(String name, String value) {
        this.labels.put(name, value);
        return this;
    }

    public DockerService removeLabel(String name) {
        this.labels.remove(name);
        return this;
    }

    public DockerService addPort(int local, int container) {
        this.ports.put(local, container);
        return this;
    }

    public DockerService removePort(int local, int container) {
        this.ports.remove(local, container);
        return this;
    }

    public Map<String, String> getLabels() {
        return this.labels;
    }

    public Map<Integer, Integer> getPorts() {
        return this.ports;
    }

    public DockerService withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public DockerService withPublishedPort(int publishedPort) {
        this.publishedPort = publishedPort;
        return this;
    }

    public DockerService withTargetPort(int targetPort) {
        this.targetPort = targetPort;
        return this;
    }

    public ServiceSpec toSwarmService() {
        final ContainerSpec containerSpec = new ContainerSpec()
                .withImage(this.image.getName() + DockerImage.DOCKER_IMAGE_TAG_SEPARATOR + this.image.getTag())
                .withHostname(this.hostname)
                .withEnv(this.envs);

        final EndpointSpec endpointSpec = new EndpointSpec()
                .withMode(EndpointResolutionMode.VIP)
                .withPorts(Collections.singletonList(new PortConfig()
                        .withProtocol(PortConfigProtocol.TCP)
                        .withPublishedPort(this.publishedPort)
                        .withTargetPort(this.targetPort)
                ));

        final TaskSpec taskSpec = new TaskSpec()
                .withContainerSpec(containerSpec)
                .withNetworks(Collections.singletonList(new NetworkAttachmentConfig().withTarget(this.network.getName())));

        return new ServiceSpec()
                .withName(this.name)
                .withTaskTemplate(taskSpec)
                .withEndpointSpec(endpointSpec)
                .withLabels(this.labels);
    }

}

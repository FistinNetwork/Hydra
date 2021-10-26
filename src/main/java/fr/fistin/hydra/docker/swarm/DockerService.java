package fr.fistin.hydra.docker.swarm;

import com.github.dockerjava.api.model.*;
import fr.fistin.hydra.docker.image.DockerImage;
import fr.fistin.hydra.docker.network.DockerNetwork;

import java.util.*;

public class DockerService {

    private final String name;

    private final DockerImage image;
    private final DockerNetwork network;

    private String hostname;

    private List<String> envs;

    private final Map<String, String> mount;
    private final Map<Integer, Integer> portMap;

    private int publishedPort;
    private int targetPort;

    public DockerService(String name, DockerImage image, DockerNetwork network) {
        this.name = name;
        this.image = image;
        this.network = network;
        this.envs = new ArrayList<>();
        this.mount = new HashMap<>();
        this.portMap = new HashMap<>();
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

    public void addEnv(String env) {
        this.envs.add(env);
    }

    public void addEnv(String key, String value) {
        this.envs.add(key + "=" + value);
    }

    public void removeEnv(String env) {
        this.envs.remove(env);
    }

    public List<String> getEnvs() {
        return this.envs;
    }

    public void setEnvs(List<String> envs) {
        this.envs = envs;
    }

    public void addMount(String local, String container) {
        this.mount.put(local, container);
    }

    public void removeMount(String local, String container) {
        this.mount.remove(local, container);
    }

    public void addPort(int local, int container) {
        this.portMap.put(local, container);
    }

    public void removePort(int local, int container) {
        this.portMap.remove(local, container);
    }

    public Map<String, String> getMount() {
        return mount;
    }

    public Map<Integer, Integer> getPortMap() {
        return portMap;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setPublishedPort(int publishedPort) {
        this.publishedPort = publishedPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
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
                .withEndpointSpec(endpointSpec);
    }

}

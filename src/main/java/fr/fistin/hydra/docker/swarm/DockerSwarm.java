package fr.fistin.hydra.docker.swarm;

import com.github.dockerjava.api.DockerClient;
import fr.fistin.hydra.docker.Docker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DockerSwarm {

    private final Map<String, DockerService> services;

    private final DockerClient dockerClient;

    public DockerSwarm(Docker docker) {
        this.dockerClient = docker.getDockerClient();
        this.services = new HashMap<>();
    }

    public void runService(DockerService service) {
        this.dockerClient.createServiceCmd(service.toSwarmService()).exec();

        this.services.put(service.getName(), service);
    }

    public void removeService(String name) {
        this.dockerClient.removeServiceCmd(name).exec();

        this.services.remove(name);
    }

    public Set<DockerService> getServices() {
        return new HashSet<>(this.services.values());
    }

}

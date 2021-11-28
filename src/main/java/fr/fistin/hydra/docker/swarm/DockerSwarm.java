package fr.fistin.hydra.docker.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.api.model.SwarmInfo;
import com.github.dockerjava.api.model.SwarmNode;
import fr.fistin.hydra.docker.Docker;

import java.util.*;

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

    public void inspectService(DockerService service) {
        this.dockerClient.inspectServiceCmd(service.getName()).exec();
    }

    public List<Service> listServices() {
        return this.dockerClient.listServicesCmd().exec();
    }

    public Set<DockerService> getServices() {
        return new HashSet<>(this.services.values());
    }

    public Swarm getSwarm() {
        return this.dockerClient.inspectSwarmCmd().exec();
    }

    public boolean isSwarmActive() {
        final SwarmInfo info = this.dockerClient.infoCmd().exec().getSwarm();

        if (info != null) {
            return info.getNodeAddr() != null;
        }
        return false;
    }

    public List<SwarmNode> listSwarmNodes() {
        return this.dockerClient.listSwarmNodesCmd().exec();
    }

}

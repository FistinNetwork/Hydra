package fr.fistin.hydra.docker.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.api.model.SwarmInfo;
import com.github.dockerjava.api.model.SwarmNode;
import fr.fistin.hydra.docker.Docker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DockerSwarmManager {

    private final Set<DockerService> services;

    private final DockerClient dockerClient;

    public DockerSwarmManager(Docker docker) {
        this.dockerClient = docker.getDockerClient();
        this.services = new HashSet<>();
    }

    public void runService(DockerService service) {
        this.dockerClient.createServiceCmd(service.toSwarmService()).exec();

        this.services.add(service);
    }

    public void removeService(DockerService service) {
        this.dockerClient.removeServiceCmd(service.getName()).exec();

        this.services.remove(service);
    }

    public List<Service> listServices() {
        return this.dockerClient.listServicesCmd().exec();
    }

    public Set<DockerService> getServices() {
        return this.services;
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

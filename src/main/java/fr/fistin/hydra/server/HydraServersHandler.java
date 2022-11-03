package fr.fistin.hydra.server;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.data.HydraEnv;
import fr.fistin.hydra.api.server.HydraServer;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 03/11/2022 at 09:56
 */
public class HydraServersHandler {

    public void startServer(HydraServer server) {
        final String name = server.getName();
        final Map<String, String> env = server.getOptions().asEnv();

        env.put("TYPE", "SPIGOT");
        env.put("VERSION", "1.8.8-R0.1-SNAPSHOT-latest");
        env.put("ONLINE_MODE", "FALSE");
        env.put("EULA", "TRUE");
        env.put("ENABLE_RCON", "FALSE");
        env.put("INIT_MEMORY", "512M");
        env.put("MAX_MEMORY", "4G");

        env.putAll(new HydraEnv(name, Hydra.get().getConfig().getRedis()).asMap());

        final List<EnvVar> envVars = env.entrySet().stream().map(entry -> new EnvVarBuilder()
                .withName(entry.getKey())
                .withValue(entry.getValue())
                .build()).toList();

        final PodBuilder podBuilder = new PodBuilder()
                .withNewMetadata().withName(name).addToLabels("app", name).endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withEnv(envVars)
                .withName(name)
                .withImage("itzg/minecraft-server:java8")
                .addNewPort()
                .withProtocol("TCP")
                .withContainerPort(25565).endPort().endContainer().endSpec();

        final ServiceBuilder serviceBuilder = new ServiceBuilder()
                .editOrNewMetadata().withName(name).and()
                .editOrNewSpec()
                .addToSelector("app", name)
                .addNewPort()
                .withProtocol("TCP")
                .withPort(25565)
                .withTargetPort(new IntOrString(25565)).endPort().endSpec();

        final KubernetesClient client = Hydra.get().getKubernetes().getClient();

        client.pods().resource(podBuilder.build()).create();
        client.services().resource(serviceBuilder.build()).create();
    }

    public void stopServer(String serverName) {
        try {
            final KubernetesClient client = Hydra.get().getKubernetes().getClient();

            client.pods().withName(serverName).delete();
            client.services().withName(serverName).delete();
        } catch (Exception e) {
            System.err.println(serverName + " cannot be deleted from Kubernetes. Error: " + e.getMessage());
        }
    }

}

package fr.fistin.hydra.proxy;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.api.protocol.data.HydraEnv;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.server.HydraServer;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 03/11/2022 at 09:56
 */
public class HydraProxiesHandler {

    public void startProxy(HydraProxy proxy) {
        final String name = proxy.getName();
        final Map<String, String> env = new HashMap<>();

        env.put("TYPE", "WATERFALL");
        env.put("HEALTH_USE_PROXY", "TRUE");
        env.put("INIT_MEMORY", "512M");
        env.put("MAX_MEMORY", "4G");
        env.put("ENABLE_RCON", "FALSE");

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
                .withImage("itzg/bungeecord:latest")
                .addNewPort()
                .withProtocol("TCP")
                .withContainerPort(25577).endPort().endContainer().endSpec();

        final ServiceBuilder serviceBuilder = new ServiceBuilder()
                .editOrNewMetadata().withName(name).and()
                .editOrNewSpec()
                .addToSelector("app", name)
                .addNewPort()
                .withProtocol("TCP")
                .withPort(25577)
                .withTargetPort(new IntOrString(25577)).endPort().endSpec();

        final KubernetesClient client = Hydra.get().getKubernetes().getClient();

        client.pods().resource(podBuilder.build()).create();
        client.services().resource(serviceBuilder.build()).create();
    }

    public void stopProxy(String proxyName) {
        try {
            final KubernetesClient client = Hydra.get().getKubernetes().getClient();

            client.pods().withName(proxyName).delete();
            client.services().withName(proxyName).delete();
        } catch (Exception e) {
            System.err.println(proxyName + " cannot be deleted from Kubernetes. Error: " + e.getMessage());
        }
    }

}

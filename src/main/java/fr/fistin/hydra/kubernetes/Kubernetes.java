package fr.fistin.hydra.kubernetes;

import fr.fistin.hydra.Hydra;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

/**
 * Created by AstFaster
 * on 03/11/2022 at 11:50
 */
public class Kubernetes {

    private final KubernetesClient client;

    public Kubernetes() {
        final Config config = new ConfigBuilder()
                .withNamespace(Hydra.get().getConfig().getKubernetes().getNamespace())
                .build();

        this.client = new KubernetesClientBuilder()
                .withConfig(config)
                .build();
    }

    public void stop() {
        this.client.close();
    }

    public KubernetesClient getClient() {
        return this.client;
    }

}

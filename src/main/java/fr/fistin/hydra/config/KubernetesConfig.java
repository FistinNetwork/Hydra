package fr.fistin.hydra.config;

/**
 * Created by AstFaster
 * on 03/11/2022 at 11:52
 */
public class KubernetesConfig {

    private String namespace;

    private KubernetesConfig() {}

    public KubernetesConfig(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return this.namespace;
    }

}

package fr.fistin.hydra.server.template;

public class HydraTemplate {

    private String name;
    private String confidentialityLevel;
    private TemplateStartingOptions startingOptions;
    private TemplateDependencies dependencies;
    private TemplateHydraOptions hydraOptions;

    public HydraTemplate(){}

    public HydraTemplate(String name, String confidentialityLevel, TemplateStartingOptions startingOptions, TemplateDependencies dependencies, TemplateHydraOptions hydraOptions) {
        this.name = name;
        this.confidentialityLevel = confidentialityLevel;
        this.startingOptions = startingOptions;
        this.dependencies = dependencies;
        this.hydraOptions = hydraOptions;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfidentialityLevel() {
        return this.confidentialityLevel;
    }

    public void setConfidentialityLevel(String confidentialityLevel) { this.confidentialityLevel = confidentialityLevel;}

    public TemplateStartingOptions getStartingOptions() {
        return this.startingOptions;
    }

    public void setStartingOptions(TemplateStartingOptions startingOptions) {
        this.startingOptions = startingOptions;
    }

    public TemplateDependencies getDependencies() {
        return this.dependencies;
    }

    public void setDependencies(TemplateDependencies dependencies) {
        this.dependencies = dependencies;
    }

    public TemplateHydraOptions getHydraOptions() {
        return this.hydraOptions;
    }

    public void setHydraOptions(TemplateHydraOptions hydraOptions) {
        this.hydraOptions = hydraOptions;
    }

}

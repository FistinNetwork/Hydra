package fr.fistin.hydra.server.template;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.logger.LogType;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class HydraTemplateManager {

    private final File templatesFolder;
    private final Map<String, HydraTemplate> templates;

    private final Hydra hydra;

    public HydraTemplateManager(Hydra hydra) {
        this.hydra = hydra;
        this.templatesFolder = new File("templates");
        this.templates = new HashMap<>();
    }

    public void createTemplatesFolder() {
        if (!this.templatesFolder.exists()) {
            this.hydra.getLogger().log(LogType.INFO, String.format("%s folder doesn't exist ! Creating it...", this.templatesFolder.getName()));
            this.templatesFolder.mkdirs();
        }
    }

    public void loadAllTemplatesFromTemplatesFolder() {
        final File[] files = this.templatesFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".yml")) {
                    final HydraTemplate template = this.loadTemplate(file);
                    this.templates.putIfAbsent(template.getName(), template);
                }
            }
        }
    }

    public HydraTemplate loadTemplate(File file) {
        HydraTemplate template = null;
        try {
            final InputStream inputStream = new FileInputStream(file);
            final Yaml yaml = new Yaml(new Constructor(HydraTemplate.class));

            template = yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("Couldn't find %s file !", file.getName()));
        }

        if (template == null) this.hydra.getLogger().log(LogType.ERROR, String.format("Couldn't load a template from %s file !", file.getName()));
        return template;
    }

    public HydraTemplate loadTemplate(String url) {
        final Yaml yaml = new Yaml(new Constructor(HydraTemplate.class));

        return yaml.load(this.getUrlContents(url));
    }

    public void createTemplateFile(HydraTemplate template) {
        final File file = new File(this.templatesFolder.getName() + "/" + template.getName() + ".yml");
        try {
            if (!file.exists()){
                final DumperOptions options = new DumperOptions();
                options.setIndent(2);
                options.setPrettyFlow(true);
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

                final Representer representer = new Representer();
                representer.addClassTag(HydraTemplate.class, Tag.MAP);

                final PrintWriter writer = new PrintWriter(file);
                final Yaml yaml = new Yaml(representer, options);

                yaml.dump(template, writer);

                this.hydra.getLogger().log(LogType.INFO, String.format("Successfully created %s template !", template.getName()));
            } else {
                this.hydra.getLogger().log(LogType.ERROR, String.format("%s file already exists !", file.getName()));
            }
        } catch (Exception e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("Encountered exception during creating %s template in %s file", template.getName(), file.getName()));
            if (file.exists()) file.delete();
        }
    }

    private String getUrlContents(String url) {
        final StringBuilder content = new StringBuilder();

        try {
            final URLConnection urlConnection = new URL(url).openConnection();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void addTemplate(HydraTemplate template) {
        this.templates.putIfAbsent(template.getName(), template);
        this.createTemplateFile(template);
    }

    public void removeTemplate(String name) {
        this.templates.remove(name);

        final File file = new File(this.templatesFolder.getName() + "/" + name + ".yml");
        if (file.exists()) file.delete();
    }

    public HydraTemplate getTemplateByName(String name) {
        return this.templates.getOrDefault(name, null);
    }

    public File getTemplatesFolder() {
        return this.templatesFolder;
    }

    public Map<String, HydraTemplate> getTemplates() {
        return this.templates;
    }
}

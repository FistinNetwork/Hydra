package fr.fistin.hydra.server.template;

import fr.fistin.hydra.Hydra;
import fr.fistin.hydra.util.logger.LogType;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class HydraTemplateManager {

    private final Hydra hydra;

    public HydraTemplateManager(Hydra hydra) {
        this.hydra = hydra;
    }

    public HydraTemplate loadTemplateFromFile(File file) {
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

    public HydraTemplate loadTemplateFromUrl(String url) {
        final Yaml yaml = new Yaml(new Constructor(HydraTemplate.class));

        return yaml.load(this.getUrlContents(url));
    }

    public void createTemplate(HydraTemplate template, File file) {
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
            } else {
                this.hydra.getLogger().log(LogType.ERROR, String.format("%s file already exists !", file.getName()));
            }
        } catch (Exception e) {
            this.hydra.getLogger().log(LogType.ERROR, String.format("Encountered exception during creating %s template in %s file", template.getName(), file.getName()));
            if (file.exists()) file.delete();
        }
    }

    public String getUrlContents(String url) {
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

}

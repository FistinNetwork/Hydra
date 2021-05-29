package fr.fistin.hydra.docker.image;

public class DockerImage {

    public static final String DOCKER_IMAGE_TAG_SEPARATOR = ":";

    private String name;
    private String tag;

    public DockerImage(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}

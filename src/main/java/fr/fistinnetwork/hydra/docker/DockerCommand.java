package fr.fistinnetwork.hydra.docker;

import fr.fistinnetwork.hydra.Hydra;

import java.util.HashMap;
import java.util.Map;

public enum DockerCommand
{
	NONE("unknown", () -> {
		Hydra.getInstance().getLogger().warning("Unknow command !");
	});
	
	private static final Map<String, DockerCommand> DOCKER_COMMANDS = new HashMap<>();
	
	private final String name;
	private final Runnable task;
	
	static
	{
		for(DockerCommand command : values())
			DOCKER_COMMANDS.put(command.getName(), command);
	}
	
	DockerCommand(String name, Runnable task)
	{
		this.name = name;
		this.task = task;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Runnable getTask() 
	{
		return this.task;
	}
	
	public static DockerCommand getDockerCommandByName(String name)
	{
		return DOCKER_COMMANDS.getOrDefault(name, NONE);
	}
	
	static void unload()
	{
		DOCKER_COMMANDS.clear();
	}
}

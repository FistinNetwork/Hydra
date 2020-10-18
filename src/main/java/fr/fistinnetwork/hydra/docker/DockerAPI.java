package fr.fistinnetwork.hydra.docker;

import fr.fistinnetwork.hydra.Hydra;

public class DockerAPI
{
	public void callCommand(String name)
	{
		Hydra.getInstance().getProxy().getScheduler().runAsync(Hydra.getInstance(), () -> {
			final DockerCommand command = DockerCommand.getDockerCommandByName(name);
			DockerAPI.this.getPlugin().getLogger().info(String.format("Calling %s command !", command.getName()));
			command.getTask().run();
		});
	}
	
	public void unload()
	{
		DockerCommand.unload();
	}
	
	public Hydra getPlugin()
	{
		return Hydra.getInstance();
	}
}

package caexbot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Message;

public class ShutdownCommand implements CommandExecutor {

	@Command(aliases={"sleep"}, description = "Kill Caex", requiredPermissions = "player")
	public void shutdown(){
		System.exit(0);
	}
}

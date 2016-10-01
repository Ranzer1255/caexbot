package caexbot.commands;

import java.util.List;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ShutdownCommand extends CaexCommand {

	@Command(aliases={"sleep"}, description = "Kill Caex", requiredPermissions = "player")
	public void shutdown(){
		System.exit(0);
	}

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAlias() {
		// TODO Auto-generated method stub
		return null;
	}
}

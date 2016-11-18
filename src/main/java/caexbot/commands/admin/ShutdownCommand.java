package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShutdownCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if (author!=event.getJDA().getUserById(CaexConfiguration.getInstance().getOwner())){
			noPermission(event);
			return;
		}
		channel.sendMessage("if you insist boss.... *blerg*").queue();
		event.getJDA().shutdown();
		System.exit(0);
	}

	@Override
	public String getUsage(Guild g) {
		return getPrefix(g)+"sleep (requires permision)";
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("sleep");
	}

	@Override
	public String getDescription() {
		return "kill Caex!";
	}
}
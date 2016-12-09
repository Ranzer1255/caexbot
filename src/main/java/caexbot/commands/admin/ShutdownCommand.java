package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.config.CaexConfiguration;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class ShutdownCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if (author!=event.getJDA().getUserById(CaexConfiguration.getInstance().getOwner())){
			noPermission(event);
			return;
		}
		channel.sendMessage("if you insist boss.... *blerg*").queue();
		if (args.length>0&&args[0].equals("alert")) {
			for (Guild g : event.getJDA().getGuilds()) {
				try {
					g.getPublicChannel().sendMessage("I've got to go.... \n*casts teleport and vanishes*").queue();
				} catch (PermissionException e) {
					Logging.error("i can't talk here sorry: " + e.getLocalizedMessage());
				}
			} 
		}
		event.getJDA().shutdown();
		System.exit(0);
	}

	@Override
	public String getUsage(Guild g) {
		return getPrefix(g)+"sleep (requires permision)";
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("vdri");
	}

	@Override
	public String getDescription() {
		return "kill Caex!";
	}
}
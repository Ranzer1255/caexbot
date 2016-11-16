package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.config.CaexConfiguration;
import caexbot.config.PrefixManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		switch (args.length){
		case 0:
			PrefixManager.removePrefix(event.getGuild());
			channel.sendMessage(String.format("Ok boss, I'll listen for \"%s\"", CaexConfiguration.getInstance().getPrefix())).queue();
			return;
		case 1:
			PrefixManager.setPrefix(event.getGuild(), args[0]);
			channel.sendMessage(String.format("Ok boss, I'll listen for \"%s\"", args[0])).queue();
		default:			
			channel.sendMessage("Hey, i can't listen for more than one thing ;)").queue();
			return;
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("set-prefix");
	}

	@Override
	public String getDescription() {
		// TODO make getDescription
		return null;
	}

	@Override
	public String getUsage(Guild g) {
		// TODO make getUsage
		return null;
	}
	
	@Override
	public Permission getPermissionRequirements() {
		
		return Permission.ADMINISTRATOR;
	}

}

package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.config.CaexConfiguration;
import caexbot.data.GuildManager;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixCommand extends CaexCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		switch (args.length){
		case 0:
			GuildManager.removePrefix(event.getGuild());
			channel.sendMessage(String.format("Ok boss, I'll listen for \"%s\"", CaexConfiguration.getInstance().getPrefix())).queue();
			return;
		case 1:
			GuildManager.setPrefix(event.getGuild(), args[0]);
			channel.sendMessage(String.format("Ok boss, I'll listen for \"%s\"", GuildManager.getPrefix(event.getGuild()))).queue();
			return;
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
	public String getShortDescription() {
		return "Set prefix for the Guild (requires Administrator permision)";
	}

	@Override
	public String getLongDescription() {
		// TODO make getLongDescription
		return getShortDescription();
	}
	@Override
	public String getUsage(Guild g) {
		return String.format("**[%s]** <prefix to set to> [blank to reset to default] ", StringUtil.cmdArrayToString(getAlias(), ", "	, g));
	}
	
	@Override
	public Permission getPermissionRequirements() {
		
		return Permission.ADMINISTRATOR;
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.ADMIN;
	}
}

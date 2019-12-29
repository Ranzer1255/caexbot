package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.CaexBot;
import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.config.CaexConfiguration;
import caexbot.data.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrefixCommand extends CaexCommand implements Describable{

	@Override
	public void process(String[] args,  MessageReceivedEvent event) {
		
		switch (args.length){
		case 0:
			CaexBot.GUILD_MANAGER.removePrefix(event.getGuild());
			event.getChannel().sendMessage(String.format("Ok boss, I'll listen for \"%s\"", CaexConfiguration.getInstance().getPrefix())).queue();
			return;
		case 1:
			CaexBot.GUILD_MANAGER.setPrefix(event.getGuild(), args[0]);
			event.getChannel().sendMessage(String.format("Ok boss, I'll listen for \"%s\"", CaexBot.GUILD_MANAGER.getPrefix(event.getGuild()))).queue();
			return;
		default:			
			event.getChannel().sendMessage("Hey, i can't listen for more than one thing ;)").queue();
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
		return    "Defines a new prefix for this guild.\n\n"
				+ "Leave the option blank to reset to the default prefix of `"+CaexConfiguration.getInstance().getPrefix()+"`";
	}
	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" [<new prefix>]`";
	}
	
	@Override
	public Permission getPermissionRequirements() {
		
		return Permission.ADMINISTRATOR;
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.ADMIN;
	}

	@Override
	public boolean isAplicableToPM() {
		return false;
	}
}

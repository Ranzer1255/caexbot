package net.ranzer.caexbot.commands.admin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.data.GuildManager;

import java.util.Collections;
import java.util.List;

public class PrefixCommand extends BotCommand implements Describable{

	private static final String SCO_PREFIX = "prefix";

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		
		switch (args.length){
		case 0:
			GuildManager.removePrefix(event.getGuild());
			event.getChannel().sendMessage(String.format("Ok boss, I'll listen for \"%s\"", CaexConfiguration.getInstance().getPrefix())).queue();
			return;
		case 1:
			GuildManager.setPrefix(event.getGuild(), args[0]);
			event.getChannel().sendMessage(String.format("Ok boss, I'll listen for \"%s\"", GuildManager.getPrefix(event.getGuild()))).queue();
			return;
		default:			
			event.getChannel().sendMessage("Hey, i can't listen for more than one thing ;)").queue();
		}
	}

	@Override
	public List<String> getAlias() {
		return Collections.singletonList("set-prefix");
	}

	@Override
	public String getShortDescription() {
		return "Set prefix for the Guild (requires Administrator permission)";
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
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public boolean isApplicableToPM() {
		return false;
	}

	@Override
	public CommandData getCommandData() {
		CommandData rtn = new CommandData(getName(),getShortDescription());
		rtn.addOption(OptionType.STRING, SCO_PREFIX,"the prefix i will listen for when not using slash commands",true);
		rtn.setDefaultEnabled(false);
		return rtn;
	}
}

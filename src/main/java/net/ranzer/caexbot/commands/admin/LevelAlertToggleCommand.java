package net.ranzer.caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.data.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.data.IGuildData;

public class LevelAlertToggleCommand extends CaexCommand implements Describable {

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		
		if (args.length==0){
			event.getTextChannel().sendMessage("The Level announcement setting for this guild is currently: "+
					GuildManager.getGuildData(event.getGuild()).getXPAnnouncement()).queue();
			return;
		}
		if (args.length!=1||!(args[0].toLowerCase().equals("true")||args[0].toLowerCase().equals("false"))){
			event.getTextChannel().sendMessage("I'm sorry i didn't understand that please follow the usage\n"
								+getUsage(event.getGuild())).queue();
			return;
		}
		
		GuildManager.getGuildData(event.getGuild()).setXPAnnouncement(Boolean.parseBoolean(args[0]));
		
		if(Boolean.parseBoolean(args[0])){
			event.getTextChannel().sendMessage("I will now inform you when your level changes!").queue();
		} else {
			event.getTextChannel().sendMessage("I will no longer inform you when your level changes").queue();
		}
	
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("level-alert");
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" [{true|false}]`";
	}

	@Override
	public String getShortDescription() {
		return "Set Level change announcemnt for the guild";
	}

	@Override
	public String getLongDescription() {
		return "This command sets the Level Change announcement for the guild\n"
				+ "leaving the value blank will return the current setting for this Guild\n\n"
				+ "`True`: users will be notified when their level changes\n"
				+ "`False`: users will not be notified when their level changes\n"
				+ "`Default Value`: " + IGuildData.DEFAULT_XP_ANNOUNCEMENT;
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
package net.ranzer.caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.data.IGuildData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AnnouncementSettingCommand extends BotCommand implements Describable {

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		
		IGuildData guildData = GuildManager.getGuildData(event.getGuild());
		
		if (args.length==1){
			subCommand cmd = subCommand.UNKNOWN;
			for (subCommand sc : subCommand.values()) {
				if (args[0].toUpperCase().equals(sc.toString())){
					cmd = sc;
				}
			}
			switch (cmd){
			case SET:
				guildData.setAnnouncementChannel(event.getTextChannel());
				break;
			case CLEAR:
				guildData.setAnnouncementChannel(null);
				break;
			default:
				break;
			}
		}
		
		String chan = (guildData.getAnnouncementChannel()!=null ? guildData.getAnnouncementChannel().getName():"none");
		event.getChannel().sendMessage(String.format("My current announcement channel is: %s",
				chan)).queue();
		
	}

	@Override
	public String getShortDescription() {
		return "Manages caex's announcement channel for your guild";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n"
				+ "announcements will include Joiners and leavers to your guild as well as bot alerts such"
				+ " as shutdowns and reboots, (though these are rare)\n\n"
				+ "`set`:  will set the current channel to be Caex's announcemnet channel\n"
				+ "`clear`: will clear the announcement channel and disable all of caex's announcements";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%s%s [{set | clear}]`",
				getPrefix(g),
				getName());
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("announce","announcement");
	}

	@Override
	public boolean isApplicableToPM() {
		return false;
	}

	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}
	
	@Override
	public Permission getPermissionRequirements() {
		
		return Permission.ADMINISTRATOR;
	}

	private enum subCommand {
		SET,CLEAR,UNKNOWN
	}

}

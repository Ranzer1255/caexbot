package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.data.GuildData;
import caexbot.data.GuildManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AnnouncementSettingCommand extends CaexCommand implements Describable {

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		
		GuildData guildData = GuildManager.getGuildData(event.getGuild());
		
		if (args.length==1){
			subCommand cmd = subCommand.UNKNOWN;
			for (subCommand sc : subCommand.values()) {
				if (args[0].toUpperCase().equals(sc.toString())){
					cmd = sc;
				}
			}
			System.out.println(cmd);
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
		event.getChannel().sendMessage(String.format("My current annoucement channel is: %s",
				chan)).queue();
		
	}

	@Override
	public String getShortDescription() {
		return "Manages caex's annoucement channel for your guild";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n"
				+ "annoucements will include Joiners and leavers to your guild as well as bot alerts such"
				+ " as shutdowns and reboots, (though these are rare)\n\n"
				+ "`set`:  will set the current channel to be Caex's Annoucemnet channel\n"
				+ "`clear`: will clear the annoucement channel and disable all of caex's annoucements";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("%s%s [{set | clear}]",
				getPrefix(g),
				getName());
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("annouce","annoucement");
	}

	@Override
	public boolean isAplicableToPM() {
		return false;
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.ADMIN;
	}
	
	@Override
	public Permission getPermissionRequirements() {
		
		return Permission.ADMINISTRATOR;
	}

	private enum subCommand {
		SET,CLEAR,UNKNOWN
	}

}

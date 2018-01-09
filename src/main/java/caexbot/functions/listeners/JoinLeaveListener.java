package caexbot.functions.listeners;

import caexbot.data.GuildData;
import caexbot.data.GuildManager;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class JoinLeaveListener extends ListenerAdapter {
	
	private final String JOIN_MESSAGE = "*bows* Welcome %s!";
	private final String LEAVE_MESSAGE = "To all that care, %s has departed!";
	
	//TODO add in disable option to Database!
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		GuildData gd = GuildManager.getGuildData(event.getGuild());
		TextChannel tc = gd.getAnnouncementChannel();
		if (tc==null) return;
		
		tc.sendMessage(String.format(JOIN_MESSAGE, event.getUser().getAsMention())).queue();
	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		GuildData gd = GuildManager.getGuildData(event.getGuild());
		TextChannel tc = gd.getAnnouncementChannel();
		if (tc==null) return;
		
		tc.sendMessage(String.format(LEAVE_MESSAGE, event.getUser().getName())).queue();
		
	}

}

package net.ranzer.caexbot.functions.listeners;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.data.IGuildData;

public class JoinLeaveListener extends ListenerAdapter {
	
	private final String JOIN_MESSAGE = "*bows* Welcome %s!";
	private final String LEAVE_MESSAGE = "To all that care, %s has departed!";
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		IGuildData gd = GuildManager.getGuildData(event.getGuild());
		TextChannel tc = gd.getAnnouncementChannel();
		if (!gd.getJLAnnouncement()) return;
		if (tc==null) return;		
		tc.sendMessage(String.format(JOIN_MESSAGE, event.getUser().getAsMention())).queue();
	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		IGuildData gd = GuildManager.getGuildData(event.getGuild());
		TextChannel tc = gd.getAnnouncementChannel();
		if (!gd.getJLAnnouncement()) return;
		if (tc==null) return;		
		tc.sendMessage(String.format(LEAVE_MESSAGE, event.getUser().getName())).queue();		
	}
}

package net.ranzer.caexbot.functions.listeners;

import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.data.GuildData;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinLeaveListener extends ListenerAdapter {
	
	private final String JOIN_MESSAGE = "*bows* Welcome %s!";
	private final String LEAVE_MESSAGE = "To all that care, %s has departed!";
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		GuildData gd = GuildManager.getGuildData(event.getGuild());
		TextChannel tc = gd.getAnnouncementChannel();
		if (!gd.getJLannouncement()) return;
		if (tc==null) return;		
		tc.sendMessage(String.format(JOIN_MESSAGE, event.getUser().getAsMention())).queue();
	}
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		GuildData gd = GuildManager.getGuildData(event.getGuild());
		TextChannel tc = gd.getAnnouncementChannel();
		if (!gd.getJLannouncement()) return;
		if (tc==null) return;		
		tc.sendMessage(String.format(LEAVE_MESSAGE, event.getUser().getName())).queue();		
	}
}

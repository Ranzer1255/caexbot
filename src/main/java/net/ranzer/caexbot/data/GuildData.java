package net.ranzer.caexbot.data;

import net.ranzer.caexbot.functions.levels.RoleLevel;
import net.ranzer.caexbot.functions.levels.UserLevel;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.ranzer.caexbot.functions.levels.RoleLevel;
import net.ranzer.caexbot.functions.levels.UserLevel;

import java.util.List;

public abstract class GuildData extends ListenerAdapter {
	//xp methods
	abstract public void addXP(User author, int XP, MessageChannel channel);

	abstract public void removeXP(User author, int XP, MessageChannel channel);

	abstract public boolean getXPAnnouncement();

	abstract public void setXPAnnouncement(boolean announce);

	abstract public boolean getJLannouncement();

	abstract public void setJLannouncement(boolean announce);

	abstract public RoleLevel getRoleLevel(Role role);

	abstract public List<RoleLevel> getRoleRankings();

	abstract public void setExcludedRole(Role r, boolean exclude);

	abstract public List<UserLevel> getGuildRankings();

	abstract public UserLevel getUserLevel(Member author);

	abstract public int getLevel(User author);

	abstract public int getXP(User u);

	//prefix methods
	abstract public String getPrefix();

	abstract public void setPrefix(String prefix);

	abstract public void removePrefix();

	public abstract TextChannel getAnnouncementChannel();

	public abstract void setAnnouncementChannel(TextChannel channel);

	public abstract TextChannel getDefaultMusicChannel();

	public abstract void setDefaultMusicChannel(TextChannel musicChannel);

	public abstract ChannelData getChannel(TextChannel channel);

}

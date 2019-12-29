package net.ranzer.caexbot.data.json;

import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.data.ChannelData;
import net.ranzer.caexbot.data.GuildData;
import net.ranzer.caexbot.functions.levels.RoleLevel;
import net.ranzer.caexbot.functions.levels.UserLevel;
import net.dv8tion.jda.api.entities.*;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.functions.levels.UserLevel;

import java.util.List;

public class JSONGuildData extends GuildData {

	private final Guild GUILD;

	public JSONGuildData(Guild guild) {
		GUILD = guild;
	}

	@Override
	public void addXP(User author, int XP, MessageChannel channel) {

	}

	@Override
	public void removeXP(User author, int XP, MessageChannel channel) {

	}

	@Override
	public boolean getXPAnnouncement() {
		return false;
	}

	@Override
	public void setXPAnnouncement(boolean announce) {

	}

	@Override
	public boolean getJLannouncement() {
		return false;
	}

	@Override
	public void setJLannouncement(boolean announce) {

	}

	@Override
	public RoleLevel getRoleLevel(Role role) {
		return new RoleLevel(role);
	}

	@Override
	public List<RoleLevel> getRoleRankings() {
		return null;
	}

	@Override
	public void setExcludedRole(Role r, boolean exclude) {

	}

	@Override
	public List<UserLevel> getGuildRankings() {
		return null;
	}

	@Override
	public UserLevel getUserLevel(Member author) {
		return null;
	}

	@Override
	public int getLevel(User author) {
		return 0;
	}

	@Override
	public int getXP(User u) {
		return 0;
	}

	@Override
	public String getPrefix() {
		return CaexConfiguration.getInstance().getPrefix();
	}

	@Override
	public void setPrefix(String prefix) {

	}

	@Override
	public void removePrefix() {

	}

	@Override
	public TextChannel getAnnouncementChannel() {
		return null;
	}

	@Override
	public void setAnnouncementChannel(TextChannel channel) {

	}

	@Override
	public TextChannel getDefaultMusicChannel() {
		return null;
	}

	@Override
	public void setDefaultMusicChannel(TextChannel musicChannel) {

	}

	@Override
	public ChannelData getChannel(TextChannel channel) {
		return null;
	}
}

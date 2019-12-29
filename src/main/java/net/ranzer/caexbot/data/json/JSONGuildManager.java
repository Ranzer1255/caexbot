package net.ranzer.caexbot.data.json;

import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.data.GuildData;
import net.ranzer.caexbot.data.GuildManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;

import java.util.HashMap;
import java.util.Map;

public class JSONGuildManager extends GuildManager {

	private Map<Guild, GuildData> guilds = new HashMap<>();

	public JSONGuildManager() {
		for (Guild g : CaexBot.getJDA().getGuilds()) {
			guilds.put(g,new JSONGuildData(g));
		}
	}


	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		guilds.put(event.getGuild(),new JSONGuildData(event.getGuild()));
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {

	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

	}

	@Override
	public String getPrefix(Guild guild) {
		return guilds.get(guild).getPrefix();
	}

	@Override
	public void setPrefix(Guild guild, String prefix) {

	}

	@Override
	public void removePrefix(Guild guild) {

	}

	@Override
	public GuildData getGuildData(Guild guild) {
		return guilds.get(guild);
	}
}

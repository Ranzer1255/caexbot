package caexbot.data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

abstract public class GuildManager extends ListenerAdapter {

	public abstract void onGuildJoin(GuildJoinEvent event);

	public abstract void onGuildLeave(GuildLeaveEvent event);

	public abstract void onGuildMemberLeave(GuildMemberLeaveEvent event);

	//convenience pass-through methods
	public abstract String getPrefix(Guild guild);

	public abstract void setPrefix(Guild guild, String prefix);

	public abstract void removePrefix(Guild guild);

	public abstract GuildData getGuildData(Guild guild);
}

package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCommand extends MusicCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		GuildPlayerManager.getPlayer(event.getGuild()).stop();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("stop");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

}

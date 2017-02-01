package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShuffleCommand extends AbstractMusicCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		GuildPlayerManager.getPlayer(event.getGuild()).shuffle();

	}

	@Override
	public List<String> getAlias() {
		// TODO Auto-generated method stub
		return Arrays.asList("Shuffle");
	}

}

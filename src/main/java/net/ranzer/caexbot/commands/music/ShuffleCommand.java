package net.ranzer.caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.functions.music.GuildPlayerManager;

public class ShuffleCommand extends AbstractMusicCommand {

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		GuildPlayerManager.getPlayer(event.getGuild()).shuffle();

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("shuffle", "mix");
	}

	@Override
	public String getShortDescription() {
		return "shuffle shuffle!";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()+
				"randomly shuffles the order of the queue";
	}
}

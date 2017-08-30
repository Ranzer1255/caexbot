package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		GuildPlayerManager.getPlayer(event.getGuild()).stop();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("stop");
	}

	@Override
	public String getShortDescription() {
		return "Stops the current song, leaves the VC and Clears the queue";
	}
}

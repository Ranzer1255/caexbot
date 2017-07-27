package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		GuildPlayerManager.getPlayer(event.getGuild()).pause();
		
		 
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("pause");
	}

	@Override
	public String getShortDescription() {
		return "Pauses the currently playing song";
	}
}

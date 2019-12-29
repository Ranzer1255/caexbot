package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PauseCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if(!inSameVoiceChannel(event)){
			event.getChannel().sendMessage("You must be listening to pause.").queue();
			return;
		}
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
	
	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n"
				+ "to resume: call this command again, or call `play`\n\n"
				+ "you must be in the same voice channel to pause music.";
	}
	
}

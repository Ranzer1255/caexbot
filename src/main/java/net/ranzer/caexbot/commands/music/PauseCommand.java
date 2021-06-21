package net.ranzer.caexbot.commands.music;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.music.GuildPlayerManager;

import java.util.Collections;
import java.util.List;

public class PauseCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if(notInSameVoiceChannel(event)){
			event.getChannel().sendMessage("You must be listening to pause.").queue();
			return;
		}
		GuildPlayerManager.getPlayer(event.getGuild()).pause();
		
		 
	}

	@Override
	public List<String> getAlias() {
		return Collections.singletonList("pause");
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

package net.ranzer.caexbot.functions.music.commands;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.music.GuildPlayerManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Joins requester voice channel
 * 
 * @author Ranzer
 *
 */
public class JoinCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		
		VoiceChannel join = getVoiceChannel(Objects.requireNonNull(event.getMember()));
		
		if(join!=null){	
			GuildPlayerManager.getPlayer(event.getGuild()).join(join);
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("join","j");
	}

	@Override
	public String getShortDescription() {
		return "join bot to your current voice channel";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()
				+ "This command will join caex to whatever voice channel you are currently in";
	}
}

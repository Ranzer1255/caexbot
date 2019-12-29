package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SkipCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if(!inSameVoiceChannel(event)){
			event.getChannel().sendMessage("You must be listening to skip a song.").queue();
			return;
		}
		if(GuildPlayerManager.getPlayer(event.getGuild()).isPlaying()){
			GuildPlayerManager.getPlayer(event.getGuild()).playNext(true);
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("skip", "next", "n");
	}

	@Override
	public String getShortDescription() {
		return "Skips the rest of the current track";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()+
				"\n\n"
				+ "You must be in the same voice channel to skip songs.";
	}
}

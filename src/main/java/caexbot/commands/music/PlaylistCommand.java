package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlaylistCommand extends AbstractMusicCommand implements Describable {

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if (args.length == 0){
			event.getChannel().sendMessage("what was the playlist ID again?").queue();
			return;
		}
		if (args[0].charAt(0)=='i' && args.length>1){
			GuildPlayerManager.getPlayer(event.getGuild()).insertID(args[1]);
		}
		GuildPlayerManager.getPlayer(event.getGuild()).queueID(args[0]);

		
	}
	
	@Override
	public List<String> getAlias() {
		return Arrays.asList("playlist", "pl");
	}

	@Override
	public String getShortDescription() {
		return "skips the search process and passes input straight to lavaplayer";
	}
}

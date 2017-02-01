package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SkipCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if(GuildPlayerManager.getPlayer(event.getGuild()).isPlaying()){
			GuildPlayerManager.getPlayer(event.getGuild()).playNext();
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("skip");
	}

	@Override
	public String getShortDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

}
package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		/*TODO Pause Command process
		 * Pause current playing track.
		 * if not playing 
		 * 		do nothing
		 */

		GuildPlayerManager.getPlayer(event.getGuild()).pause();
		
		 
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("pause");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

}

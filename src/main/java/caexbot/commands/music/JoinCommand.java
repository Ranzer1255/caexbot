/**
 * 
 */
package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Joins requester's voice channel
 * 
 * @author Ranzer
 *
 */
public class JoinCommand extends CaexCommand{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		VoiceChannel join = event.getGuild().getMember(author).getVoiceState().getChannel();
		
		if(join!=null){	
			GuildPlayerManager.getPlayer(event.getGuild()).join(join);
		}
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("join","j");
	}

	@Override
	public String getDescription() {
		// TODO make getDescription
		return null;
	}

	@Override
	public String getUsage(Guild g) {
		// TODO make getUsage
		return null;
	}

}

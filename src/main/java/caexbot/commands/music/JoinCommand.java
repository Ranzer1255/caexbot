/**
 * 
 */
package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.music.GuildPlayerManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Joins requester's voice channel
 * 
 * @author Ranzer
 *
 */
public class JoinCommand extends AbstractMusicCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		
		VoiceChannel join = event.getGuild().getMember(event.getAuthor()).getVoiceState().getChannel();
		
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

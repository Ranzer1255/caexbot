package caexbot.commands.music;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class PlayCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		/*TODO play command process
		 * join voice channel of requested user
		 * start first track in queue for requested guild
		 */
		
//		event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceStatusOfUser(author).getChannel());

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("play","p");
	}

	@Override
	public String getDescription() {
		return "Start playing the first song in queue";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

}

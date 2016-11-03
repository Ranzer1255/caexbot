package caexbot.commands.music;

import java.util.List;

import caexbot.commands.CaexSubCommand;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class QueueCommand extends CaexSubCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		/*TODO Queue command process
		 *search for video id
		 *add id to track queue 
		 */

	}

	@Override
	public List<String> getAlias() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

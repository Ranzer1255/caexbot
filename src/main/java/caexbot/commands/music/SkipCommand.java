package caexbot.commands.music;

import java.util.List;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class SkipCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		/*TODO skip process
		 * skip to next track in queue
		 * if not playing do nothing
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

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

}

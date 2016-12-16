/**
 * 
 */
package caexbot.commands.music;

import java.util.List;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Joins requester's voice channel
 * 
 * @author jrdillingham
 *
 */
public class JoinCommand extends CaexCommand{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		
	}

	@Override
	public List<String> getAlias() {
		// TODO make getAlias
		return null;
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

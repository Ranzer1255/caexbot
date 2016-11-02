package caexbot.commands.music;

import java.util.List;

import caexbot.commands.CaexSubCommand;
import caexbot.functions.music.GuildPlayerManager;
import caexbot.functions.music.TrackQueue;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class QueueCommand extends CaexSubCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		TrackQueue queue = GuildPlayerManager.getInstance().getQueue(event.getGuild());
		queue.add(args);//TODO this is where i left off.

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

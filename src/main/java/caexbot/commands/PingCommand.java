package caexbot.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class PingCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		channel.sendMessage("pong!");	
	}

	@Override
	public String getUsage() {
		return getPrefix()+"ping";
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("ping");
	}

	@Override
	public String getDescription() {
		return "pong!";
	}


}

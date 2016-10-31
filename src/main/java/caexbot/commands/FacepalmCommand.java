package caexbot.commands;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class FacepalmCommand extends CaexCommand {

	private String[] facepalms ={"*%s shoves their palm through their brain*",
								 "*%s groans as a flat palm meets their forhead*",
								 "*%s throws their head on their desk with a loud thud*"}; 
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		channel.sendMessage(String.format(facepalms[ThreadLocalRandom.current().nextInt(facepalms.length)], author.getAsMention()));

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("facepalm","fb");
	}

	@Override
	public String getDescription() {
		// TODO Facepalm description
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Facepalm Usage
		return null;
	}

}

package caexbot.commands;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public abstract class CaexCommand{

	public void runCommand(String[] args, User author, TextChannel channel, MessageReceivedEvent event){} //TODO
	
	
}

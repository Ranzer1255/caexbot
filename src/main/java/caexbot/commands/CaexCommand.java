package caexbot.commands;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public abstract class CaexCommand{

	public static String CMD_PREFEX = "!";
	
	public static void setPrefex(String prefex){
		CMD_PREFEX = prefex;
	}

	public void runCommand(String[] args, User author, TextChannel channel, MessageReceivedEvent event){} //TODO
	
	abstract public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event);
	
	abstract public String getUsage();
}

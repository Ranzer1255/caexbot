package caexbot.commands;

import java.util.List;

import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public abstract class CaexCommand{

	
	public static String getPrefix() {
		return CaexConfiguration.getInstance().getPrefix();
	}

	public void runCommand(String[] args, User author, TextChannel channel, MessageReceivedEvent event){
		
		
		process(args, author, channel, event);
	} //TODO
	
	abstract public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event);
	
	abstract public List<String> getAlias();
	
	abstract public String getDescription();
	
	abstract public String getUsage();


}

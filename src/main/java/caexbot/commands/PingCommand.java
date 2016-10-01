package caexbot.commands;

import java.util.List;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class PingCommand extends CaexCommand {

	@Command(aliases = {"ping"}, description = "pong!")
	public String pingCommand(){
		return "pong!";
	}
	
	@Command(aliases = {("pong")}, description = "ping!")
	public String pongCommand(){
		return "ping!";
	}
	
	@Command(aliases = {"ding"}, description = "dong!")
	public String dingCommand(){
		return "dong!";
	}
	
	@Command(aliases = {"dong"}, description = "ding!")
	public String dongCommand(){
		return "ding!";
	}
	
	@Command(aliases = {"derp"}, description = "herp!")
	public String derpCommand(){
		return "dert de der!";
	}

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAlias() {
		// TODO Auto-generated method stub
		return null;
	}
}

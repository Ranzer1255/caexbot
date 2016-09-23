package caexbot.commands;

import caexbot.references.CaexBotReference;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingCommand implements CommandExecutor {

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
}

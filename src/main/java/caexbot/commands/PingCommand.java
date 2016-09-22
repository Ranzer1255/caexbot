package caexbot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingCommand implements CommandExecutor {

	@Command(aliases = {"!ping"}, description = "pong!")
	public String pingCommand(){
		return "pong!";
	}
	
	@Command(aliases = {"!pong"}, description = "pong!")
	public String pongCommand(){
		return "ping!";
	}
	
	@Command(aliases = {"!ding"}, description = "pong!")
	public String dingCommand(){
		return "dong!";
	}
	
	@Command(aliases = {"!dong"}, description = "pong!")
	public String dongCommand(){
		return "ding!";
	}
	
	@Command(aliases = {"!derp"}, description = "pong!")
	public String derpCommand(){
		return "dert de der!";
	}
}

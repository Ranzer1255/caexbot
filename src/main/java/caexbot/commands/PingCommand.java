package caexbot.commands;

import caexbot.references.CaexBotReference;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingCommand implements CommandExecutor {

	@Command(aliases = {CaexBotReference.COMMAND_LEAD+" ping"}, description = "pong!")
	public String pingCommand(){
		return "pong!";
	}
	
	@Command(aliases = {(CaexBotReference.COMMAND_LEAD+"pong")}, description = "pong!")
	public String pongCommand(){
		return "ping!";
	}
	
	@Command(aliases = {CaexBotReference.COMMAND_LEAD+"ding"}, description = "pong!")
	public String dingCommand(){
		return "dong!";
	}
	
	@Command(aliases = {CaexBotReference.COMMAND_LEAD+"dong"}, description = "pong!")
	public String dongCommand(){
		return "ding!";
	}
	
	@Command(aliases = {CaexBotReference.COMMAND_LEAD+"derp"}, description = "pong!")
	public String derpCommand(){
		return "dert de der!";
	}
}

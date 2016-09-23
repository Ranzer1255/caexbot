package caexbot.commands;

import java.text.Format;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.CommandHandler.SimpleCommand;

public class HelpCommand implements CommandExecutor {

	private CommandHandler h;
	
	public HelpCommand(CommandHandler h) {
		this.h=h;
	}
	
	@Command(aliases={"help"}, description="Show a list of commands")
	public String helpCommand(){
		StringBuilder sb = new StringBuilder();
		for (SimpleCommand sc : h.getCommands()) {
			sb.append(("**["+h.getDefaultPrefix()));
			sb.append(sc.getCommandAnnotation().aliases()[0]);
			sb.append("]** ");
			sb.append(sc.getCommandAnnotation().description()+"\n");	
		}
		return sb.toString();
	}
}

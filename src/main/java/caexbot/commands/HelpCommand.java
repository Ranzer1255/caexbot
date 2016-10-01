package caexbot.commands;

import java.util.List;

import caexbot.backgroundFunctions.CommandListener;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.CommandHandler.SimpleCommand;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class HelpCommand extends CaexCommand {

	private CommandListener h;
	
	public HelpCommand(CommandListener h) {
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

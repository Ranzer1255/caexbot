package caexbot.backgroundFunctions;

import java.util.List;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	List<CaexCommand> cmds;
	
	public void addCommand(CaexCommand cmd){
		
		this.cmds.add(cmd);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
	}

}

package caexbot.functions.background;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.CaexBot;
import caexbot.commands.CaexCommand;
import caexbot.commands.DraconicCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DraconicListener extends ListenerAdapter{

	List<DraconicCommand> cmds = new ArrayList<>();
	CommandListener cl;
	
	public DraconicListener(CommandListener cl) {
		this.cl=cl;
	}

	public void addCommand(DraconicCommand cmd){
		cmds.add(cmd);
	}
	
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()){return;}
		
		if(!event.getMessage().getContentRaw().toLowerCase().startsWith(CaexCommand.getPrefix(event.getGuild()))) 
			return;
		
		if (event.getAuthor() != CaexBot.getJDA().getSelfUser()) {
			String[] args = event.getMessage().getContentRaw().split(" ");
			String command = args[0].replace(CaexCommand.getPrefix(event.getGuild()), "").toLowerCase();
			String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
			Optional<DraconicCommand> c = cmds.stream().filter(dc -> dc.getDraconicAlias().contains(command)).findFirst();
			
			if(c.isPresent()){
				CaexCommand cmd = c.get().getCommand();
				
				cl.callCommand(event, finalArgs, cmd);
			}
		}
	}
}

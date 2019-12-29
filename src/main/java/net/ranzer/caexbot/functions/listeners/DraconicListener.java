package net.ranzer.caexbot.functions.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DraconicListener extends ListenerAdapter{

	private static DraconicListener dl;
	List<DraconicCommand> cmds = new ArrayList<>();
	
	public static DraconicListener getInstance() {
		if (dl==null) dl = new DraconicListener();
		return dl;
	}

	private DraconicListener(){}

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
				
				CommandListener.getInstance().callCommand(event, finalArgs, cmd);
			}
		}
	}
}

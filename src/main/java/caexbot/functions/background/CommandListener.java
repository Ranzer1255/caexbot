package caexbot.functions.background;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.CaexBot;
import caexbot.commands.CaexCommand;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	private List<CaexCommand> cmds = new ArrayList<CaexCommand>();
	
	public CommandListener addCommand(CaexCommand cmd){
		this.cmds.add(cmd);
		return this;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		User author = event.getAuthor();
		String message = event.getMessage().getRawContent();
		
		if(!message.toLowerCase().startsWith(CaexCommand.getPrefix(event.getGuild()).toLowerCase()))return;
		
		if (author != CaexBot.getJDA().getSelfUser()) {
			String[] args = message.split(" ");
			String command = args[0].replace(CaexCommand.getPrefix(event.getGuild()), "").toLowerCase();
			String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
			TextChannel channel = event.getTextChannel();
			Optional<CaexCommand> c = cmds.stream().filter(cc -> cc.getAlias().contains(command)).findFirst();
			
			if(c.isPresent()){
				CaexCommand cmd = c.get();
				
				new Thread() {
					@Override
					public void run(){
						cmd.runCommand(finalArgs, author, channel, event);
						interrupt();
					}
				}.start();
			}
		} 
	}

	public List<CaexCommand> getCommands() {
		
		return cmds;
	}

}

package caexbot.functions.background;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.CaexBot;
import caexbot.commands.CaexCommand;
import caexbot.util.Logging;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	private List<CaexCommand> cmds = new ArrayList<CaexCommand>();
	
	public CommandListener addCommand(CaexCommand cmd){
		
		this.cmds.add(cmd);
		return this;
	}
	
	@Deprecated
	public String getPrefix() {
		return CaexCommand.getPrefix();
	}

	@Deprecated
	public void setPrefex(String prefix) {
		CaexCommand.setPrefex(prefix);
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		User author = event.getAuthor();
		String message = event.getMessage().getRawContent();
		
		if(!message.startsWith(CaexCommand.getPrefix()))return;
		
		if (author != CaexBot.getJDA().getSelfInfo()) {
			String[] args = message.split(" ");
			String command = args[0].replace("!", "");
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

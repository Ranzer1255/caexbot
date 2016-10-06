package caexbot.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.functions.background.CommandListener;
import caexbot.util.Logging;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class HelpCommand extends CaexCommand {

	private CommandListener cmds;
	
	public HelpCommand(CommandListener cmds) {
		this.cmds=cmds;
	}
	
//	(aliases={"help"}, description="Show a list of commands")

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		Logging.debug("Help called");

		StringBuilder sb = new StringBuilder();
		
		if(args.length==1){
			Optional<CaexCommand> c = cmds.getCommands().stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
			if(c.isPresent()){
				CaexCommand cmd = c.get();
				
				sb.append(("**["+getPrefix())).append(cmd.getAlias()).append("]** \n")
				  .append(cmd.getDescription()).append("\n")
				  .append(cmd.getUsage());
			}
		}else{
			for (CaexCommand cmd : cmds.getCommands()) {
				sb.append(("**["+getPrefix()));
				sb.append(cmd.getAlias().get(0));
				sb.append("]** ");
				sb.append(cmd.getUsage()+"\n");	
			}
			
			channel.sendMessage(sb.toString());
		}
	}

	@Override
	public String getUsage() {

		return getPrefix()+"help [command]";
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("help", "h");
	}

	@Override
	public String getDescription() {
		return "Gives a list of avaliable commands\nGives a detailed breakdown of supplied command";
	}
}

package caexbot.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.functions.background.CommandListener;
import caexbot.util.Logging;
import caexbot.util.StringUtil;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class HelpCommand extends CaexCommand {

	private CommandListener cmds;
	
	public HelpCommand(CommandListener cmds) {
		this.cmds=cmds;
	}

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		Logging.debug("Help called");

		StringBuilder sb = new StringBuilder();
		
		if(args.length==1){
			Logging.debug("help with arg (" +args[0]+")");
			Optional<CaexCommand> c = cmds.getCommands().stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
			Logging.debug(String.valueOf(c.isPresent()));
			if(c.isPresent()){
				CaexCommand cmd = c.get();
				
				sb.append("**Alias:** ")	 .append("[").append(StringUtil.cmdArrayToString(cmd.getAlias(), ", ")).append("]\n")
				  .append("**Description** ").append(cmd.getDescription()).append("\n")
				  .append("**Usage: **")	 .append(cmd.getUsage());
			}
		}else{
			for (CaexCommand cmd : cmds.getCommands()) {
				sb.append("**[");
				sb.append(StringUtil.cmdArrayToString(cmd.getAlias(), ", "));
				sb.append("]:** ");
				sb.append(cmd.getDescription()+"\n");	
			}
			
		}
		channel.sendMessage(sb.toString());
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
		return "Gives a list of avaliable command";
	}
}

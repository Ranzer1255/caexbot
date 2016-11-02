package caexbot.commands.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.commands.CaexCommand;
import caexbot.functions.games.zdice.subcommands.*;
import caexbot.util.StringUtil;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ZomDiceCommand extends CaexCommand {

	List<CaexCommand> zomSubCommands = new ArrayList<>();
	
	public ZomDiceCommand() {
		zomSubCommands.add(new ZomStartCommand());
		zomSubCommands.add(new ZomJoinCommand());
		zomSubCommands.add(new ZomRollCommand());
		zomSubCommands.add(new ZomEndTurnCommand());
		zomSubCommands.add(new ZomScoreCommand());
	}
	
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if(args.length!=1){
			channel.sendMessage(author.getAsMention() + getUsage());
		}
		
		Optional<CaexCommand> c = zomSubCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
		
		if(!c.isPresent()){
			channel.sendMessage(invalidUsage());
			return;
		}
		
		c.get().runCommand(args, author, channel, event);

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("zomdice","z","zdice","zd");
	}

	@Override
	public String getDescription() {
		return "play a game of Zombie Dice!";
	}

	@Override
	public String getUsage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ")).append("]** ");
		
		sb.append("<sub command>\n");
		sb.append("    __Sub Commands__\n");
		for (CaexCommand cmd : zomSubCommands) {
			sb.append("  **<").append(StringUtil.arrayToString(cmd.getAlias(), ", "));
			sb.append(">** ").append(cmd.getDescription()).append("\n");
		}
		
		return sb.toString();
	}
	
	public String invalidUsage(){
		return "I'm sorry, i didn't understand\n" +getUsage();
	}

}

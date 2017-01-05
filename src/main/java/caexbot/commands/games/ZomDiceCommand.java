package caexbot.commands.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.functions.games.zdice.subcommands.*;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ZomDiceCommand extends CaexCommand implements Describable{

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
			channel.sendMessage(author.getAsMention() + getUsage(event.getGuild())).queue();
		}
		
		Optional<CaexCommand> c = zomSubCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
		
		if(!c.isPresent()){
			channel.sendMessage(invalidUsage(event.getGuild())).queue();
			return;
		}
		
		c.get().runCommand(args, author, channel, event);

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("zomdice","z","zdice","zd");
	}

	@Override
	public String getShortDescription() {
		return "play a game of Zombie Dice!";
	}

	@Override
	public String getLongDescription() {
		// TODO make getLongDescription
		return getShortDescription();
	}
	
	@Override
	public String getUsage(Guild g) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ", g)).append("]** ");
		
		sb.append("<sub command>\n");
//		sb.append("    __Sub Commands__\n"); TODO conform to new framework of Describabe for subcommands
//		for (CaexCommand cmd : zomSubCommands) {
//			sb.append("  **<").append(StringUtil.arrayToString(cmd.getAlias(), ", "));
//			sb.append(">** ").append(cmd.getDescription()).append("\n");
//		}
		
		return sb.toString();
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.GAME;
	}
	
	@Override
	public String invalidUsage(Guild g){
		return "I'm sorry, i didn't understand\n" +getUsage(g);
	}

}

package net.ranzer.caexbot.commands.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.games.zdice.subcommands.ZomEndTurnCommand;
import net.ranzer.caexbot.functions.games.zdice.subcommands.ZomJoinCommand;
import net.ranzer.caexbot.functions.games.zdice.subcommands.ZomQuitCommand;
import net.ranzer.caexbot.functions.games.zdice.subcommands.ZomRollCommand;
import net.ranzer.caexbot.functions.games.zdice.subcommands.ZomScoreCommand;
import net.ranzer.caexbot.functions.games.zdice.subcommands.ZomStartCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ZomDiceCommand extends CaexCommand implements Describable{

	List<CaexCommand> zomSubCommands = new ArrayList<>();
	
	public ZomDiceCommand() {
		zomSubCommands.add(new ZomStartCommand());
		zomSubCommands.add(new ZomJoinCommand());
		zomSubCommands.add(new ZomRollCommand());
		zomSubCommands.add(new ZomEndTurnCommand());
		zomSubCommands.add(new ZomScoreCommand());
		zomSubCommands.add(new ZomQuitCommand());
	}
	
	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		if(args.length!=1){
			event.getChannel().sendMessage(event.getAuthor().getAsMention() + getUsage(event.getGuild())).queue();
		}
		
		Optional<CaexCommand> c = zomSubCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
		
		if(!c.isPresent()){
			event.getChannel().sendMessage(invalidUsage(event.getGuild())).queue();
			return;
		}
		
		c.get().runCommand(args, event);

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
		StringBuilder sb = new StringBuilder();
		
		
		sb.append(getShortDescription() +"\n\n");
		
		for (CaexCommand cmd : zomSubCommands) {
			sb.append(
				String.format("`%s`: %s\n", cmd.getName(), ((Describable)cmd).getShortDescription())
			);
		}
		
		sb.append("Zombie Dice's rules can be found [Here](https://github.com/Sgmaniac1255/caexbot/wiki/Zombie-Dice)");
		return sb.toString();
	}
	
	@Override
	public String getUsage(Guild g) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("`%s%s {", getPrefix(g), getName()));
		for(CaexCommand cmd : zomSubCommands){
			sb.append(String.format("%s | ", cmd.getName()));
		}
		sb.delete(sb.length()-3,sb.length());
		sb.append("}`");
				
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

	@Override
	public boolean isApplicableToPM() {
		return false;
	}
	
	@Override
	public boolean hasSubcommands() {
		return true;
	}
	
	@Override
	public List<CaexCommand> getSubcommands() {
		return zomSubCommands;
	}

}

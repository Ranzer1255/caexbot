package caexbot.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import caexbot.functions.games.zdice.subcommands.*;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ZomDiceCommand extends CaexCommand {

	List<ZomDiceCommand> zomSubCommands;
	
	public ZomDiceCommand() {
		zomSubCommands.add(new ZomStartCommand());
		zomSubCommands.add(new ZomJoinCommand());
		zomSubCommands.add(new ZomRollCommand());
		zomSubCommands.add(new ZomScoreCommand());
	}
	
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if(args.length!=1){
			channel.sendMessage(author.getAsMention() + getUsage());
		}
		
		Optional<ZomDiceCommand> c = zomSubCommands.stream().filter(cc -> cc.getAlias().contains(args[0])).findFirst();
		
		if(!c.isPresent()){
			invalidUsage();
			return;
		}
		
		c.get().runCommand(args, author, channel, event);

	}

	@Override
	public List<String> getAlias() {
		// TODO Auto-generated method stub
		return Arrays.asList("zomdice","z","zdice","zd");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void invalidUsage(){
		// TODO fill in usage
	}

}

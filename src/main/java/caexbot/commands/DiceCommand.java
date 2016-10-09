package caexbot.commands;

import java.util.Arrays;
import java.util.List;

import caexbot.functions.dice.DiceParser;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class DiceCommand extends CaexCommand{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if(!(args.length==1)){
			invalidUsage();
		}
		
		try {
			channel.sendMessage(author.getAsMention()+": "+DiceParser.parseDiceString(args[0]).Result());
		} catch (IllegalArgumentException e) {
			channel.sendMessage("I'm sorry i didn't uderstand \""+ e.getMessage()+"\" please use standard RPG format.");
		}
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("roll","r");
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

}

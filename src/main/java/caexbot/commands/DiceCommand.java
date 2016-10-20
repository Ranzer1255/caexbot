package caexbot.commands;

import java.util.Arrays;
import java.util.List;

import caexbot.functions.dice.DiceParser;
import caexbot.util.StringUtil;
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
		return "Roll the Dice! Standard RPG dice format";
	}

	@Override
	public String getUsage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ")).append("]** ").append("<Standard RPG Dice format>\n");
		sb.append("  __Examples__\n");
		sb.append("```\n"
				+ "  1d20\n"
				+ "  1d6+3\n"
				+ "  3d10\n"
				+ "```");
		
		return sb.toString();
	}

}

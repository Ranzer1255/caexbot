package caexbot.commands.games;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.functions.dice.DiceParser;
import caexbot.functions.dice.DieRoll;
import caexbot.functions.dice.IllegalDiceFormatException;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiceCommand extends CaexCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		System.out.println(event.getMessage().getContent());
		if(!(args.length==1)){
			invalidUsage(event.getGuild());
		}
		DieRoll dr = DiceParser.parseDiceString(args[0]);
		try {
			String message = author.getAsMention()+": "+dr.verboseResult();
			channel.sendMessage(message).queue();
		} catch (IllegalDiceFormatException e) {
			channel.sendMessage("I'm sorry i didn't understand \""+ e.getMessage()+"\" please use standard RPG format.").queue();
		} catch (IllegalArgumentException e) {
			if (e.getMessage().equals("Provided text for message must be less than 2000 characters in length")) {
				channel.sendMessage("Sorry "+event.getAuthor().getAsMention()+" Result too long to display each die\n"+ dr.result()).queue();
			}
		}
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("roll","r");
	}

	@Override
	public String getShortDescription() {
		return "Roll the Dice! Standard RPG dice format";
	}

	@Override
	public String getLongDescription() {
		return "Rolls dice using the standard RPG dice format\n"
				+ "Currently it will only handle 1 die type and a single modifier\n\n"
				+ "__Examples__\n"
				+ "```\n"
				+ "1d20+5\n"
				+ "8d6\n"
				+ "1d100\n"
				+ "```";
	}
	
	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" <basic rpg notation>`";
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}
	
	@Override
	public String invalidUsage(Guild g) {
		// TODO make invalidUsage
		return super.invalidUsage(g);
	}

}

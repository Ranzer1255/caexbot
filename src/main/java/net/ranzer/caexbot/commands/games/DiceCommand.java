package net.ranzer.caexbot.commands.games;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.dice.Dice;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.util.StringUtil;

/**
 * <p> Credit where Credit is due:
 * <p>I borrowed the dice roller code from <a href="https://github.com/JoshCode/gilmore">GilmoreBot</a>
 *  by <a href=https://github.com/JoshCode>JoshCode</a>
 * @author Ranzer 
 *
 */
public class DiceCommand extends BotCommand implements Describable{

	private static final int MAX_MESSAGE_LENGTH = 1000;
	private static final String SCO_DICE = "dice";
	private static final String SCO_HIDDEN = "hidden";

	@Override
	public void processSlash(SlashCommandEvent event) {
		String expression = Objects.requireNonNull(event.getOption(SCO_DICE)).getAsString();
		Dice dice = new Dice(expression);
		int result = dice.roll();
		OptionMapping isHidden = event.getOption(SCO_HIDDEN);
		event.reply(String.format("%s = %d",
				getRollBreakdown(expression,dice),
				result)).setEphemeral(isHidden != null && isHidden.getAsBoolean()).queue();
	}

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {

		if(!(args.length<1)){
			invalidUsage(event.getGuild());
		}

		String expression = StringUtil.arrayToString(args," ");

		Dice dice = new Dice(expression);
		
		int result = dice.roll();

		event.getChannel().sendMessage(String.format("%s: %s = %d", 
				event.getAuthor().getAsMention(),
				getRollBreakdown(expression, dice),
				result))
		.queue();

	}

	private String getRollBreakdown(String expression, Dice dice) {
		return (dice.getBreakdown().length() < MAX_MESSAGE_LENGTH) ? dice.getBreakdown() :
				"(Message to long to display each die)" + expression.substring(1);
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
				+ "Usage: ![roll|dice] [expression]\n"
				+ "for example: !roll 1d20 + 5 [to hit]\n"
				+ "[comment]: this is ignored\n"
				+ "2d20khX: keep the X highest dice\n"
				+ "2d20klX: keep the X lowest dice\n"
				+ "4d6r<X: re-roll every die lower than X\n"
				+ "4d6ro<X: re-roll every die lower than X, but only once\n"
				+ "1d10!: exploding die - every time you roll a critical, add an extra die";
	}
	
	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" <basic rpg notation>`";
	}
	
	@Override
	public Category getCategory() {
		return Category.CHAT;
	}
	
	@Override
	public boolean isApplicableToPM() {
		return true;
	}

	@Override
	public CommandData getCommandData() {
		CommandData rtn = new CommandData(getName(),getShortDescription());

		rtn.addOption(OptionType.STRING,SCO_DICE,"example: 1d20+5 [dex save]",true)
				.addOption(OptionType.BOOLEAN,SCO_HIDDEN,"Hide this roll?");

		return rtn;
	}
}

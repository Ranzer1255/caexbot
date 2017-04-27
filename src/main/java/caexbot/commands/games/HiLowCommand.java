package caexbot.commands.games;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HiLowCommand extends CaexCommand implements Describable {

	@Override
	public Catagory getCatagory() {
		return Catagory.GAME;
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" xp {high | low}`";
	}

	@Override
	public String getShortDescription() {
		return "Gamble all your hard earned XP away!";
	}

	@Override
	public String getLongDescription() {
		return "Play a Game of HiLow\n\n"
				+ ""
				+ "Choose how much you would like to bet,\n"
				+ "then decide if the number will fall Hi or Low of `50` on a scale of `1-100`\n"
				+ "(Side note a hit on 50 will be counted as `low`)\n\n"
				+ "If you win, Congradulations! Your bet will be doubled and added back to your total\n"
				+ "If you lose, <sad trombone>, Your bet is gone and your hard earned xp is washed away\n\n"
				+ "You must have a minimum of 1000xp to play, and you may only bet up to 50% of your current xp.\n"
				+ "GOOD LUCK!";
	}

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("hilow", "hl");
	}

}

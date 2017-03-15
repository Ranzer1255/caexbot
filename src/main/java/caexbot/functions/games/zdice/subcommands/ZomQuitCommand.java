package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;
import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ZomQuitCommand extends CaexCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		ZomDiceDiscordControler.getInstance().removePlayer(author);

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("quit","leave","q","l");
	}

	@Override
	public String getShortDescription() {
		return "Leave the game.";
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.GAME;
	}

	@Override
	public String getUsage(Guild g) {
		return null;
	}

	@Override
	public String getLongDescription() {
		return getShortDescription();
	}

}

package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ZomQuitCommand extends AbstractZombieCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		ZomDiceDiscordControler.getInstance().removePlayer(event.getAuthor());

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("quit","leave","q","l");
	}

	@Override
	public String getShortDescription() {
		return "Leave the game.";
	}

}

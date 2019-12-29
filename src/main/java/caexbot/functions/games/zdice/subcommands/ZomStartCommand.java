package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ZomStartCommand extends AbstractZombieCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		ZomDiceDiscordControler.getInstance().startGameInChannel(event.getTextChannel());
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("start");
	}

	@Override
	public String getShortDescription() {
		return "Start the game.";
	}
	
	@Override
	public String getLongDescription() {
		return super.getLongDescription()+
				"Must have more than 1 player currently registered to play";
	}

}

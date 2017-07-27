package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
	public Catagory getCatagory() {
		return Catagory.GAME;
	}

	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

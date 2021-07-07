package net.ranzer.caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ZomRollCommand extends AbstractZombieCommand implements Describable{

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		ZomDiceDiscordControler.getInstance().setActiveChannel(event.getTextChannel());
		ZomDiceDiscordControler.getInstance().roll(event.getAuthor());
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("roll", "r");
	}

	@Override
	public String getShortDescription() {
		return "Roll the dice.";
	}
}

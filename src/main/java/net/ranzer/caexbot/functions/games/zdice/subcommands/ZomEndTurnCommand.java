package net.ranzer.caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ZomEndTurnCommand extends AbstractZombieCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		ZomDiceDiscordControler.getInstance().setActiveChannel(event.getTextChannel());
		ZomDiceDiscordControler.getInstance().endTurn(event.getAuthor());
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("end","done","e","d");
	}

	@Override
	public String getShortDescription() {
		return "End your turn.";
	}

}

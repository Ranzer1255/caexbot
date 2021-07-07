package net.ranzer.caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ZomJoinCommand extends AbstractZombieCommand implements Describable{

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		if(ZomDiceDiscordControler.getInstance().addPlayer(event.getAuthor()))
			event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", you have been added to the game").queue();
		else
			event.getChannel().sendMessage(event.getAuthor().getAsMention() +", you're already on the list").queue();
	}
	
	@Override
	public List<String> getAlias(){
		return Arrays.asList("join");
	}

	@Override
	public String getShortDescription() {
		return "Join the Game.";
	}
}

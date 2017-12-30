package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.Describable;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ZomJoinCommand extends AbstractZombieCommand implements Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
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

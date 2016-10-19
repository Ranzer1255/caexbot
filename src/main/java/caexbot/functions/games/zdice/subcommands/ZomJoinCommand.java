package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexSubCommand;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ZomJoinCommand extends CaexSubCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if(ZomDiceDiscordControler.getInstance().addPlayer(author))
			channel.sendMessage(author.getAsMention() + ", you have been added to the game");
		else
			channel.sendMessage(author.getAsMention() +", you're already on the list");
	}
	
	@Override
	public List<String> getAlias(){
		return Arrays.asList("join");
	}

	@Override
	public String getDescription() {
		return "Join the Game.";
	}
}

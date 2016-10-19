package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexSubCommand;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ZomEndTurnCommand extends CaexSubCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		ZomDiceDiscordControler.getInstance().setActiveChannel(channel);
		ZomDiceDiscordControler.getInstance().endTurn();
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("end", "score", "done","e","s","d");
	}

	@Override
	public String getDescription() {
		return "End your turn.";
	}

}
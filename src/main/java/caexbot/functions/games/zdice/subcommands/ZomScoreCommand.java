package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexSubCommand;
import caexbot.functions.games.zdice.controlers.UserPlayerAdapter;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ZomScoreCommand extends CaexSubCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		int brains = ZomDiceDiscordControler.getInstance().getScore(new UserPlayerAdapter(author));
		channel.sendMessage(String.format("%s: your score is **%d** Brains!", author.getAsMention(), brains));
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("score","s");
	}

	@Override
	public String getDescription() {
		return "See your current number of brains.";
	}

}

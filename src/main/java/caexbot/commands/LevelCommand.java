package caexbot.commands;

import java.util.Arrays;
import java.util.List;

import caexbot.functions.levels.expTable;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class LevelCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		channel.sendMessage(String.format("%s: Current XP: %d", author.getAsMention(), expTable.getInstance().getXP(channel.getGuild(),author)));
		

	}

	@Override
	public List<String> getAlias() {
		// TODO Auto-generated method stub
		return Arrays.asList("xp", "level");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

}

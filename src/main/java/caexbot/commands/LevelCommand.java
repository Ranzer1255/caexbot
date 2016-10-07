package caexbot.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import caexbot.functions.levels.UserLevel;
import caexbot.functions.levels.expTable;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class LevelCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if (args.length>0){
			if (args[0].equals("rank")){
				channel.sendMessage(rankMessage(args,author,channel,event));
				return;
			}
		}
		channel.sendMessage(String.format("%s: Current Lvl: %d XP: %d", author.getAsMention(), expTable.getInstance().getLevel(channel.getGuild(),author),expTable.getInstance().getXP(channel.getGuild(),author)));
		

	}


	@Override
	public List<String> getAlias() {
		// TODO Auto-generated method stub
		return Arrays.asList("xp", "level");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "lists your level and experence.  Add \"rank\" the leaderboard.";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	private String rankMessage(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder msg = new StringBuilder();
		
		List<Map.Entry<User, UserLevel>> rankings = expTable.getInstance().getGuildRankings(channel.getGuild());
		
		msg.append("__***Current Leaderboard***__\n\n");
		for (Map.Entry<User, UserLevel> entry : rankings) {
			String userName;
			if ((userName = channel.getGuild().getNicknameForUser(entry.getKey())) == null)
					userName = entry.getKey().getUsername(); 
			msg.append("__**").append(userName).append("**__:\t*Level:* **").append(entry.getValue().getLevel())
			   .append("** with __").append(entry.getValue().getXP()).append("*xp*__\n\n");
		}
		
		return msg.toString();
	}
}

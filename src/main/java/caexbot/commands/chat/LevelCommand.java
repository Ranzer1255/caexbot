package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import caexbot.commands.CaexCommand;
import caexbot.functions.levels.UserLevel;
import caexbot.functions.levels.expTable;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LevelCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if (args.length>0){
			if (args[0].equals("rank")){
				channel.sendMessage(rankMessage(args,author,channel,event)).queue();
				return;
			}
		}
		channel.sendMessage(String.format("%s: Current Lvl: %d XP: %d", author.getAsMention(), expTable.getInstance().getLevel(channel.getGuild(),author),expTable.getInstance().getXP(channel.getGuild(),author))).queue();
		

	}


	@Override
	public List<String> getAlias() {
		return Arrays.asList("xp", "level");
	}

	@Override
	public String getDescription() {
		return "lists your level and experence.";
	}

	@Override
	public String getUsage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ")).append("]** ").append("<Sub Command>\n");
		sb.append("        __Sub Commands__\n");
		sb.append("**    default:** lists your XP and Level\n")
		  .append("**    [rank]** see current standings for the server");
		

		
		return sb.toString();
	}

	private String rankMessage(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder msg = new StringBuilder();
		
		List<Map.Entry<User, UserLevel>> rankings = expTable.getInstance().getGuildRankings(channel.getGuild());
		
		msg.append("__***Current Leaderboard***__\nall XP is beta and will be reset\n\n");
		for (Map.Entry<User, UserLevel> entry : rankings) {
			msg.append(
				String.format("__**%s**__:\t*Level:* **%s** with __%sxp*__\n\n", 
					channel.getGuild().getMember(entry.getKey()).getEffectiveName(), 
					entry.getValue().getLevel(),
					entry.getValue().getXP()
				)
			);
		}
		
		return msg.toString();
	}
}

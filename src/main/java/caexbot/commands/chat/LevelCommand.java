package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import caexbot.data.GuildManager;
import caexbot.functions.levels.UserLevel;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LevelCommand extends CaexCommand implements DraconicCommand,Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if (args.length>0){
			if (args[0].equals("rank")){
				channel.sendMessage(rankMessage(args,author,channel,event)).queue();
				return;
			}
		}
		
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(event.getMember().getEffectiveName(), null, author.getAvatarUrl())
			.setColor(event.getMember().getColor())
			.setThumbnail(author.getAvatarUrl())
			.setTitle("XP Breakdown")
			.addField("XP", String.format("%dxp",GuildManager.getGuildData(event.getGuild()).getXP(author)), true)
			.addField("Level", String.format("Lvl: %d", GuildManager.getGuildData(event.getGuild()).getLevel(author)), true);
		
		MessageBuilder mb = new MessageBuilder();
		channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
	
//		channel.sendMessage(String.format("%s: Current Lvl: %d XP: %d", author.getAsMention(), expTable.getInstance().getLevel(channel.getGuild(),author),expTable.getInstance().getXP(channel.getGuild(),author))).queue();

	}


	@Override
	public List<String> getAlias() {
		return Arrays.asList("xp", "level");
	}

	@Override
	public String getShortDescription() {
		return "lists your level and experence.";
	}
	
	@Override
	public String getLongDescription() {
		// TODO make getLongDescription
		return getShortDescription();
	}
	
	@Override
	public String getUsage(Guild g) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ",g)).append("]** ").append("<Sub Command>\n");
		sb.append("        __Sub Commands__\n");
		sb.append("**    default:** lists your XP and Level\n")
		  .append("**    [rank]** see current standings for the server");
		

		
		return sb.toString();
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

	private String rankMessage(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder msg = new StringBuilder();
		
		List<Map.Entry<User, UserLevel>> rankings = GuildManager.getGuildData(event.getGuild()).getGuildRankings();
		
		msg.append("__***Current Leaderboard***__\nall XP is beta and will be reset\n\n");
		int index=0;
		for (Map.Entry<User, UserLevel> entry : rankings) {
			if(index++>=10) break;
			msg.append(
				String.format("__**%s**__:\t*Level:* **%s** with __%sxp*__\n", 
					channel.getGuild().getMember(entry.getKey()).getEffectiveName(), 
					entry.getValue().getLevel(),
					entry.getValue().getXP()
				)
			);
		}
		
		return msg.toString();
	}


	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("tawura_authot");
	}
}

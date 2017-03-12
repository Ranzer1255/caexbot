package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import caexbot.data.GuildManager;
import caexbot.functions.levels.UserLevel;
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
			.setTitle("XP Breakdown",null)
			.addField("XP", String.format("%dxp",GuildManager.getGuildData(event.getGuild()).getXP(author)), true)
			.addField("Level", String.format("Lvl: %d", GuildManager.getGuildData(event.getGuild()).getLevel(author)), true);
		
		MessageBuilder mb = new MessageBuilder();
		channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();

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
		return "This command returns the caller's current XP and level.\n"
				+ "With the `rank` option, this command will return the top 10 users in the guild";
	}
	
	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getAlias().get(0)+" [rank]`";
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

	//TODO make this an embed
	private String rankMessage(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder msg = new StringBuilder();
		
		List<UserLevel> rankings = GuildManager.getGuildData(event.getGuild()).getGuildRankings();
		
		msg.append("__***Current Leaderboard***__\nall XP is beta and will be reset\n\n");
		int index=0;
		for (UserLevel entry : rankings) {
			if(index++>=10) break;
			msg.append(
				String.format("__**%s**__:\t*Level:* **%s** with __%sxp*__\n", 
					entry.getMember().getEffectiveName(), 
					entry.getLevel(),
					entry.getXP()
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

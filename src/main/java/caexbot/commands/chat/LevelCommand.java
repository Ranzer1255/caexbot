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
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LevelCommand extends CaexCommand implements DraconicCommand,Describable{

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		if (event.getChannelType().equals(ChannelType.TEXT)) {
			guildCall(args, event);
		} else if (event.getChannelType().equals(ChannelType.PRIVATE)){
			privateCall(args,event);
		}

	}


	private void privateCall(String[] args, MessageReceivedEvent event) {
		MessageChannel channel = event.getAuthor().openPrivateChannel().complete();
		
		for(Guild g : event.getAuthor().getMutualGuilds()){
			MessageBuilder mb = new MessageBuilder();
			mb.append("***__"+g.getName()+"__***");
			mb.setEmbed(memberXPEmbed(g.getMember(event.getAuthor())));
			
			channel.sendMessage(mb.build()).queue();
		}
		
	}


	private void guildCall(String[] args, MessageReceivedEvent event) {
		if (args.length>0){
			if (args[0].equals("rank")){
				event.getChannel().sendMessage(rankMessage(event)).queue();
				return;
			} 
		}
		
		MessageBuilder mb = new MessageBuilder();
		event.getChannel().sendMessage(mb.setEmbed(memberXPEmbed(event.getMember())).build()).queue();
	}


	//lays the ground work for seeing other's xp outside of the ranked list
	//TODO add ability to see individual member's XP stats by mention.
	private MessageEmbed memberXPEmbed(Member member) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl())
			.setColor(member.getColor())
			.setThumbnail(member.getUser().getAvatarUrl())
			.setTitle("XP Breakdown",null)
			.addField("XP", String.format("%,dxp",GuildManager.getGuildData(member.getGuild()).getXP(member.getUser())), true)
			.addField("Level", String.format("Lvl: %,d", GuildManager.getGuildData(member.getGuild()).getLevel(member.getUser())), true);
		return eb.build();
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
		return "This command returns the caller's current XP and level.\n\n"
				+ "`rank` option: This command will return the top 10 users in the guild";
	}
	
	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getAlias().get(0)+" [rank]`";
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}


	private Message rankMessage(MessageReceivedEvent event) {
	
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setAuthor("Current Leaderboard", null, null);
		eb.setDescription("XP is in beta and is likely to be reset");// is this still needed? i've polished the code so that its stable now
		eb.setColor(getCatagory().COLOR);
		eb.setThumbnail("http://i1.kym-cdn.com/entries/icons/original/000/021/324/photo.jpg");
		
		
		List<UserLevel> rankings = GuildManager.getGuildData(event.getGuild()).getGuildRankings();
		
		int index=0;
		for (UserLevel entry : rankings) {
			if(index++>=10) break;
			eb.addField(
					"**"+index+": **"+entry.getMember().getEffectiveName(), 
					String.format("*Level:* **%s**\n%,dxp", 
							entry.getLevel(),
							entry.getXP()
					), 
					true
			);
		}
		
		return new MessageBuilder().setEmbed(eb.build()).build();
	}


	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("tawura_authot");
	}


	@Override
	public boolean isAplicableToPM() {
		return true;
	}
}

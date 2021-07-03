package net.ranzer.caexbot.commands.chat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.functions.levels.UserLevel;
import net.ranzer.caexbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LevelCommand extends BotCommand implements DraconicCommand,Describable{

	private static final int NUMBER_OF_RANKINGS = 10;
	private static final String XP_RULES = "Earn XP by talking!\n"
			+ "You'll get 15-25xp per message once every 60 seconds";
	private static final int MAX_NAME_LENGTH = 25;


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
		
		if (args.length>0){
			if (args[0].equals("rank")){
				
				//if i call xp rank, give me everyone's guild. not just the ones i'm in.
				if(event.getAuthor().getId().equals(CaexConfiguration.getInstance().getOwner())){
					for (Guild g: event.getJDA().getGuilds()) {
						event.getChannel().sendMessage(rankMessage(g)).queue();
					}
					return;
				}
				
				for (Guild g: event.getAuthor().getMutualGuilds()) {
					event.getChannel().sendMessage(rankMessage(g)).queue();
				}
				return;
			} 
		}
		
		for(Guild g : event.getAuthor().getMutualGuilds()){
			MessageBuilder mb = new MessageBuilder();
			mb.append("***__").append(g.getName()).append("__***");
			mb.setEmbeds(memberXPEmbed(Objects.requireNonNull(event.getMember())));
			
			channel.sendMessage(mb.build()).queue();
		}
		
	}


	private void guildCall(String[] args, MessageReceivedEvent event) {
		if (args.length>0){
			if ("rank".equals(args[0])) {
				event.getChannel().sendMessage(rankMessage(event.getGuild())).queue();
				return;
			}
			
			if(!event.getMessage().getMentionedMembers().isEmpty()){
				event.getChannel().sendMessageEmbeds(memberXPEmbed(event.getMessage().getMentionedMembers().get(0))).queue();
				return;
			}
		}
		
		event.getChannel().sendMessageEmbeds(memberXPEmbed(Objects.requireNonNull(event.getMember()))).queue();
	}
	
	private MessageEmbed memberXPEmbed(Member member) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl())
			.setColor(member.getColor())
			.setThumbnail(member.getUser().getAvatarUrl())
			.setTitle("XP Breakdown",null)
			.addField("XP", String.format("%,dxp",GuildManager.getGuildData(member.getGuild()).getMemberData(member).getXP()), true)
			.addField("Level", String.format("Lvl: %,d", GuildManager.getGuildData(member.getGuild()).getMemberData(member).getLevel()), true)
			.setFooter(XP_RULES, null);
		return eb.build();
	}


	@Override
	public List<String> getAlias() {
		return Arrays.asList("xp", "level");
	}

	@Override
	public String getShortDescription() {
		return "lists your level and experience.";
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
	public Category getCategory() {
		return Category.CHAT;
	}


	private Message rankMessage(Guild guild) {
	
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setAuthor(String.format("%s Leaderboard",guild.getName()), null, null);
		eb.setDescription(XP_RULES);
		eb.setColor(getCategory().COLOR);
		eb.setThumbnail(guild.getIconUrl());
		
		
		List<UserLevel> rankings = GuildManager.getGuildData(guild).getGuildRankings();
		
		int index=0;
		for (UserLevel entry : rankings) {
			if(index++>=NUMBER_OF_RANKINGS) break;
			eb.addField(
					"**"+index+": **"+StringUtil.truncate(entry.getMember().getEffectiveName(),MAX_NAME_LENGTH), 
					String.format("*Level:* **%s**\n%,dxp", 
							entry.getLevel(),
							entry.getXP()
					), 
					true
			);
		}
		
		return new MessageBuilder()
				.setEmbeds(eb.build())
				.build();
	}

	@Override
	public List<String> getDraconicAlias() {
		return Collections.singletonList("tawura_authot");
	}

	//is applicable

	@Override
	public boolean isApplicableToPM() {
		return true;
	}
}

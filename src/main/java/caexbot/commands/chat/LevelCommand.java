package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;

import caexbot.CaexBot;
import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import caexbot.config.CaexConfiguration;
import caexbot.data.GuildManager;
import caexbot.functions.levels.RoleLevel;
import caexbot.functions.levels.UserLevel;
import caexbot.util.StringUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LevelCommand extends CaexCommand implements DraconicCommand,Describable{

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
			mb.append("***__"+g.getName()+"__***");
			mb.setEmbed(memberXPEmbed(g.getMember(event.getAuthor())));
			
			channel.sendMessage(mb.build()).queue();
		}
		
	}


	private void guildCall(String[] args, MessageReceivedEvent event) {
		if (args.length>0){
			if (args[0].equals("rank")){
				event.getChannel().sendMessage(rankMessage(event.getGuild())).queue();
				return;
			} else if (args[0].equals("roles")){
				event.getChannel().sendMessage(rollMessage(event.getGuild())).queue();
				return;
			}
			
			if(!event.getMessage().getMentionedMembers().isEmpty()){
				event.getChannel().sendMessage(memberXPEmbed(event.getMessage().getMentionedMembers().get(0))).queue();
				return;
			}
		}
		
		event.getChannel().sendMessage(memberXPEmbed(event.getMember())).queue();
	}
	
	private MessageEmbed memberXPEmbed(Member member) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl())
			.setColor(member.getColor())
			.setThumbnail(member.getUser().getAvatarUrl())
			.setTitle("XP Breakdown",null)
			.addField("XP", String.format("%,dxp",CaexBot.GUILD_MANAGER.getGuildData(member.getGuild()).getXP(member.getUser())), true)
			.addField("Level", String.format("Lvl: %,d", CaexBot.GUILD_MANAGER.getGuildData(member.getGuild()).getLevel(member.getUser())), true)
			.setFooter(XP_RULES, null);
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
				+ "`rank` option: This command will return the top 10 users in the guild\n"
				+ "`roles` option: this command will return the top 10 roles in the guild";
	}
	
	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getAlias().get(0)+" [{rank | roles}]`";
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}


	private Message rankMessage(Guild guild) {
	
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setAuthor(String.format("%s Leaderboard",guild.getName()), null, null);
		eb.setDescription(XP_RULES);
		eb.setColor(getCatagory().COLOR);
		eb.setThumbnail(guild.getIconUrl());
		
		
		List<UserLevel> rankings = CaexBot.GUILD_MANAGER.getGuildData(guild).getGuildRankings();
		
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
				.setEmbed(eb.build())
				.build();
	}

	private Message rollMessage(Guild guild) {
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		List<RoleLevel> rankings = CaexBot.GUILD_MANAGER.getGuildData(guild).getRoleRankings();
		
		eb.setAuthor("Current Role Leaderboard", null, null);
		eb.setDescription(XP_RULES + "\n[wip] role exclusion to come");
		eb.setThumbnail(guild.getIconUrl());
		eb.setColor(rankings.get(0).ROLE.getColor());
				
		int index = 0;
		for (RoleLevel entry : rankings) {
			if(index++>=NUMBER_OF_RANKINGS) break;
			eb.addField(
					"**"+index+": **"+StringUtil.truncate(entry.ROLENAME,MAX_NAME_LENGTH), 
					String.format("%,dxp",
							entry.getXp()
					), 
					true
			);
		}
		
		return mb.setEmbed(eb.build()).build();
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

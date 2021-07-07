package net.ranzer.caexbot.commands.admin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.ranzer.caexbot.CaexBot;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InfoCommand extends BotCommand implements DraconicCommand, Describable{

	private static final List<Permission> REQUIRED_PERMISSIONS = Arrays.asList(
		Permission.NICKNAME_CHANGE,
		Permission.VIEW_CHANNEL,
		Permission.MESSAGE_WRITE,
		Permission.MESSAGE_EMBED_LINKS,
		Permission.MESSAGE_ATTACH_FILES,
		Permission.MESSAGE_ADD_REACTION,
		Permission.VOICE_CONNECT,
		Permission.VOICE_SPEAK
	);

	@Override
	public void processSlash(SlashCommandEvent event) {
		OptionMapping option = event.getOption("stats");
		if(option!=null && option.getAsString().equals("stats") ){
			event.replyEmbeds(statusEmbed(event.getJDA().getSelfUser()).build()).setEphemeral(true).queue();
			return;
		}
		event.replyEmbeds(infoEmbed(event.getJDA().getSelfUser()).build()).setEphemeral(true).queue();
	}

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {

		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		User bot = event.getJDA().getSelfUser();
		
		if (event.isFromGuild()) {
			eb.setColor(Objects.requireNonNull(event.getGuild().getMember(bot)).getColor());
		}
		
		if (args.length == 1&&args[0].equals("stats")) { // 1 argument !info stats
			eb = statusEmbed(event.getJDA().getSelfUser());
		} else { // !info
			eb = infoEmbed(bot);
		}

		event.getChannel().sendMessage(mb.setEmbeds(eb.build()).build()).queue();

	}

	static public EmbedBuilder statusEmbed(User bot) {
		EmbedBuilder rtn = coreEmbed(bot);
		rtn.addField("Guilds", String.valueOf(bot.getJDA().getGuilds().size()), false)
		  .addField("Users", countNonBotUsers(bot.getJDA()), true)
		  .addField("Bots", countBotUsers(bot.getJDA()), true)
		  .addField("Up Time",getUpTime(), true)
		  .addField("Game", Objects.requireNonNull(bot.getJDA().getPresence().getActivity()).getName(), true);
		return rtn;
	}

	static public EmbedBuilder infoEmbed(User bot) {
		EmbedBuilder rtn = coreEmbed(bot);
		  rtn.addField("Version", CaexConfiguration.getInstance().getVersion(), true)
		  .addField("Language", "Java", true)
		  .addField("Artwork", "Mellie", false)
		  .addField("Invite me!", inviteLinkBuilder(bot), true)
		  .addField("GitHub Repo", "[GitHub](https://github.com/ranzer1255/caexbot)\n[Bugs and Suggestions](https://github.com/ranzer1255/caexbot/issues)", true)
		  .setFooter("Please report bugs or suggestions in the link above", null);
		return rtn;
	}

	static private EmbedBuilder coreEmbed(User bot) {
		EmbedBuilder rtn = new EmbedBuilder();
		rtn.setAuthor("Caex Hewa", "https://github.com/Sgmaniac1255/caexbot", bot.getAvatarUrl())
		  .setTitle("A Discord Chatbot",null)
		  .setDescription("Written by Ranzer")
		  .setThumbnail(bot.getAvatarUrl());
		return rtn;
	}

	private static String getUpTime() {
		StringBuilder sb = new StringBuilder();
		LocalDateTime now = LocalDateTime.now();
		
		if(CaexBot.START_TIME.until(now, ChronoUnit.YEARS)!=0){
			sb.append(CaexBot.START_TIME.until(now, ChronoUnit.YEARS)).append(" Yrs, ");
			now=now.minusYears(CaexBot.START_TIME.until(now, ChronoUnit.YEARS));
		}
		if(CaexBot.START_TIME.until(now, ChronoUnit.MONTHS)!= 0){
			sb.append(CaexBot.START_TIME.until(now, ChronoUnit.MONTHS)).append(" Mths, ");
			now=now.minusMonths(CaexBot.START_TIME.until(now, ChronoUnit.MONTHS));
		}
		if(CaexBot.START_TIME.until(now, ChronoUnit.DAYS)!=0){
			sb.append(CaexBot.START_TIME.until(now, ChronoUnit.DAYS)).append(" Days, ");
			now=now.minusDays(CaexBot.START_TIME.until(now, ChronoUnit.DAYS));
		}
		if(CaexBot.START_TIME.until(now, ChronoUnit.HOURS)!=0){
			sb.append(CaexBot.START_TIME.until(now, ChronoUnit.HOURS)).append(" Hrs, ");
			now=now.minusHours(CaexBot.START_TIME.until(now, ChronoUnit.HOURS));
		}
		if(CaexBot.START_TIME.until(now, ChronoUnit.MINUTES)!=0){
			sb.append(CaexBot.START_TIME.until(now, ChronoUnit.MINUTES)).append(" Mins, ");
			now=now.minusMinutes(CaexBot.START_TIME.until(now, ChronoUnit.MINUTES));
		}
		if(CaexBot.START_TIME.until(now, ChronoUnit.SECONDS)!=0){
			sb.append(CaexBot.START_TIME.until(now, ChronoUnit.SECONDS)).append(" Secs, ");
		}
		
		
		sb.delete(sb.length()-2, sb.length());
		sb.append(".");
		
		return sb.toString();
	}

	private static String countBotUsers(JDA api) {
		int count = 0;
		
		for(User u:api.getUsers()){
			if (u.isBot()){
				count++;
			}
		}
		
		return String.valueOf(count);
	}

	private static String countNonBotUsers(JDA api) {
		int count = 0;
		
		for(User u:api.getUsers()){
			if (!u.isBot()){
				count++;
			}
		}
		
		return String.valueOf(count);
	}

	private static String inviteLinkBuilder(User bot) {
		@SuppressWarnings("StringBufferReplaceableByString")
		StringBuilder sb = new StringBuilder();

		sb.append("[No Permissions]")
		  .append("(").append(CaexBot.getJDA().getInviteUrl()).append(")\n")
		  .append("[Limited Permissions]")
		  .append("(").append(CaexBot.getJDA().getInviteUrl(REQUIRED_PERMISSIONS)).append(")\n")
		  .append("[Admin Permissions]")
		  .append("(").append(CaexBot.getJDA().getInviteUrl(Permission.ADMINISTRATOR)).append(")\n");

		return sb.toString();
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" [stats]`";

	}

	@Override
	public List<String> getAlias() {

		return Arrays.asList("info", "i");
	}

	@Override
	public String getShortDescription() {

		return "Information about Caex and Author";
	}

	@Override
	public List<String> getDraconicAlias() {
		return Collections.singletonList("vucot");
	}

	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public String getLongDescription() {
		return    "This command gives detailed information about the bot\n\n"
				+ "`stats`: displays misc. stats reported by JDA";
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}

	@Override
	public CommandData getCommandData() {
		CommandData rtn = new CommandData(getName(),getShortDescription());

		rtn.addOption(OptionType.STRING,"stats","say \"stats\" if you want to see my stats");

		return rtn;
	}
}

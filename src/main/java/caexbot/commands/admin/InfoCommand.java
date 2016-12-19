package caexbot.commands.admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.DraconicCommand;
import caexbot.config.CaexConfiguration;
import caexbot.util.StringUtil;

public class InfoCommand extends CaexCommand implements DraconicCommand{

	private static final String REQUIRED_PERMISSIONS = "70372416";

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();

		if (args.length > 1) { // more than 1 argument

			channel.sendMessage("To many arguments!").queue();
		}
		if (args.length == 0) { // !info
			User bot = event.getJDA().getSelfUser();
			eb.setAuthor("Caex Hewa", "https://github.com/Sgmaniac1255/caexbot", bot.getAvatarUrl())
			  .setColor(event.getGuild().getMember(bot).getColor())
			  .setTitle("A Discord Chatbot")
			  .setDescription("Written by Ranzer")
			  .setThumbnail(bot.getAvatarUrl())
			  .addField("Version", CaexConfiguration.getInstance().getVersion(), true)
			  .addField("Language", "Java", true)
			  .addField("Artwork", "Mellie", false)
			  .addField("Invite me!", inviteLinkBuilder(bot), true)
			  .addField("GitHub Repo", "[GitHub](https://github.com/sgmaniac1255/caexbot)", true)
			  .setFooter("Please report bugs/features ideas on the github/issues page", null);

			channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
		}
		if (args.length == 1) { // 1 argument
			if (args[0].equals("stats")) { // !info stats
				User bot = event.getJDA().getSelfUser();
				eb.setAuthor("Caex Hewa", "https://github.com/Sgmaniac1255/caexbot", bot.getAvatarUrl())
				  .setColor(event.getGuild().getMember(bot).getColor())
				  .setTitle("A Discord Chatbot")
				  .setDescription("Written by Ranzer")
				  .setThumbnail(bot.getAvatarUrl());
				eb.addField("Guilds", String.valueOf(event.getJDA().getGuilds().size()), false)
				  .addField("Users", countNonBotUsers(event), true)
				  .addField("Bots", countBotUsers(event), true)
				  .addField("Game", event.getJDA().getPresence().getGame().getName(), true);

				channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
			}
		}

	}

	private String countBotUsers(MessageReceivedEvent event) {
		int count = 0;
		
		for(User u:event.getJDA().getUsers()){
			if (u.isBot()){
				count++;
			}
		}
		
		return String.valueOf(count);
	}

	private String countNonBotUsers(MessageReceivedEvent event) {
		int count = 0;
		
		for(User u:event.getJDA().getUsers()){
			if (!u.isBot()){
				count++;
			}
		}
		
		return String.valueOf(count);
	}

	private String inviteLinkBuilder(User bot) {
		StringBuilder sb = new StringBuilder();

		sb.append("[No Permissions]")
		  .append("(https://discordapp.com/oauth2/authorize?client_id=").append(bot.getId()).append("&scope=bot)\n")
		  .append("[Limited Permissions]")
		  .append("(https://discordapp.com/oauth2/authorize?client_id=").append(bot.getId()).append("&scope=bot&permissions=").append(REQUIRED_PERMISSIONS).append(")\n")
		  .append("[Admin Permissions]")
		  .append("(https://discordapp.com/oauth2/authorize?client_id=").append(bot.getId()).append("&scope=bot&permissions=8)");

		return sb.toString();
	}

	@Override
	public String getUsage(Guild g) {
		StringBuilder sb = new StringBuilder();

		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ",g)).append("]** ")
		.append("<sub_command>\n");

		sb.append("        __Sub Commands__\n")
		  .append("**    default:** all about Caex.\n")
		  .append("**    [stats]** basic live stats of Caex.\n");

		return sb.toString();

	}

	@Override
	public List<String> getAlias() {

		return Arrays.asList("info", "i");
	}

	@Override
	public String getDescription() {

		return "Information about Caex and Author";
	}

	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("vucot");
	}

}

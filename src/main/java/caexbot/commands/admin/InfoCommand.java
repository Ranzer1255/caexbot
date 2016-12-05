package caexbot.commands.admin;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.DraconicCommand;
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
        		.addField("Version", "1.2.0", true)
        		.addField("Language", "Java", true)
        		.addField("Artwork", "Mellie", false)
        		.addField("Invite me!", inviteLinkBuilder(bot), true)
        		.addField("GitHub Repo", "[GitHub](https://github.com/sgmaniac1255/caexbot)", true);
        	
            channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
        }
        if (args.length == 1) { // 1 argument
            if (args[0].equals("author")) { // !info author
            	channel.sendMessage("- **Name:** Ranzer\n" +
            						"- **Age:** 27\n"+
            						"- **Artwork:** Mellie\n").queue();
            }
            if (args[0].equals("time")) { // !info time
                SimpleDateFormat format = new SimpleDateFormat("hh:mm a z(Z)");
                Date currentDate = new Date(System.currentTimeMillis());
                channel.sendMessage("It's " + format.format(currentDate)).queue();
            }
        }
		
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
		  .append("**    [author]** author and contributers to CaexBot.\n")
		  .append("**    [time]** tell the current time.");
		
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

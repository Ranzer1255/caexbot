package caexbot.commands.admin;

import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.util.StringUtil;

public class InfoCommand extends CaexCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		if (args.length > 1) { // more than 1 argument
            channel.sendMessage("To many arguments!");
        }
        if (args.length == 0) { // !info
            channel.sendMessage("- **Author:** Ranzer\n" +
            					"- **Language:** Java\n" +
            					"- **Github Repo:** https://github.com/Sgmaniac1255/caexbot");
        }
        if (args.length == 1) { // 1 argument
            if (args[0].equals("author")) { // !info author
            	channel.sendMessage("- **Name:** Ranzer\n" +
            						"- **Age:** 27\n"+
            						"- **Artwork:** Mellie\n");
            }
            if (args[0].equals("time")) { // !info time
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date currentDate = new Date(System.currentTimeMillis());
                channel.sendMessage("It's " + format.format(currentDate));
            }
        }
		
	}

	@Override
	public String getUsage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ")).append("]** ")
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

}

package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.commands.CaexCommand;
import caexbot.util.StringUtil;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class FacepalmCommand extends CaexCommand {

	private String[] facepalms ={
			"*%s is ashamed for you*",
			"*%s shoves their palm through their brain*",
			"*%s slaps their face with a thunderous clap*",
			"*%s tried to high-five the bakc of their face*",
			"*%s groans as a flat palm meets their forhead*",
			"*%s throws their head on their desk with a loud thud*",
			"*%s thinks you deserve a high five... to the face... with a brick*",
			"*%s sighs and slowly places their palm on their face and shakes their head*",
			"*%s is having an aneurysm trying to comprehend the enormity of your stupidity*"
	};
	
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		channel.sendMessage(String.format(facepalms[ThreadLocalRandom.current().nextInt(facepalms.length)], author.getAsMention()));

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("facepalm","fp");
	}

	@Override
	public String getDescription() {
		return "Facepalm....";
	}

	@Override
	public String getUsage() {
		return String.format("**[%s]** %s", StringUtil.cmdArrayToString(getAlias(), ", "), getDescription());
	}

}
   
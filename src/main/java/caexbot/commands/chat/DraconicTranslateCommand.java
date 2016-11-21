package caexbot.commands.chat;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.util.DraconicTranslator;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DraconicTranslateCommand extends CaexCommand {

	private static final String DICT = "/caex/draconic/dict.txt";
	private DraconicTranslator trans;
	
	public DraconicTranslateCommand() {
		
		File dict;
		dict = new File(System.getProperty("user.home"),DICT);

		
		
		trans = new DraconicTranslator(dict);
	}
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		channel.sendMessage(String.format("**[%s]** here ya go\n*%s*", author.getAsMention(), trans.translate(args, true))).queue();

	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("draconic","drc");
	}

	@Override
	public String getDescription() {
		return "I speak Draconic! What do you want to know how to say?";
	}

	@Override
	public String getUsage(Guild g) {
		
		return String.format("**[%s]** <Common-Tongne(english) phrase>",
				StringUtil.cmdArrayToString(getAlias(), ", ",g));
		
	}

}
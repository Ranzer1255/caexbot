package caexbot.commands.chat;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.util.DraconicTranslator;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DraconicTranslateCommand extends CaexCommand {

	private static final String DICT = "/caexbot/draconic/dict.txt";
	private DraconicTranslator trans;
	
	public DraconicTranslateCommand() {
		
		File dict;
		dict = new File(System.getProperty("user.home"),DICT);

		
		
		trans = new DraconicTranslator(dict);
	}
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		eb.setAuthor("Draconic Translation", "http://draconic.twilightrealm.com/", null);
		eb.setFooter("Powered by Draconic Translator from Twilight Realm", null);
		eb.setColor(new Color(0xa0760a));
		eb.addField("Common:", StringUtil.arrayToString(Arrays.asList(args), " "), false);
		eb.addField("Draconic", trans.translate(args, true), false);
		
		channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
//		channel.sendMessage(String.format("**[%s]** here ya go\n*%s*", author.getAsMention(), trans.translate(args, true))).queue();

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
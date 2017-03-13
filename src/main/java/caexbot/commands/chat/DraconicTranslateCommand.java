package caexbot.commands.chat;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.util.DraconicTranslator;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DraconicTranslateCommand extends CaexCommand implements Describable{

	private static final String DICT = "/caexbot/draconic/dict.txt";
	private DraconicTranslator trans;
	
	public DraconicTranslateCommand() 
	{	
		File dict;
		dict = new File(System.getProperty("user.home"),DICT);

		trans = new DraconicTranslator(dict);
	}
	
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		
		if (args[0].equals("com")){
			fromDraconic(Arrays.copyOfRange(args, 1,args.length), channel);
			return;
		}
		
		toDraconic(args, channel);
	}
	
	private void fromDraconic(String[] args, TextChannel channel) {
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		eb.setAuthor("Draconic Translation", "http://draconic.twilightrealm.com/", null);
		eb.setFooter("Powered by Draconic Translator from Twilight Realm", null);
		eb.setColor(new Color(0xa0760a));
		eb.addField("Draconic:", StringUtil.arrayToString(Arrays.asList(args), " "), false);
		eb.addField("Common:", trans.translate(args, false), false);
		
		channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
	}

	private void toDraconic(String[] args, TextChannel channel) {
		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		
		eb.setAuthor("Draconic Translation", "http://draconic.twilightrealm.com/", null);
		eb.setFooter("Powered by Draconic Translator from Twilight Realm", null);
		eb.setColor(new Color(0xa0760a));
		eb.addField("Common:", StringUtil.arrayToString(Arrays.asList(args), " "), false);
		eb.addField("Draconic", trans.translate(args, true), false);
		
		channel.sendMessage(mb.setEmbed(eb.build()).build()).queue();
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("draconic","drc");
	}

	@Override
	public String getShortDescription() {
		return "I speak Draconic! What do you want to know how to say?";
	}
	
	@Override
	public String getLongDescription() {
		return "Translates a word or phrase from Common (english) to Draconic.\n\n"
				+ "`com`: will translate a word or phrase in Draconic back into Common (english)\n\n"
				+ "This Translator is powered by [Twilight Realm](http://draconic.twilightrealm.com/)";
	}

	@Override
	public String getUsage(Guild g) {
		
		return "`"+getPrefix(g)+getName()+" [com] <translation phrase>`";
		
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}
}
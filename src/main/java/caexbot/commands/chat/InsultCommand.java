package caexbot.commands.chat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.commands.DraconicCommand;
import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * the host website i was pulling insults from has gone down... this is disabled indefenantly.
 * @author jrdillingham
 *
 */
@Deprecated
public class InsultCommand extends CaexCommand implements Describable, DraconicCommand {

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		
		for ( User u : event.getMessage().getMentionedUsers()) {
			System.out.println(u.getId());
			if(u.getId().equals(CaexConfiguration.getInstance().getOwner())){
				channel.sendMessage("You want me to insult him?!?!.... \n I'm sorry but I can't insult *him*.... he'll *__KILL__* me!!").queue();
				continue;
			}
			sb.append(u.getAsMention()+", ");
		}
		if(sb.length()==0) return;//don't throw an insult if no one was tagged.
		try {
			Document doc = Jsoup.parse(new URL("http://www.insultgenerator.org"), 3000);
			String insult = doc.getElementsByClass("wrap").get(0).html().replace("<br>", "").replace("\n", "");
			sb.append(insult);
			
			channel.sendMessage(sb.toString()).queue();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("insult","offend");
	}

	@Override
	public List<String> getDraconicAlias() {
		return Arrays.asList("chikohk");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

	@Override
	public String getShortDescription() {
		return "Give them a slap in the face!";
	}

	@Override
	public String getUsage(Guild g) {
		return getPrefix(g)+getName()+" [@user/users you want to insult]";
	}

	@Override
	public String getLongDescription() {
		// TODO make getLongDescription
		return getShortDescription();
	}

}

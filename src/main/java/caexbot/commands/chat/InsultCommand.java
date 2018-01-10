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
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * @author Ranzer
 */
public class InsultCommand extends CaexCommand implements Describable, DraconicCommand {

	public static User lastOwnerInsult = null;
	
	@Override
	public void process(String[] args,MessageReceivedEvent event) {
		StringBuilder sb = new StringBuilder();
		
		for ( Member m : event.getMessage().getMentionedMembers()) {
			if(m.getUser().getId().equals(CaexConfiguration.getInstance().getOwner())){
				if(m.getOnlineStatus()==OnlineStatus.ONLINE){
					event.getChannel().sendMessage("You want me to insult him?!?!.... \n I'm sorry but I can't insult *him*.... he'll *__KILL__* me!!").queue();
				} else {
					event.getChannel().sendMessage("*looks around nervously* ... he's not here right now....\n"
							+ "alright, I'll do it..... **but** I wont tag him! *gulp*\n"
							+ "if he says \"sleep\", its on your head!").queue();
					lastOwnerInsult=event.getAuthor();
					sb.append(m.getEffectiveName()+", ");
				}
				continue;
			}
			
			if(event.getMember().equals(m)){
				event.getChannel().sendMessage("*looks confused* yourself? but.... ok...").queue();
			}
			sb.append(m.getAsMention()+", ");
		}
		if(sb.length()==0) return;//don't throw an insult if no one was tagged.
		try {
			Document doc = Jsoup.parse(new URL("http://www.pangloss.com/seidel/Shaker/index.html?"), 3000);
			String insult = doc.getElementsByTag("p").get(0).text();;
			sb.append(insult);
			
			event.getChannel().sendMessage(sb.toString()).queue();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("insult","offend","curse*");
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
		return "`"+getPrefix(g)+getName()+" <Taged users you want to insult>`";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n"
				+ "Tag the people you want to insult. can insult more than one at a time.\n\n"
				+ "Powered by [pangloss.com](http://www.pangloss.com/seidel/Shaker/index.html?)";
	}

	@Override
	public boolean isAplicableToPM() {
		return false;
	}

}

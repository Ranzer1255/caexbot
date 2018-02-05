package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class XpGiftCommand extends CaexCommand implements Describable {
	
	final String IDREGEX = "(?<id>\\d{18})";

	@Override
	public void process(String[] args, MessageReceivedEvent event) {		
	
		List<User> mentions = event.getMessage().getMentionedUsers();
		if(mentions==null||mentions.size()==0){
			
		} else {
			
		}
		
	}

	@Override
	public String getShortDescription() {
		return "Give your Friends some of your XP";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n"
				+ "";//TODO add instructions for gifting
	}

	@Override
	public boolean isAplicableToPM() {
		return false;
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("gift","give");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

}

package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class XpGiftCommand extends CaexCommand implements Describable {
	
	private final String ID_REGEX = "(?<id>\\d{18})";
	private final String XP_REGEX = "((?<xp>\\d+)(\\s?[xX][pP])?)";
	

	@Override
	public void process(String[] args, MessageReceivedEvent event) {		
	
		if(args.length!=2) return;//TODO usage
		
		Matcher xp_match = Pattern.compile(XP_REGEX).matcher(args[0]);
		if(!xp_match.matches()) return; //TODO usage
		
		
		
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
		return Arrays.asList("donate","gift","give");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

}

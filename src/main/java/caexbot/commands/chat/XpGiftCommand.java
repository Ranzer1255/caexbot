package caexbot.commands.chat;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.data.GuildData;
import caexbot.data.GuildManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class XpGiftCommand extends CaexCommand implements Describable {
	
	private final Pattern ID_REGEX = Pattern.compile("(?<id>\\d{18})");
	private final Pattern XP_REGEX = Pattern.compile("((?<xp>\\d+)(\\s?[xX][pP])?)");
	

	@Override
	public void process(String[] args, MessageReceivedEvent event) {		
	
		if(args.length!=2) return;//TODO usage
		
		int donation=-1;
		
		donation = getDonation(event.getMessage().getContentRaw());
		if(donation <=0) return; //TODO no donation
				
		Member donatee = getDonatee(args[1],event);
		if (donatee==null) return; //TODO no donatee
		
		Member donator = event.getMember();
		
		donate(donatee, donator, donation,event.getChannel());	
	}
	
	/**
	 * 
	 * @return donation as a positive integer or -1 if an error is detected
	 */
	private int getDonation(String string) {
		int donation = -1;
		
		Matcher m = XP_REGEX.matcher(string);
		if(m.matches()){
			try {
				donation = Math.abs(Integer.parseInt(m.group("xp")));
			} catch (NumberFormatException e) {
				// TODO bad donation message
				return -1;
			}
		}
		
		return donation;
	}



	private void donate(Member donatee, Member donator, int donation, MessageChannel channel) {
		GuildData gd = GuildManager.getGuildData(donatee.getGuild());
		gd.removeXP(donator.getUser(), donation, channel);
		gd.addXP(donatee.getUser(), donation, channel);		
	}

	private Member getDonatee(String string, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return null;
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

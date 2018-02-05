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
import net.dv8tion.jda.core.entities.TextChannel;
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
		
		int donation = Math.abs(Integer.parseInt(xp_match.group("xp")));
		
		if(!donationAmountCheck(donation, event.getMember())) return;//TODO amount error
		
		Member donatee = getDonatee(args[1],event);
		if (donatee==null) return; //TODO no donatee
		
		Member donator = event.getMember();
		
		donate(donatee, donator, donation,event.getChannel());
		
		
	}

	private void donate(Member donatee, Member donator, int donation, MessageChannel channel) {//TODO this is where i was working
		GuildManager.getGuildData(donatee.getGuild()).addXP(donatee.getUser(), donation, (TextChannel)channel);
		
	}

	private Member getDonatee(String string, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean donationAmountCheck(int donation, Member member) {
		// TODO Auto-generated method stub
		return false;
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

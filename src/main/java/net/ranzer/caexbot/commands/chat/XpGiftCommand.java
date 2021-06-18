package net.ranzer.caexbot.commands.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.data.IGuildData;
import net.ranzer.caexbot.data.IMemberData;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XpGiftCommand extends CaexCommand implements Describable {
	
	private final Pattern ID_REGEX = Pattern.compile("(?<id>\\d{18})");
	private final Pattern XP_REGEX = Pattern.compile("((?<xp>\\d+)(\\s?[xX][pP])?)");
	

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		
		int donation=-1;
		
		donation = getDonation(event.getMessage().getContentRaw());
		if(donation <=0) {
			System.out.println("no donation");
			return;
		}
				
		Member donatee = getDonatee(event.getMessage().getContentRaw(),event.getGuild());
		if (donatee==null) {
			System.out.println("no donatee");
			return;
		}
		
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
		
		if(m.find()){
			donation = Math.abs(Integer.parseInt(m.group("xp")));
		}
		
		return donation;
	}



	private void donate(Member recipient, Member donor, int donation, MessageChannel channel) {
		IGuildData gd = GuildManager.getGuildData(recipient.getGuild());
		IMemberData donorData = gd.getMemberData(donor);
		IMemberData recipientData = gd.getMemberData(recipient);

		//affordance check
		if(donorData.getXP()<donation){
			channel.sendMessage(String.format(
					"you can't afford to donate %,dxp, you only have %,dxp to your name.",
					donation,donorData.getXP())).queue();
			return;
		}
		//bot check
		if(recipient.getUser().isBot()){
			channel.sendMessage("While I appreciate the gesture, my fellow bots and I don't need XP. It's for you humans only.").queue();
			return;
		}
		
		channel.sendMessage(donationEmbed(donor,donation,recipient)).queue();
		
		donorData.removeXP(donation, channel);
		recipientData.addXP(donation, channel);
	}

	private MessageEmbed donationEmbed(Member donator, int donation, Member donatee) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("Donation");
		eb.setColor(getCatagory().COLOR);
		eb.setDescription(
				String.format("**From:** %s\n"
						+ "**To:** %s\n"
						+ "%,dxp", 
						donator.getEffectiveName(),
						donatee.getEffectiveName(),
						donation)
				);
		eb.setThumbnail(donatee.getUser().getAvatarUrl());
		
		return eb.build();
	}

	private Member getDonatee(String string, Guild guild) {
		
		Matcher id = ID_REGEX.matcher(string);
		if (id.find()){
			return guild.getMemberById(id.group("id"));
		}
		
		
		return null;
	}

	@Override
	public String getShortDescription() {
		return "Give your Friends some of your XP";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n"
				+ "<donation> the amount you would like to give\n"
				+ "<recipient> ether mention them or put in their user ID number (the 18 digit number)";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("%s%s <donation> <recipient>", getPrefix(g),getName());
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

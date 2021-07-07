package net.ranzer.caexbot.commands.chat;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.data.IGuildData;
import net.ranzer.caexbot.data.IMemberData;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XpGiftCommand extends BotCommand implements Describable {

	private static final Pattern ID_REGEX = Pattern.compile("(?<id>\\d{18})");
	private static final Pattern XP_REGEX = Pattern.compile("((?<xp>\\d+)(\\s?[xX][pP])?)");
	private static final String SCO_DONATION = "donation";
	private static final String SCO_RECIPIENT = "recipient";
	private static final String CANT_AFFORD = "you can't afford to donate %,dxp, you only have %,dxp to your name.";
	private static final String BOT_DONATION = "While I appreciate the gesture, my fellow bots and I don't need XP. It's for you humans only.";

	@Override
	public void processSlash(SlashCommandEvent event) {
		try {
			Member donor = Objects.requireNonNull(event.getMember());
			Member recipient = Objects.requireNonNull(Objects.requireNonNull(event.getOption(SCO_RECIPIENT)).getAsMember());
			int donation = (int) Objects.requireNonNull(event.getOption(SCO_DONATION)).getAsLong();
			donate(recipient,
					donor,
					donation,
					event.getChannel());
			event.replyEmbeds(donationEmbed(donor,donation,recipient)).queue();
		} catch (CantAffordException e) {
			event.reply(String.format(
					CANT_AFFORD,
					e.donation,e.balance)).setEphemeral(true).queue();
		} catch (BotDonationException e) {
			event.reply(BOT_DONATION).setEphemeral(true).queue();
		}
	}

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		
		int donation;
		
		donation = getDonation(event.getMessage().getContentRaw());
		if(donation <=0) {
			System.out.println("no donation");
			return;
		}
				
		Member recipient = getRecipient(event.getMessage().getContentRaw(),event.getGuild());
		if (recipient==null) {
			System.out.println("no recipient");
			return;
		}
		
		Member donor = Objects.requireNonNull(event.getMember());

		try {
			donate(recipient, donor, donation,event.getChannel());
			event.getChannel().sendMessageEmbeds(donationEmbed(donor,donation,recipient)).queue();
		} catch (CantAffordException e) {
			event.getChannel().sendMessage(String.format(
					CANT_AFFORD,
					e.donation,e.balance)).queue();
		} catch (BotDonationException e){
			event.getChannel().sendMessage(BOT_DONATION).queue();
		}
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



	private void donate(Member recipient, Member donor, int donation, MessageChannel channel) throws CantAffordException, BotDonationException {
		//bot check
		if(recipient.getUser().isBot()){
			throw new BotDonationException();
		}

		IGuildData gd = GuildManager.getGuildData(recipient.getGuild());
		IMemberData donorData = gd.getMemberData(donor);
		IMemberData recipientData = gd.getMemberData(recipient);

		//affordance check
		if(donorData.getXP()<donation){
			throw new CantAffordException(donation,donorData.getXP());
		}
		donorData.removeXP(donation, channel);
		recipientData.addXP(donation, channel);
	}

	private MessageEmbed donationEmbed(Member donor, int donation, Member recipient) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setTitle("Donation");
		eb.setColor(getCategory().COLOR);
		eb.setDescription(
				String.format("**From:** %s\n"
						+ "**To:** %s\n"
						+ "%,dxp", 
						donor.getEffectiveName(),
						recipient.getEffectiveName(),
						donation)
				);
		eb.setThumbnail(recipient.getUser().getAvatarUrl());
		
		return eb.build();
	}

	private Member getRecipient(String string, Guild guild) {
		
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
	public boolean isApplicableToPM() {
		return false;
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("gift","donate","give");
	}

	//Category
	@Override
	public Category getCategory() {
		return Category.CHAT;
	}

	@Override
	public CommandData getCommandData() {
		CommandData rtn = new CommandData(getName(),getShortDescription());

		rtn.addOption(OptionType.INTEGER,SCO_DONATION,"how much do you want to give?",true)
		   .addOption(OptionType.USER, SCO_RECIPIENT,"who are you giving it to?",true);

		return rtn;
	}

	private static class CantAffordException extends Exception{
		public final int donation;
		public final int balance;

		public CantAffordException(int donation,int balance){
			this.donation = donation;
			this.balance = balance;
		}
	}
	private static class BotDonationException extends Exception{}
}

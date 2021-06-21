package net.ranzer.caexbot.commands.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.data.GuildManager;
import net.ranzer.caexbot.data.IGuildData;
import net.ranzer.caexbot.util.Logging;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HiLowCommand extends CaexCommand implements Describable {

	private static final double MAX_BET_PERCENTAGE = .25;//set this on a server by server level?
	private static final int MIN_XP = 1000;//and this?
	private static final int MIN_RAN = 1;
	private static final int MAX_RAN = 100;
	private static final int CUT_OFF = 50;

	@Override
	public void process(String[] args,  MessageReceivedEvent event) {

		//parse args
		if (args.length==0 || args.length>2){
			invalidUsage(event, "Wrong number of arguments!");
			return;
		}
		
		//parse bet
		IGuildData gd = 	GuildManager.getGuildData(event.getGuild());
		if(gd.getMemberData(event.getMember()).getXP()<MIN_XP){
			event.getChannel().sendMessage("I'm sorry, but you have not yet earned enough points to gamble you XP away.\n"
					+ "Come back after you have earned " + MIN_XP+ " points").queue();
			return;
		}
		int bet;
		try {
			bet = Math.abs(Integer.parseInt(args[0]));
		} catch (NumberFormatException e) {
			invalidUsage(event, "I didn't get an integer for your bet value!"); 
			return;
		}
		
		if (bet > (gd.getMemberData(event.getMember()).getXP()*MAX_BET_PERCENTAGE)){
			event.getChannel().sendMessage("I'm sorry I can't allow you to bet more than "+ (MAX_BET_PERCENTAGE*100) +"% of your XP.").queue();
			return;
		}
		
		//parse choice
		Choice c;
		if (args[1].toLowerCase().charAt(0)=='h')
			c=Choice.HIGH;
		else if (args[1].toLowerCase().charAt(0)=='l')
			c=Choice.LOW;
		else { 
			invalidUsage(event, "I'm sorry I Didn't understand your choice. please pick high or low");
			return;
		}
		
		
		//main logic
		
		runGame(gd, bet, c, event);
	}

	private void runGame(IGuildData gd, int bet, Choice c, MessageReceivedEvent event) {
		HiLowGame game = new HiLowGame(event, bet, c);
		
		try {
			if (game.win()){
				gd.getMemberData(event.getMember()).addXP(bet, event.getTextChannel());
				win(game);
			} else {
				gd.getMemberData(event.getMember()).removeXP(bet, event.getTextChannel());
				lose(game);
			}
		} catch (Exception e) {
			Logging.error("WTF!! just happened");
			Logging.log(e);
		}
	}

	private void invalidUsage(MessageReceivedEvent event, String string) {
		super.invalidUsage(event.getGuild());
		event.getChannel().sendMessage(
						string+"\n"
						+ "Please follow this usage chart"
						+ getUsage(event.getGuild())
			).queue();
		
	}

	private void win(HiLowGame game) {
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setAuthor("Winner!", null, null);
		eb.setDescription(String.format(
				  "Your bet was: `%dxp`\n"
				+ "Your choice was: `%s`\n"
				+ "The Random number is: `%d`\n", 
				game.bet, game.choice, game.randomNumber));
		eb.addField("Winner", "You earn "+(game.getBet()*2)+"xp!", false);
		eb.setColor(Color.yellow);
		
		game.getChannel().sendMessage(new MessageBuilder().setEmbeds(eb.build()).append(game.getPlayer().getAsMention()).build()).queue();
		
	}
	private void lose(HiLowGame game) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Bad Luck!", null, null);
		eb.setDescription(String.format(
				  "Your bet was: `%dxp`\n"
				+ "Your choice was: `%s`\n"
				+ "The Random number is: `%d`\n", 
				game.bet, game.choice, game.randomNumber));
		eb.addField("Loser", "You Lose "+game.getBet()+"xp!", false);
		eb.setColor(Color.RED);
		
		game.getChannel().sendMessage(new MessageBuilder().setEmbeds(eb.build()).append(game.getPlayer().getAsMention()).build()).queue();
	}


	@Override
	public List<String> getAlias() {
		return Arrays.asList("hilow", "hl");
	}

	@Override
	public Category getCategory() {
		return Category.GAME;
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" xp {high | low}`";
	}

	@Override
	public String getShortDescription() {
		return "Gamble all your hard earned XP away!";
	}

	@Override
	public String getLongDescription() {
		return "Play a Game of Hi-Low\n\n"
				+ ""
				+ "Choose how much you would like to bet,\n"
				+ "then decide if the number will fall Hi or Low of `"+CUT_OFF+"` on a scale of `"+MIN_RAN+"-"+MAX_RAN+"`\n"
				+ "(Side note, a hit on `"+CUT_OFF+"` will be counted as `low`)\n\n"
				+ "If you win, Congratulations! Your bet will be doubled and added back to your total\n"
				+ "If you lose, <sad trombone>, Your bet is gone and your hard earned xp is washed away\n\n"
				+ "You must have a minimum of `"+MIN_XP+"xp` to play, and you may only bet up to `"+(MAX_BET_PERCENTAGE*100)+"%` of your current xp.\n"
				+ "GOOD LUCK!";
	}

	@Override
	public boolean isApplicableToPM() {
		return false;
	}

	public static class HiLowGame {
		private final User player;
		private final TextChannel channel;
		private final int bet;
		private final int randomNumber;
		private final Choice choice;

		public HiLowGame(MessageReceivedEvent event, int bet, Choice c){
			this.player=event.getAuthor();
			this.channel=event.getTextChannel();
			this.bet = bet;
			choice = c;
			randomNumber = ThreadLocalRandom.current().nextInt(MIN_RAN, MAX_RAN+1);
		}
	
		public User getPlayer() {
			return player;
		}
	
	
	
		public TextChannel getChannel() {
			return channel;
		}
	
	
	
		public int getBet() {
			return bet;
		}
	
		public  boolean win() throws Exception{
			switch (choice) {
			case HIGH:
				return randomNumber>CUT_OFF;
				
			case LOW:
				return randomNumber<=CUT_OFF;
				
			default:
				throw new Exception("bad choice");// this should never happen. but just in case
			}
		}
		
	}

	public enum Choice {
		HIGH, LOW
	}

}

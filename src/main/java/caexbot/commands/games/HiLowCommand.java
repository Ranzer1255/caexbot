package caexbot.commands.games;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.data.GuildData;
import caexbot.data.GuildManager;
import caexbot.util.Logging;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HiLowCommand extends CaexCommand implements Describable {

	private static final double MAX_BET_PERCENTAGE = .1;//set this on a server by server level?
	private static final int MIN_XP = 1000;//and this?
	private static final int MIN_RAN = 1;
	private static final int MAX_RAN = 100;
	private static final int CUT_OFF = 50;

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		//parse args
		if (args.length==0 || args.length>2){
			invalidUsage(event, "Wrong number of arguments!");
			return;
		}
		
		//parse bet
		GuildData gd = 	GuildManager.getGuildData(event.getGuild());
		if(gd.getXP(author)<MIN_XP){
			channel.sendMessage("I'm sorry, but you have not yet earned enough points to gamble you XP away.\n"
					+ "Come back after you have earned " + MIN_XP+ " points").queue();
			return;
		}
		int bet;
		try {
			bet = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			invalidUsage(event, "I didn't get an integer for your bet value!"); 
			return;
		}
		
		if (bet > (gd.getXP(author)*MAX_BET_PERCENTAGE)){
			channel.sendMessage("I'm sorry I can't allow you to bet more than "+ (MAX_BET_PERCENTAGE*100) +"% of your XP.").queue();
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
		gd.removeXP(author, bet, channel);
		
		HiLowGame game = new HiLowGame(author, channel, bet, c);
		
		try {
			if (game.win()){
				win(game, gd);
			} else {
				lose(game, gd);
			}
		} catch (Exception e) {
			Logging.error("WTF!! just happend");
			Logging.log(e);
			return;
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

	private void win(HiLowGame game, GuildData gd) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Winner!", null, null);
		eb.setDescription(String.format(
				  "Your bet was: `%dxp`\n"
				+ "Your choice was: `%s`\n"
				+ "The Random number is: `%d`\n", 
				game.bet, game.choice, game.randomNumber));
		eb.addField("Winner", "You earn "+(game.getBet()*2)+"xp!", false);
		eb.setColor(Color.yellow);
		
		game.getChannel().sendMessage(new MessageBuilder().setEmbed(eb.build()).build()).queue();
		
	}
	private void lose(HiLowGame game, GuildData gd) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("Bad Luck!", null, null);
		eb.setDescription(String.format(
				  "Your bet was: `%dxp`\n"
				+ "Your choice was: `%s`\n"
				+ "The Random number is: `%d`\n", 
				game.bet, game.choice, game.randomNumber));
		eb.addField("Loser", "You Lose "+game.getBet()+"xp!", false);
		eb.setColor(Color.RED);
		
		game.getChannel().sendMessage(new MessageBuilder().setEmbed(eb.build()).build()).queue();
	}


	@Override
	public List<String> getAlias() {
		return Arrays.asList("hilow", "hl");
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.GAME;
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
				+ "(Side note a hit on `"+CUT_OFF+"` will be counted as `low`)\n\n"
				+ "If you win, Congradulations! Your bet will be doubled and added back to your total\n"
				+ "If you lose, <sad trombone>, Your bet is gone and your hard earned xp is washed away\n\n"
				+ "You must have a minimum of `"+MIN_XP+"xp` to play, and you may only bet up to `"+(MAX_BET_PERCENTAGE*100)+"%` of your current xp.\n"
				+ "GOOD LUCK!";
	}

	public class HiLowGame {
		private User player;
		private TextChannel channel;
		private int bet;
		private int randomNumber;
		private Choice choice;
		public HiLowGame(User player, TextChannel channel, int bet, Choice c){
			this.player=player;
			this.channel=channel;
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
	
		public int getRandomNumber() {
			return randomNumber;
		}
	
		public Choice getChoice() {
			return choice;
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

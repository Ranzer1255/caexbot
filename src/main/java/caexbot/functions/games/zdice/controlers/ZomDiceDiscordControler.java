package caexbot.functions.games.zdice.controlers;

import java.util.Deque;

import caexbot.functions.games.zdice.Player;
import caexbot.functions.games.zdice.RollResult;
import caexbot.functions.games.zdice.ZomDie;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class ZomDiceDiscordControler extends ZomDiceController{

	private static ZomDiceDiscordControler instanace;
	
	private TextChannel gameChannel;
	
	//constructor for singleton
	private ZomDiceDiscordControler(){}
	
	public static ZomDiceDiscordControler getInstance(){
		if( instanace == null){
			instanace = new ZomDiceDiscordControler();
		}
		return instanace;
	}
	
	public void startGameInChannel(TextChannel c){
		gameChannel = c;
		startGame();
	}
	public boolean addPlayer(User u){
		return addPlayer(new UserPlayerAdapter(u));
	}
	public boolean removePlayer(User u){
		return game.removePlayer(new UserPlayerAdapter(u));
		
	}

	@Override
	public void announceGameStart(Deque<Player> playerList) {
		StringBuilder sb = new StringBuilder();
		for (Player player : playerList) {
			sb.append(((UserPlayerAdapter) player).getUser().getAsMention());
			sb.append(", ");
		}
		sb.append("The game has started.");
		gameChannel.sendMessage(sb.toString()).queue();
	}

	@Override
	public void promptPlayer(Player nextPlayer) {
		gameChannel.sendMessage(
				((UserPlayerAdapter) nextPlayer).getUser().getAsMention()+" it is now your turn").queue();
		
	}

	public void roll(User p) {
		roll(new UserPlayerAdapter(p));
	}
	
	public void endTurn(User u){
		endTurn(new UserPlayerAdapter(u));
	}

	@Override
	public void notActivePlayer() {
		gameChannel.sendMessage("It's not yet your turn. Please wait").queue();
	}

	@Override
	public void needMorePlayers() {
		gameChannel.sendMessage("There are not enough players yet to start the game.").queue();
		
	}

	@Override
	public void rollResult(RollResult result) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("You rolled:\n");
		for (ZomDie d : result.getDiceRolled()) {
			sb.append(d.getColor().name + ", ");
		}
		sb.delete(sb.length()-2, sb.length()).append("\nfor a result of:\n");
		
		for (ZomDie.Side s: result.getSideResults()){
			sb.append(s.name +", ").append("\n");
		}
		sb.delete(sb.length()-3, sb.length()-1);
		
		sb.append("your current brains earned this turn are: " + result.getBrains()).append("\n");
		sb.append("Your current Shots earned this turn are: " + result.getShots()).append("\n");
		sb.append("Roll again? (roll)\nEnd turn (end)");
		
		gameChannel.sendMessage(sb.toString()).queue();
		
	}

	@Override
	public void announceEndTurn(Player currentPlayer) {
		gameChannel.sendMessage("Your turn is over.\n"+"you have **" +currentPlayer.getBrains()+"** brains.").queue();
	}

	@Override
	public void announceGameEnd(Player[] highscoreTable) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Game over!\n\n");
		sb.append("Winner: "+((UserPlayerAdapter) highscoreTable[0]).getUser().getAsMention()+"\n\n");
		sb.append("__Scores__\n");
		for (int i = 0; i < highscoreTable.length; i++) {
			sb.append(((UserPlayerAdapter) highscoreTable[i]).getUser().getAsMention()+": "+highscoreTable[i].getBrains()+"\n");
		}
		
		gameChannel.sendMessage(sb.toString()).queue();
		instanace = null;
	}

	public void setActiveChannel(TextChannel channel) {
		gameChannel = channel;
		
	}

	@Override
	public void announceFinalRound() {
		gameChannel.sendMessage("The final round has begun! Last chance to win!").queue();
		
	}
}

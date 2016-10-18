package caexbot.functions.games.zdice.controlers;

import java.util.Deque;

import caexbot.functions.games.zdice.Player;
import caexbot.functions.games.zdice.RollResult;
import caexbot.functions.games.zdice.ZomDie;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

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
		gameChannel.sendMessage(sb.toString());
	}

	@Override
	public void promptPlayer(Player nextPlayer) {
		gameChannel.sendMessage(
				((UserPlayerAdapter) nextPlayer).getUser().getAsMention()+" it is now your turn");
		
	}

	public void roll(User p) {
		roll(new UserPlayerAdapter(p));
	}

	@Override
	public void notActivePlayer() {
		
	}

	@Override
	public void needMorePlayers() {
		// TODO announce in game channel that there are not enough players
		
	}

	@Override
	public void rollResult(RollResult result) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("You rolled:\n");
		for (ZomDie d : result.getDiceRolled()) {
			sb.append(d.getColor().toString() + ", ");
		}
		sb.delete(sb.length()-2, sb.length()).append("\nfor a result of:\n");
		
		for (ZomDie.Side s: result.getSideResults()){
			sb.append(s.name() +", ").append("\n");
		}
		sb.delete(sb.length()-2, sb.length());
		
		sb.append("your current brains earnd this turn are: " + result.getBrains()).append("\n");
		sb.append("Your current Shots earnd this turn are: " + result.getShots()).append("\n");
		sb.append("Roll again? (roll)/nEnd turn (end)");
		
		gameChannel.sendMessage(sb.toString());
		
	}

	@Override
	public void endTurn(Player currentPlayer) {
		// TODO end turn
		
	}

	@Override
	public void announceGameEnd(Player[] highscoreTable) {
		// TODO announce game end
		
		instanace = null;
	}

	public void setActiveChannel(TextChannel channel) {
		gameChannel = channel;
		
	}

	public void endTurn(User user) {
		endTurn(new UserPlayerAdapter(user));
		
	}
}

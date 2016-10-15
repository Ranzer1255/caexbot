package caexbot.functions.games.zdice.controlers;

import java.util.Deque;

import caexbot.functions.games.zdice.Player;
import caexbot.functions.games.zdice.RollResult;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public class ZomDiceDiscordControler extends ZomDiceController {

	private final JDA api;//not sure i'll need this
	private TextChannel gameChannel;
	
	public ZomDiceDiscordControler(JDA view){
		api = view;
	}
	public void startGameInChannel(TextChannel c){
		if(startGame()){
			gameChannel=c;
		}
	}
	
	public void addPlayer(TextChannel c, User u){
		addPlayer(new UserPlayerAdapter(u));
		c.sendMessage(u.getAsMention()+" You have been added.");
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
		// TODO announce calling player isn't currently playing
		
	}

	@Override
	public void needMorePlayers() {
		// TODO announce in game channel that there are not enough players
		
	}

	@Override
	public void rollResult(RollResult result) {
		// TODO display results from roll
		
	}

	@Override
	public void endTurn(Player currentPlayer) {
		// TODO end turn
		
	}

	@Override
	public void announceGameEnd(Player[] highscoreTable) {
		// TODO announce game end
		
	}
}

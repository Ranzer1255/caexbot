package caexbot.functions.games.zdice.controlers;

import caexbot.functions.games.zdice.Player;
import net.dv8tion.jda.entities.User;

public class ZomDiceDiscordControler extends ZomDiceController {
	

	public void addPlayer(User u){
		addPlayer(new UserPlayerAdapter(u));
	}

	public static ZomDiceController getGame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void announceGameStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void promptPlayer(Player nextPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roll(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notActivePlayer() {
		// TODO Auto-generated method stub
		
	}

}

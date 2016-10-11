package caexbot.functions.games.zdice.controlers;

import net.dv8tion.jda.entities.User;

public class ZomDiceDiscordControler extends ZomDiceController {
	

	public void addPlayer(User u){
		addPlayer(new UserPlayerAdapter(u));
	}

	public static ZomDiceController getGame() {
		// TODO Auto-generated method stub
		return null;
	}

}

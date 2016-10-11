package caexbot.functions.games.zdice.controlers;

import caexbot.functions.games.zdice.Player;
import net.dv8tion.jda.entities.User;

public class UserPlayerAdapter implements Player {

	private final User USER;
	
	public UserPlayerAdapter(User u) {
		this.USER=u;
	}
	
	public User getUser(){
		return USER;
	}

	@Override
	public boolean equals(Object obj){
		if((obj instanceof UserPlayerAdapter)){
			if (((UserPlayerAdapter)obj).getUser().getId().equals(this.getUser().getId()))
				return true;
		}
			
		return false;
		
	}
}

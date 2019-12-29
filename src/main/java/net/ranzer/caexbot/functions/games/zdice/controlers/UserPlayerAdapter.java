package net.ranzer.caexbot.functions.games.zdice.controlers;

import net.ranzer.caexbot.functions.games.zdice.Player;
import net.dv8tion.jda.api.entities.User;

public class UserPlayerAdapter extends Player {

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
			return ((UserPlayerAdapter) obj).getUser().getId().equals(this.getUser().getId());
		}
			
		return false;
		
	}
}

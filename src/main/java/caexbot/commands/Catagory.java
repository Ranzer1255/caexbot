package caexbot.commands;

import java.awt.Color;

public enum Catagory {
			
	ADMIN("Admin", Color.RED),
	CHAT("Chat", Color.blue),
	GAME("Game", Color.GREEN),
	SEARCH("Search", Color.yellow),
	MUSIC("Music", Color.ORANGE);
	
	public final String NAME;
	public final Color COLOR;
	
	Catagory(String name, Color color){
		NAME = name;
		COLOR =color;
	}
	
	@Override
	public String toString(){
		return NAME;
	}
}

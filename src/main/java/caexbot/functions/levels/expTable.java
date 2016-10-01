package caexbot.functions.levels;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import caexbot.util.Logging;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class expTable {

	private static expTable instance;
	private static final String SAVE_LOCATION = "./src/main/resources/LevelSave";
	private static XPSaver save;
	private Map<Pair<Guild,User>, UserLevel> exp;
	
	private expTable(){
		save = new XPSaver(this, SAVE_LOCATION);
		exp = new HashMap<>();
	}
	
	public static expTable getInstance(){

		if(instance == null){
			instance = new expTable();
		}
		
		return instance;
	}

	public void addXP(Guild guild, User author, int XP) {

		Logging.debug(this, "Adding "+ XP + "XP to "+ author.getUsername()+":"+guild.getName());
		
		Pair<Guild,User> key = new ImmutablePair<>(guild, author);
		
		if(!exp.containsKey(key)){
			exp.put(key, new UserLevel(XP));
			return;
		}
		
		exp.get(key).addXP(XP);
		
		save.save(this);
		
	}
	
	
	public void load(){
		
	}
}
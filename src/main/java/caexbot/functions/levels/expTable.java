package caexbot.functions.levels;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import caexbot.util.Logging;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class expTable {

	private static expTable instance;
	private static final String SAVE_LOCATION = "./src/main/resources/LevelSave";
	private Map<Pair<Guild,User>, Integer> exp;
	
	private expTable(){}
	
	public static expTable getInstance(){

		if(instance == null){
			instance = new expTable();
		}
		
		return instance;
	}

	public void addXP(Guild guild, User author, int XP) {

		Pair<Guild,User> key = new ImmutablePair<>(guild, author);
		
		if(!exp.containsKey(new ImmutablePair<Guild, User>(guild, author))){
			exp.put(key, XP);
			return;
		}
		
		exp.put(key, new Integer(Integer.sum(XP, exp.get(key).intValue())));
		
	}
	
	private void save(){
		FileWriter f;
		try {
			f = new FileWriter(new File(SAVE_LOCATION),false);
		} catch (IOException e) {
			Logging.error(this, e.getMessage());
		}
		Set<Pair<Guild,User>> keys = exp.keySet();
		
		
		for (Pair<Guild, User> key : keys) {
			//TODO
		}
	}
	
	public void load(){
		
	}
}

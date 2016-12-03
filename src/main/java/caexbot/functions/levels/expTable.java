package caexbot.functions.levels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import caexbot.database.CaexDB;
import caexbot.util.Logging;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class expTable {

	private static expTable instance;
	private Map<Guild, Map<User, UserLevel>> exp;
	
	private expTable(){
		exp = new HashMap<>();
		load();
	}
	
	public static expTable getInstance(){

		if(instance == null){
			instance = new expTable();
		}
		
		return instance;
	}


		
	private void load(){
		exp.putAll(CaexDB.getLevels()); 
	}

	
}

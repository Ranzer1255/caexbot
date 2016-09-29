package caexbot.functions.levels;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class expTable {

	private static expTable instance;
	
	private Map<Pair<Guild,User>, Integer> exp;
	
	private expTable(){}
	
	public expTable getInstance(){

		if(instance == null){
			instance = new expTable();
		}
		
		return instance;
	}
}

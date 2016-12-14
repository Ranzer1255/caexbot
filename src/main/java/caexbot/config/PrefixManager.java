package caexbot.config;

import java.util.Map;

import caexbot.database.CaexDB;
import net.dv8tion.jda.core.entities.Guild;

public class PrefixManager {

	private static Map<Guild, String> prefixes = CaexDB.loadPrefixes();
	
	public static String getPrefix(Guild key){
		if (prefixes.get(key)==null){
			return CaexConfiguration.getInstance().getPrefix();
		}
		return prefixes.get(key);
	}
	
	public static void setPrefix(Guild key, String prefix){
		
		//this way we will only know about the lower case prefix
		prefix = prefix.toLowerCase();
		
		prefixes.put(key,prefix);
		CaexDB.savePrefix(key,prefix);
	}
	
	public static void removePrefix(Guild key){
		prefixes.remove(key);
		CaexDB.removePrefix(key);
	}
}

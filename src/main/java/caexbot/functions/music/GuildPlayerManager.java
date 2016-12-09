package caexbot.functions.music;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;

public class GuildPlayerManager {

	private static Map<Guild, GuildPlayer> players = new HashMap<Guild, GuildPlayer>();
	
	public GuildPlayer getPlayer(Guild k){
		GuildPlayer rtn = players.get(k);
		if(rtn == null){
			rtn = new GuildPlayer(k);
			players.put(k, rtn);
		}
		return rtn;
	}
}

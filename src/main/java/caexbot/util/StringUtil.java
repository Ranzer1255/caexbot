package caexbot.util;

import java.util.List;

import caexbot.commands.CaexCommand;
import net.dv8tion.jda.core.entities.Guild;

public class StringUtil {

	public static String cmdArrayToString(List<String> alias, String delmiter, Guild g) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < alias.size(); i++) {
			sb.append(CaexCommand.getPrefix(g));
			if(i==alias.size()-1){
				sb.append(alias.get(i));
			} else {
				sb.append(alias.get(i)).append(delmiter);
			}
		}
		
		return sb.toString();
	}
	
	public static String arrayToString(List<String> alias, String delmiter) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < alias.size(); i++) {
			if(i==alias.size()-1){
				sb.append(alias.get(i));
			} else {
				sb.append(alias.get(i)).append(delmiter);
			}
		}
		
		return sb.toString();
	}

	public static String calcTime(long runtime) {
		StringBuilder rtn = new StringBuilder();
		
		long hrs = runtime / 3600;
		long mins = (runtime % 3600)/60;
		long secs = runtime % 60; 
		
		if(hrs >0){
			rtn.append(hrs + " Hours ");
		}
		if(mins>0){
			rtn.append(mins + " Minutes ");
		}
		rtn.append(secs + " Seconds");
		
		return rtn.toString();
	}

}

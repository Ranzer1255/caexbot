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
		
		if(hrs >1){
			rtn.append(hrs + " Hours ");
		} else if (hrs==1){
			rtn.append(hrs + " Hour ");
		}
		if(mins>1){
			rtn.append(mins + " Minutes ");
		} else if (mins==1){
			rtn.append(mins + " Minute ");
		}
		if (secs>1){
			rtn.append(secs + " Seconds");
		} else if (secs==1){
			rtn.append(secs + " Second");
		}
		return rtn.toString();
	}
	
	public static String truncate(String string, int size) {
		if (string.length()<size) {
			return string;
		} else {
			return string.substring(0, size);
		}
	}


}

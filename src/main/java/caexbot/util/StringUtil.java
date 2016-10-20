package caexbot.util;

import java.util.List;

import caexbot.commands.CaexCommand;

public class StringUtil {

	public static String cmdArrayToString(List<String> alias, String delmiter) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < alias.size(); i++) {
			sb.append(CaexCommand.getPrefix());
			if(i==alias.size()-1){
				sb.append(alias.get(i));
			} else {
				sb.append(alias.get(i)).append(delmiter);
			}
		}
		
		return sb.toString();
	}

}

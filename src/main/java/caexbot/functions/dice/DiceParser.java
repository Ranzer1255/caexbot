package caexbot.functions.dice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import caexbot.util.Logging;

public class DiceParser {
	
	private static String pattern = "(?<num>\\d*)[dD](?<sze>\\d+)(?<mod>[+-]\\d+)?";
	private static Pattern p = Pattern.compile(pattern);
	
	/**
	 * <p>generates a <a>DieRoll</a> from the supplied string using a diluted form of standard RPG dice formatting</p>
	 * 
	 *  <p>only supports 1 die type per DieRoll and one lummpsum modifier</p>
	 *  
	 *  <p>IE: 1d20+15 ect.
	 * 
	 * <p>returns NULL if the pattern isn't matched
	 *  
	 * @param match String to build the DieRoll from
	 * @return a built DieRoll from the supplied string 
	 */
	public static DieRoll parseDiceString(String match){
		Matcher m = p.matcher(match);
		DieRoll rtn = null;
		
		Logging.debug(String.format("Testing if \"%s\" matches pattern: (%s) [%b]", match, pattern, m.matches()));
		if(m.matches()){
		
			String num = m.group("num");
			String sze = m.group("sze");
			String mod = m.group("mod");
			
			Logging.debug(String.format("values are: num: %s, sze: %s, mod: %s", num,sze,mod));
			int number, size, modifier;
			
			if (num!=null)
				number   = Integer.parseInt(num);
			else
				number	 = 1;
			size     = Integer.parseInt(sze);
			if (mod!=null)
				modifier = Integer.parseInt(mod);
			else
				modifier = 0;
			rtn = new DieRoll(number, size, modifier);
			
		} else {
			throw new IllegalDiceFormatException(match);
		}
		
		return rtn;
	}
	
	
	
}

package caexbot.functions.background;

import java.awt.Color;

import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;

public class ColorControler {

	private static CaexConfiguration config;
	
	
	public ColorControler(){
		config = CaexConfiguration.getInstance();
	}
	
	public static void setColor(JDA api, int colorHex){
		for (Guild g : api.getGuilds()) {
			g.getRolesByName(config.getRole())
				.get(0).getManager().setColor(colorHex).update();
		}
	}
	public static void setColor(JDA api, Color	color){
		for (Guild g : api.getGuilds()) {
			g.getRolesByName(config.getRole())
				.get(0).getManager().setColor(color).update();
		}
	}
	
	
	public static int getColor(JDA api){
		return api.getGuilds().get(0).getRolesByName(config.getRole()).get(0).getColor();
	}
}

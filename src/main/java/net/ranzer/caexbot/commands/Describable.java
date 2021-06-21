package net.ranzer.caexbot.commands;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;

public interface Describable {

	Category getCategory();
	
	String getName();
	List<String> getAlias();
	String getUsage(Guild g);
	String getShortDescription();
	String getLongDescription();
	Permission getPermissionRequirements();
	boolean hasSubcommands();
	List<CaexCommand> getSubcommands();
}

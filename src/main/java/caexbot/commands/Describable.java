package caexbot.commands;

import java.util.List;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;

public interface Describable {

	public Catagory getCatagory();
	
	public String getName();
	public List<String> getAlias();
	public String getUsage(Guild g);
	public String getShortDescription();
	public String getLongDescription();
	public Permission getPermissionRequirements();
}

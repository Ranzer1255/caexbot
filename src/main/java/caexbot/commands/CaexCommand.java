package caexbot.commands;

import java.util.List;

import caexbot.config.CaexConfiguration;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public abstract class CaexCommand{

	
	private static final String NO_PERMISSION_MESSAGE = "You're not my player! You can't tell me what to do!";

	public static String getPrefix() {
		return CaexConfiguration.getInstance().getPrefix();
	}

	public void runCommand(String[] args, User author, TextChannel channel, MessageReceivedEvent event){
		if(!hasPermission(args,author,channel,event)){
			noPermission(event);
			return;
		}
		
		process(args, author, channel, event);
	}
	
	abstract public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event);
	
	abstract public List<String> getAlias();
	
	abstract public String getDescription();
	
	abstract public String getUsage();

	private boolean hasPermission(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if(getPermissionRequirements()==null)
			return hasRoleRequirements(args, author,channel,event);
		for (Role role : event.getGuild().getRolesForUser(author)) {
			if(role.getPermissions().contains(getPermissionRequirements())){
				return true;
			}
		}
		return false;
	}

	private boolean hasRoleRequirements(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		if(getRoleRequirements()==null)
			return true;
		for(Role role : event.getGuild().getRolesForUser(author)){
			if(getRoleRequirements().contains(role.getName()))
				return true;
		}
		
		
		return false;
	}

	public List<String> getRoleRequirements() {
		return null;
	}

	public Permission getPermissionRequirements() {
		return null;
	}

	private void noPermission(MessageReceivedEvent event) {
		event.getChannel().sendMessage(event.getAuthor().getAsMention()+" "+NO_PERMISSION_MESSAGE);
		
	}
	
	public void invalidUsage(){
		
	}


}

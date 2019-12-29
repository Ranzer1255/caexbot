package caexbot.commands;

import java.util.List;

import caexbot.CaexBot;
import caexbot.config.CaexConfiguration;
import caexbot.data.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CaexCommand{

	
	private static final String NO_PERMISSION_MESSAGE = "You're not my player! You can't tell me what to do!";

	public static String getPrefix(Guild guild) {
		if (guild==null){
			return "";
		}
		return CaexBot.GUILD_MANAGER.getPrefix(guild);
	}

	public void runCommand(String[] args, MessageReceivedEvent event){
		if (!event.getAuthor().getId().equals(CaexConfiguration.getInstance().getOwner())) { //override all permission checks if its me
			if (!hasPermission(args, event)) {
				noPermission(event);
				return;
			} 
		}
		if(event.getGuild()==null && !isAplicableToPM()){
			event.getChannel().sendMessage("This command cannot be used in Private channels").queue();
			return;
		}
		process(args, event);
	}
	
	abstract public boolean isAplicableToPM();
	
	abstract public void process(String[] args, MessageReceivedEvent event);
	
	abstract public List<String> getAlias();
	private boolean hasPermission(String[] args, MessageReceivedEvent event) {
		if(getPermissionRequirements()==null)
			return hasRoleRequirements(args, event);
		for (Role role : event.getGuild().getMember(event.getAuthor()).getRoles()) {
			if(role.getPermissions().contains(getPermissionRequirements())){
				return true;
			}
		}
		return false;
	}

	private boolean hasRoleRequirements(String[] args, MessageReceivedEvent event) {
		if(getRoleRequirements()==null)
			return true;
		for(Role role : event.getGuild().getMember(event.getAuthor()).getRoles()){
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

	protected void noPermission(MessageReceivedEvent event) {
		event.getChannel().sendMessage(event.getAuthor().getAsMention()+" "+NO_PERMISSION_MESSAGE).queue();
		
	}
	
	public String invalidUsage(Guild g){
		return null;
	}

	public CaexCommand getCommand() {
		return this;
	}
	
	public String getName(){
		return getAlias().get(0);
	}
	
	public boolean hasSubcommands(){
		return false;
	}
	
	public List<CaexCommand> getSubcommands(){
		return null;
	}
	public String getUsage(Guild g) {
		return String.format("`%s%s`", getPrefix(g),getName());
	}

}

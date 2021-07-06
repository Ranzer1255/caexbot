package net.ranzer.caexbot.commands;

import java.util.List;
import java.util.Objects;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.ranzer.caexbot.config.CaexConfiguration;
import net.ranzer.caexbot.data.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class BotCommand {

	
	private static final String NO_PERMISSION_MESSAGE = "You're not my player! You can't tell me what to do!";

	public static String getPrefix(Guild guild) {
		if (guild==null){
			return "";
		}
		return GuildManager.getPrefix(guild);
	}

	public void runCommand(String[] args, MessageReceivedEvent event){
		if (!event.getAuthor().getId().equals(CaexConfiguration.getInstance().getOwner())) { //override all permission checks if its me
			if (!hasPermission(args, event)) {
				noPermission(event);
				return;
			} 
		}
		if(!event.isFromGuild() && !isApplicableToPM()){
			event.getChannel().sendMessage("This command cannot be used in Private channels").queue();
			return;
		}
		process(args, event);
	}
	
	abstract public boolean isApplicableToPM();
	
	abstract public void process(String[] args, MessageReceivedEvent event);
	
	abstract public List<String> getAlias();
	private boolean hasPermission(String[] args, MessageReceivedEvent event) {
		if(getPermissionRequirements()==null)
			return hasRoleRequirements(args, event);
		for (Role role : Objects.requireNonNull(event.getMember()).getRoles()) {
			if(role.getPermissions().contains(getPermissionRequirements())){
				return true;
			}
		}
		return false;
	}

	private boolean hasRoleRequirements(String[] args, MessageReceivedEvent event) {
		if(getRoleRequirements()==null)
			return true;
		for(Role role : Objects.requireNonNull(event.getMember()).getRoles()){
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

	public BotCommand getCommand() {
		return this;
	}
	
	public String getName(){
		return getAlias().get(0);
	}
	
	public boolean hasSubcommands(){
		return false;
	}
	
	public List<BotCommand> getSubcommands(){
		return null;
	}
	public String getUsage(Guild g) {
		return String.format("`%s%s`", getPrefix(g),getName());
	}

	//TODO make this abstract
	public CommandData getCommandData(){
		throw new RuntimeException("this method should not be called and isn't abstract only for testing");
	};

}

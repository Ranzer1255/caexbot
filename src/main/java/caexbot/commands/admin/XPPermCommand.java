package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.data.ChannelData;
import caexbot.data.GuildManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class XPPermCommand extends CaexCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {

		if (args.length==0){
			channel.sendMessage("This channel's XP setting is currently: "+
					GuildManager.getGuildData(event.getGuild()).getChannel(channel).getXPPerm()).queue();
			return;
		}
		if (args.length!=1||!(args[0].toLowerCase().equals("true")||args[0].toLowerCase().equals("false"))){
			channel.sendMessage("I'm sorry i didn't understand that please follow the usage\n"
								+getUsage(event.getGuild())).queue();
			return;
		}
		
		GuildManager.getGuildData(event.getGuild()).getChannel(channel).setXPPerm(Boolean.parseBoolean(args[0]));
		
		if(Boolean.parseBoolean(args[0])){
			channel.sendMessage("You will now earn XP in this channel").queue();
		} else {
			channel.sendMessage("you will now __not__ earn XP in this channel").queue();
		}
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("earn-xp");
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getAlias().get(0)+" [{true|false}]`";
	}

	@Override
	public String getShortDescription() {
		return "Set xp earnings for current text channel";
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return "This command sets the xp earning permmision for the channel within Caex\n"
				+ "leaving the value blank will return the current setting for this Channel\n"
				+ "**True:** users will earn xp in this channel\n"
				+ "**False:** users will not earn XP\n"
				+ "**Default Value:** " + ChannelData.DEFAULT_XP_SETTING;
	}

	@Override
	public Permission getPermissionRequirements() {
		
		return Permission.ADMINISTRATOR;
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.ADMIN;
	}
}

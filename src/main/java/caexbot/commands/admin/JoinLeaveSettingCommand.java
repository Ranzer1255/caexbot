package caexbot.commands.admin;

import java.util.Arrays;
import java.util.List;

import caexbot.CaexBot;
import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.data.GuildData;
import caexbot.data.GuildManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class JoinLeaveSettingCommand extends CaexCommand implements Describable {

	private static final String TRUE_MESSAGE = "Alright, I'll start telling you when someone Joins or Leaves the guild.";
	private static final String FALSE_MESSAGE = "Sounds good, I'll stop telling you when someone leaves or joins.";
	private static final String INFO_MESSAGE = "I am currently %sset to say when someone leaves or joins the guild.";

	@Override
	public void process(String[] args, MessageReceivedEvent event) {
		GuildData gd = CaexBot.GUILD_MANAGER.getGuildData(event.getGuild());
		if(args.length>0){
			//set alert toggle
			boolean setting = Boolean.parseBoolean(args[0]);
			gd.setJLannouncement(setting);
			if(setting){
				event.getChannel().sendMessage(TRUE_MESSAGE).queue();
			} else {
				event.getChannel().sendMessage(FALSE_MESSAGE).queue();
			}
			
		} else {
			//inform toggle
			event.getChannel().sendMessage(String.format(INFO_MESSAGE,
					gd.getJLannouncement()?"":"**__not__** ")).queue();
		}
	
	}

	@Override
	public String getShortDescription() {
		return "Toggle the setting for member leave and join alerts.";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n"
				+ "Caex must have an announcement channel set for this option to work.";
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%s%s [{true | false}]`",
				getPrefix(g),
				getName());
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("join-leave-alert","join-alert", "leave-alert");
	}
	
	@Override
	public Catagory getCatagory() {
		return Catagory.ADMIN;
	}

	@Override
	public boolean isAplicableToPM() {
		return false;
	}

	@Override
	public Permission getPermissionRequirements() {
		return Permission.ADMINISTRATOR;
	}
}

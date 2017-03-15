package caexbot.functions.games.zdice.subcommands;

import java.util.Arrays;
import java.util.List;

import caexbot.commands.CaexCommand;
import caexbot.commands.Catagory;
import caexbot.commands.Describable;
import caexbot.functions.games.zdice.controlers.ZomDiceDiscordControler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ZomEndTurnCommand extends CaexCommand implements Describable{

	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event) {
		ZomDiceDiscordControler.getInstance().setActiveChannel(channel);
		ZomDiceDiscordControler.getInstance().endTurn(author);
		
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("end","done","e","d");
	}

	@Override
	public String getShortDescription() {
		return "End your turn.";
	}

	@Override
	public Catagory getCatagory() {
		// TODO Auto-generated method stub
		return Catagory.GAME;
	}

	@Override
	public String getUsage(Guild g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongDescription() {
		// TODO Auto-generated method stub
		return getShortDescription();
	}

}

package net.ranzer.caexbot.functions.games.zdice.subcommands;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.dv8tion.jda.api.entities.Guild;

public abstract class AbstractZombieCommand extends CaexCommand implements Describable{

	@Override
	public boolean isAplicableToPM() {
		return false;
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%szomdice %s`", getPrefix(g), getName());
	}

	@Override
	public Catagory getCatagory() {
		return Catagory.GAME;
	}
	
	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n";
	}
}

package net.ranzer.caexbot.functions.games.zdice.subcommands;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.dv8tion.jda.api.entities.Guild;

public abstract class AbstractZombieCommand extends CaexCommand implements Describable{

	@Override
	public boolean isApplicableToPM() {
		return false;
	}
	
	@Override
	public String getUsage(Guild g) {
		return String.format("`%szomdice %s`", getPrefix(g), getName());
	}

	@Override
	public Category getCategory() {
		return Category.GAME;
	}
	
	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n";
	}
}

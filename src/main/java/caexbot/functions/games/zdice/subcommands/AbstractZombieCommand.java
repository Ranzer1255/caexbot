package caexbot.functions.games.zdice.subcommands;

import caexbot.commands.CaexCommand;

public abstract class AbstractZombieCommand extends CaexCommand {

	@Override
	public boolean isAplicableToPM() {
		return false;
	}
}

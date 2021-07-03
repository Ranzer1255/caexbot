package net.ranzer.caexbot.commands;

import java.util.List;

public interface DraconicCommand{

	List<String> getDraconicAlias();
	BotCommand getCommand();
}

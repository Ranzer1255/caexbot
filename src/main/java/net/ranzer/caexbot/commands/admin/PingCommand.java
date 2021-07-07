package net.ranzer.caexbot.commands.admin;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.commands.DraconicCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends BotCommand implements DraconicCommand, Describable{

	@Override
	public void processSlash(SlashCommandEvent event) {
		Date startTime = new Date();
		InteractionHook pong = event.reply("pong!").complete();
		Date endTime = new Date();
		long lag = endTime.getTime()-startTime.getTime();
		pong.editOriginal("pong! `"+lag+"ms`").queue();
	}

	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event) {
		Date startTime = new Date();
		Message pong = event.getChannel().sendMessage("pong!").complete();
		Date endTime = new Date();
		long lag = endTime.getTime()-startTime.getTime();
		pong.editMessage(pong.getContentDisplay()+" `"+lag+"ms`").queue();
		
	}

	@Override
	public List<String> getAlias() {
		return Collections.singletonList("ping");
	}

	@Override
	public String getShortDescription() {
		return "pong!";
	}
	
	@Override
	public String getLongDescription() {
		return "Tests the response time of the host server";
	}

	@Override
	public List<String> getDraconicAlias() {
		return Collections.singletonList("relgar");
	}

	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}

	@Override
	public CommandData getCommandData() {
		return new CommandData(getName(),getShortDescription());
	}
}

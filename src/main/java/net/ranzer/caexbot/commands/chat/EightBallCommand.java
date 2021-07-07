package net.ranzer.caexbot.commands.chat;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.caexbot.commands.BotCommand;
import net.ranzer.caexbot.commands.Category;
import net.ranzer.caexbot.commands.Describable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EightBallCommand extends BotCommand implements Describable{
	
	private final List<String> answers = Arrays.asList(
			"It is certain",
			"It is decidedly so",
			"Without a doubt",
			"Yes, definitely",
			"You may rely on it",
			"As I see it, yes",
			"Most likely",
			"Outlook good",
			"Yes",
			"Signs point to yes",
			"Reply hazy try again",
			"Ask again later",
			"Better not tell you now",
			"Cannot predict now",
			"Concentrate and ask again",
			"Don't count on it",
			"My reply is no",
			"My sources say no",
			"Outlook not so good",
			"Very doubtful"
	);
	
	@Override
	public void processPrefix(String[] args, MessageReceivedEvent event){
		event.getChannel().sendMessage(event.getAuthor().getAsMention()+": "+answers.get(ThreadLocalRandom.current().nextInt(answers.size()))).queue();
	}
	
	@Override
	public List<String> getAlias(){
		
		return Arrays.asList("8ball", "8b");
	}

	@Override
	public String getShortDescription() {
		return "Answers to your hearts most desired questions!";
	}
	@Override
	public String getLongDescription() {
		return "Looks to the future to give you the answer from the standard 8ball choices.\n";
	}

	@Override
	public String getUsage(Guild g){
		return "`"+getPrefix(g)+getName()+" [<Your question>]`";
	}
	
	@Override
	public Category getCategory() {
		return Category.CHAT;
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}
}

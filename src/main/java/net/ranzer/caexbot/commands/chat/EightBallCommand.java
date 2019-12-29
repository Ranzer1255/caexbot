package net.ranzer.caexbot.commands.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import net.ranzer.caexbot.commands.CaexCommand;
import net.ranzer.caexbot.commands.Catagory;
import net.ranzer.caexbot.commands.Describable;
import net.ranzer.caexbot.util.Logging;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EightBallCommand extends CaexCommand implements Describable{
	
	private List<String> answers;
	
	public EightBallCommand(){
		
		answers = loadAnswers();
		
		
	}

	private List<String> loadAnswers() {

		List<String> rtn = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/8BallAnswers.txt")));
			String line=null;
			while ((line = br.readLine())!= null) {
				rtn.add(line);
				Logging.debug("read in 8ball answer: "+line);
			}
			br.close();
		} catch ( Exception e ) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
		return rtn;
		
	}
	
	@Override
	public void process(String[] args, MessageReceivedEvent event){
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
	public Catagory getCatagory() {
		return Catagory.CHAT;
	}

	@Override
	public boolean isAplicableToPM() {
		return true;
	}
}

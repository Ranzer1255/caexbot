package caexbot.commands;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.util.Logging;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class EightBallCommand extends CaexCommand {
	
	private List<String> answers;
	
	public EightBallCommand(){
		
		answers = loadAnswers();
		
		
	}

	private List<String> loadAnswers() {

		List<String> rtn = new ArrayList<String>();
		
		try {
			Scanner br = new Scanner(new File("./src/main/resources/8BallAnswers.txt"));
			Logging.debug(String.valueOf(br.hasNextLine()));
			while (br.hasNextLine()) {
				String read = br.nextLine();
				rtn.add(read);
				Logging.debug("read in 8ball answer: "+read);
			}
			br.close();
		} catch ( Exception e ) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
		return rtn;
		
	}
	
	@Override
	public String getUsage(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(getPrefix()).append("8ball <the burrning question in your heart, begging for an answer>");
		
		return sb.toString();
	}
	
	@Override
	public void process(String[] args, User author, TextChannel channel, MessageReceivedEvent event){
		Logging.debug("Sending message to: "+channel.getGuild().getName()+": "+channel.getName());
		channel.sendMessage(author.getAsMention()+": "+answers.get(ThreadLocalRandom.current().nextInt(answers.size())));
	}
	
	@Override
	public List<String> getAlias(){
		
		return Arrays.asList("8ball", "8b");
	}

	@Override
	public String getDescription() {
		return "Answers to your hearts most desired questions!";
	}
}

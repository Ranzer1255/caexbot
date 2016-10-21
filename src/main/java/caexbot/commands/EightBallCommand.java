package caexbot.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.mysql.jdbc.log.Log;

import caexbot.CaexBot;
import caexbot.util.Logging;
import caexbot.util.StringUtil;
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
			InputStream resourceAsStream = getClass().getResourceAsStream("");
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
			String line=null;
			boolean test = (line = br.readLine())!= null;
			Logging.debug(String.valueOf(test));
			while (test) {
				test = (line = br.readLine())!= null;
				Logging.debug(String.valueOf(test));
				rtn.add(line);
				Logging.debug("read in 8ball answer: "+line);
			}
			Logging.debug(line);
			br.close();
		} catch ( Exception e ) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
		
		return rtn;
		
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

	@Override
	public String getUsage(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("**[").append(StringUtil.cmdArrayToString(getAlias(), ", ")).append("]** ").append("<the burrning question in your heart, begging for an answer>");
		
		return sb.toString();
	}
}

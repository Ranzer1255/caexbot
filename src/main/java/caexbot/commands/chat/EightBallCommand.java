package caexbot.commands.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import caexbot.commands.CaexCommand;
import caexbot.util.Logging;
import caexbot.util.StringUtil;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class EightBallCommand extends CaexCommand {
	
	private List<String> answers;
	
	public EightBallCommand(){
		
		answers = loadAnswers();
		
		
	}

	private List<String> loadAnswers() {

		List<String> rtn = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("8BallAnswers.txt")));
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

package caexbot.commands;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import net.dv8tion.jda.events.Event;
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
			while (br.hasNextLine()) {
				rtn.add(br.nextLine());				
			}
			br.close();
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
		
	}
	
	@Override
	public void runCommand(MessageReceivedEvent e){
		
		eanswers.get(ThreadLocalRandom.current().nextInt(answers.size()));
	}
}

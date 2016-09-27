package caexbot.commands;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class EightBallCommand implements CommandExecutor {
	
	private List<String> answers;
	
	public EightBallCommand(){
		
		answers = loadAnswers();
		
		
	}

	private List<String> loadAnswers() {

		List<String> rtn = new ArrayList<String>();
		
		try {
			Scanner br = new Scanner(new File(System.getProperty("user.home")+"/programing/java/workspace","/caexbot/src/main/resources/8BallAnswers.txt"));
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
	
//	private List<String> loadAnswers(){
//		List<String> rtn = new ArrayList<>();
//		
//		rtn.add("It is certain");
//		rtn.add("It is decidedly so");
//		rtn.add("Without a doubt");
//		
//		return rtn;
//	}
	
	@Command(aliases={"8ball"}, description="Answer live's questions")
	public String eightBallCommand(){
		return answers.get(ThreadLocalRandom.current().nextInt(answers.size()));
	}
}

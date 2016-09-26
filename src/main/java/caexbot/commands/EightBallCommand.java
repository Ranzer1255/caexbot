package caexbot.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.btobastian.sdcf4j.CommandExecutor;

public class EightBallCommand implements CommandExecutor {
	
	private List<String> answers;
	
	public EightBallCommand(){
		
		answers = loadAnswers();
		
		
	}

	private List<String> loadAnswers() {

		List<String> rtn = new ArrayList<String>();
		
		try {
			Scanner sc = new Scanner(new File("/caexbot/src/main/resources/8BallAnswers.txt"));
			while (sc.hasNextLine()) {
				rtn.add(sc.nextLine());				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
		
	}
}

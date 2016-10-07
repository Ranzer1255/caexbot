package caexbot.functions.dice;

import java.util.concurrent.ThreadLocalRandom;

public class DieRoll {

	int numDice, dieSize, modifier;
	
	public DieRoll(int number, int size, int modifier) {
		numDice = number;
		dieSize = size;
		this.modifier = modifier;
	}
	
	public int roll(){
		
		int result=0;
		for(int i = 0;i<numDice;i++){
			result += ThreadLocalRandom.current().nextInt(0, dieSize)+1;
		}
		result += modifier;
		
		return result;
	}
	
	public String Result(){
		StringBuilder rtn = new StringBuilder();
		
		rtn.append("Rolling ").append(numDice).append("d").append(dieSize);
		if(modifier<0)
			rtn.append(modifier);
		else 
			rtn.append("+").append(modifier);
		
		rtn.append(" = (");
		int total=0;
		for(int i = 0;i<numDice;i++){
			
			int result = ThreadLocalRandom.current().nextInt(0, dieSize)+1;
			rtn.append(result).append(", ");
			total+=result;
		}
		rtn.delete(rtn.length()-2, rtn.length()).append(")");
		if(modifier<0)
			rtn.append(modifier);
		else 
			rtn.append("+").append(modifier);
		
		total += modifier;
		rtn.append(" = ").append(total);
		
		return rtn.toString();
		
	}
}

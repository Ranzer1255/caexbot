package caexbot.functions.dice;

import java.util.concurrent.ThreadLocalRandom;

public class DieRoll {

	private final int numDice, dieSize, modifier;
	private boolean isRolled;
	private int[] results;
	private int total;
	
	public int getNumDice() {
		return numDice;
	}

	public int getDieSize() {
		return dieSize;
	}

	public int getModifier() {
		return modifier;
	}

	public boolean isRolled() {
		return isRolled;
	}

	public int[] getResults() {
		return results;
	}

	public int getTotal() {
		return total;
	}

	public DieRoll(int number, int size, int modifier) {
		numDice = number;
		dieSize = size;
		this.modifier = modifier;
		isRolled=false;
		results = new int[number];
	}
	
	public int roll(){
		
		total=0;
		for(int i = 0;i<numDice;i++){
			int roll = ThreadLocalRandom.current().nextInt(0, dieSize)+1;
			results[i]=roll;
			total += roll;
			
		}
		total += modifier;
		isRolled = true;
		return total;
	}
	
	public String result(){
	
		return String.format("Rolling %dd%d = %d", numDice, dieSize, total);
	}
	
	public String verboseResult(){
		if(!isRolled){
			roll();
		}
		StringBuilder rtn = new StringBuilder();
		
		rtn.append("Rolling ").append(numDice).append("d").append(dieSize);
		if(modifier<0)
			rtn.append(modifier);
		else if (modifier == 0);
		else 
			rtn.append("+").append(modifier);
		
		rtn.append(" = (");
		
		for(int i = 0;i<results.length;i++){
			rtn.append(results[i]).append(", ");
		}
		rtn.delete(rtn.length()-2, rtn.length()).append(")");
		if(modifier<0)
			rtn.append(modifier);
		else if (modifier==0);
		else 
			rtn.append("+").append(modifier);
		rtn.append(" = ").append(total);
		
		return rtn.toString();
		
	}
}

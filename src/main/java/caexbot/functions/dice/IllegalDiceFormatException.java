package caexbot.functions.dice;

public class IllegalDiceFormatException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1667534027118949691L;

	public IllegalDiceFormatException(String message) {
		super(message);
	}

}
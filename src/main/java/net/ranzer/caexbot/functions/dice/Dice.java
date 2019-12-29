package net.ranzer.caexbot.functions.dice;

import java.util.ArrayList;

import net.ranzer.caexbot.functions.dice.expressions.Addition;

public class Dice {

	private final ArrayList<Lexer.Token> tokens;
	private String breakdown;

	public Dice(String str) {
		tokens = Lexer.lex(str);

	}

	public String getBreakdown() {
		return breakdown;
	}

	public int roll() {
		Addition expr = new Addition(tokens);
		breakdown = expr.description;
		return expr.value;
	}
}	
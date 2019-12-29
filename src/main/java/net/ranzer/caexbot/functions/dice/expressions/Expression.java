package net.ranzer.caexbot.functions.dice.expressions;

import java.util.ArrayList;

import net.ranzer.caexbot.functions.dice.Lexer;

/**
 * Abstract class for all expressions. Has a list of tokens (atomic, die, addition, etc), a value
 * (the end result, e.g. 19) a description (the explanation, e.g. "1d20")
 *
 * @author Tijmen
 */
public abstract class Expression {

	public ArrayList<Lexer.Token> tokens;
	public int value;
	public String description = "";

	public Expression(ArrayList<Lexer.Token> tokens) {
		this.tokens = tokens;
	}
}
package net.ranzer.caexbot.functions.dice.expressions;

import java.util.ArrayList;

import net.ranzer.caexbot.functions.dice.Lexer;

public class Atomic extends Expression {

	public Atomic(ArrayList<Lexer.Token> tokens) {
		super(tokens);
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).type == Lexer.TokenType.ATOMIC) {
				value = Integer.parseInt(tokens.get(i).data);
			}
			description += tokens.get(i).data;
		}
	}

}

package com.propositions;

import java.util.*;
/*
 * expression	-> literal
 * 				-> unary
 * 				-> binary
 * 				-> grouping
 * 
 * literal	= name of proposition
 * binary 	= expression OPERATOR expression
 * unary	= ! expression
 * grouping	= ( expression )
 */

// PRECEDENCE LEVELS
/*
 * expression 	-> binary
 * binary		-> unary ( & ) unary 
 * 				-> unary ( | ) unary 
 * 				-> unary ( ->) unary 
 * 				-> unary (<->) unary
 * unary		-> ( "!" | "-" ) unary
 * 				    | (primary)
 * primary		->	name of proposition
 * 				    | "(" expression ")"
 */
public class PropositionParser {
	public static class ParseError extends RuntimeException {
		ParseError(String message) { super(message); }
	}
	
	private final ArrayList<PropositionTokenizer.Token> tokens;
	protected final Expr parsed;
	private int current = 0;
	
	public PropositionParser(ArrayList<PropositionTokenizer.Token> tokenized) {
		tokens = tokenized;
		parsed = this.parse();
		// This is to be passed from PropositionTokenizer.PropositionTokenize
	}
	
	public String toString() {
		return PropositionTreePrinter.TreeStringBuilder(this.parsed);
	}
	
	// ---------------------------------------------
	//     HELPER METHODS FOR ANALYZING TOKENS
	// Implemented and based off of Robert Nystrom's Lox
	// 			Programming Language.
	
	
	/*
	 * Matches tokens to see if they are of a specific type
	 * Defined in terms of one or more fundamental operations
	 */
	private boolean match(TokenType... types) {
		for (TokenType type : types) {
			if (check(type)) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	// Returns true if the peeked token is of a specified type, passed
	// Returns false if at the end of the expression, or if peeked token is not of type
	private boolean check(TokenType type) {
		if (isAtEnd()) return false;
	    return peek().type == type;
	}
	
	// Advances to the next token in the counter to find out whether it works. 
	// Returns what was previously the current token
	private PropositionTokenizer.Token advance() {
		if (!isAtEnd()) current++;
	    return previous();
	}
	
	// Checks to see if we've run out of tokens to parse through.
	// Utilizes the EOP TokenType to verify end of proposition
	private boolean isAtEnd() {
		return peek().type == TokenType.EOP;
	}
	
	// Returns current Tokens without consuming it
	private PropositionTokenizer.Token peek() {
		return tokens.get(current);
	}
	
	// Returns previous Token without consuming it
	private PropositionTokenizer.Token previous() {
		return tokens.get(current - 1);
	}
	
	// checks to see if the next token is of the expected type
	// If another token is there, throws an error
	private PropositionTokenizer.Token consume(TokenType type, String message) {
		if (check(type)) return advance();
	    throw error(type, message);
	}
	
	// Returns new ParseError in the event parsing fails.
	private ParseError error(TokenType type, String message) {
		String errorMessage = "Error during Proposition Parse: " 
				+ message + " of expected type: " + type.name();
		return new ParseError(errorMessage);
	}
	
	// END HELPER METHODS
	// ---------------------------------------------
	// START NESTED RECURSION DEFINITIONS
	
	private Expr expression() {
		return binaryIFF();
	}

	/* Outdated binary() method

	private Expr binary() {
		Expr expr = unary();
		
		while (match(TokenType.AND, TokenType.OR, 
				TokenType.IF_AND_ONLY_IF, 
				TokenType.IMPLIES) ) {
			Token operator = previous();
		    Expr right = unary();
		    expr = new Expr.BinaryOperation(expr, operator, right);
		}
		
		return expr;
	} */
	
	/* The following I might not implement, or will, depending on whether there are
	 * issues with precedence between AND OR IMPLIES and IFF */
	
	private Expr binaryIFF() {
		Expr expr = binaryIMPLIES();
		
		while (match(TokenType.IF_AND_ONLY_IF)) {
			PropositionTokenizer.Token operator = previous();
		    Expr right = binaryIMPLIES();
		    expr = new Expr.BinaryOperation(expr, operator, right);
		}
		
		return expr;
	}
	
	private Expr binaryIMPLIES() {
		Expr expr = binaryOR();
		
		while (match(TokenType.IMPLIES)) {
			PropositionTokenizer.Token operator = previous();
		    Expr right = binaryOR();
		    expr = new Expr.BinaryOperation(expr, operator, right);
		}
		
		return expr;
	}
	
	private Expr binaryOR() {
		Expr expr = binaryAND();
		
		while (match(TokenType.OR) ) {
			PropositionTokenizer.Token operator = previous();
		    Expr right = binaryAND();
		    expr = new Expr.BinaryOperation(expr, operator, right);
		}
		
		return expr;
	}
	
	private Expr binaryAND() {
		Expr expr = unary();
		
		while (match(TokenType.AND) ) {
			PropositionTokenizer.Token operator = previous();
		    Expr right = unary();
		    expr = new Expr.BinaryOperation(expr, operator, right);
		}
		
		return expr;
	}
	
	private Expr unary() {
		if (match(TokenType.NOT)) {
			PropositionTokenizer.Token operator = previous();
			Expr right = unary();
			return new Expr.Unary(operator, right);
		}
		
		return primary();
	}
	
	private Expr primary() {
		if (match(TokenType.LEFT_PAR)) {
			Expr expr = expression();
			consume(TokenType.RIGHT_PAR, "Expecting ')' within expression.");
			return new Expr.Grouping(expr);
		}
		if (match(TokenType.NAME)) {
			// If it encounters that the next token is either another NAME or a LEFT_PAR token
			// Throw an error. Either occurrence of token combinations is syntactically illegal
			// E.g. [ A ( B ) ] or [ A B ] should throw an error
			if ((peek().type == TokenType.NAME)
					|| (peek().type == TokenType.LEFT_PAR)) {
				throw error(peek().type, "Error processing expression. Issue with tokens");
			}
			return new Expr.Literal(previous());
		}
		throw error(peek().type, "Expected a second proposition after the operator when processing expression");
	}
	
	// ---------------------------------------------
	
	Expr parse() {
		return expression();
	}
	
}

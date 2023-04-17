package com.propositions;

import com.propositions.PropositionTokenizer.*;

// Now comes the part where we'll have to actually represent each of our functions
	// How can we represent operations?
		// Binary operations, where each of the operations are evaluated left to right
		// Operations with expression on the left, which needs to be evaluated
		// Operations with expression on the right
		// Operations with expressions on both sides
	
	// Operator Precedence
		// Expression
		// ()		(GROUPINGS)
		// !		(NOT)
		// &		(AND)
		// |		(OR)
		// -> <> 	(IFF, both reference same thing)
	/*
	 * expression 	-> binary
	 * binary		-> unary (and | or | -> | <->) unary
	 * unary		-> ( "!" | "-" ) unary 
	 * 				   | primary
	 * primary		->	name of proposition | "(" expression ")"
	 * 
	 */

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
	
	
abstract class Expr {
	
	static class BinaryOperation extends Expr {
		final Expr left;
		final Token operation;
		final Expr right;
		public BinaryOperation(Expr l, Token op, Expr r) {
			if ((op.type == TokenType.NAME) 
					|| (op.type == TokenType.LEFT_PAR)
					|| (op.type == TokenType.RIGHT_PAR)) {
				throw new IllegalArgumentException("Improper Binary Operation Arguments, PropositionExpr.java");
			}
			left = l;
			operation = op;
			right = r;
		}
	}
	
	// Simple Node to represent simple, single propositions, just names
	static class Literal extends Expr {
		final Token name;
		public Literal(Token node) {
			if (node.type != TokenType.NAME) {
				throw new IllegalArgumentException("LiteralNode can only have"
						+ "Tokens of TokenType.NAME");
			}
			name = node;
		}
		Token getName() {
			return name;
		}
	}
	
	// Explicitly for negations
	static class Unary extends Expr {
		final Token operator;
		final Expr expression;
		public Unary(Token operator, Expr expression) {
			this.operator = operator;
			this.expression = expression;
		}
		Token operator() {
			return operator;
		}
		
	}
	
	// Groupings and Parentheses
	static class Grouping extends Expr {
		final Expr expression;
		public Grouping(Expr expression) {
			this.expression = expression;
		}
	}
	
	// 
}

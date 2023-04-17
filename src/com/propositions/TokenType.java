package com.propositions;

enum TokenType {
	// Operators
	NOT, AND, OR, IMPLIES, IF_AND_ONLY_IF,
	// Proposition Name
	NAME,
	// Parentheses
	LEFT_PAR, RIGHT_PAR,
	// End of Proposition
	EOP
}
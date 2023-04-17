package com.propositions;

import java.util.*;

@SuppressWarnings("unused")
public class PropositionTokenizer {
	// A class that defines what a token is. 
	static class Token {
		final TokenType type;
		String lexeme;
		boolean truth_value;
		
		Token(TokenType type, String lexeme) {
			this.type = type;
			this.lexeme = lexeme;
		}
		
		Token(TokenType type, String lexeme, boolean truth) {
			this.type = type;
			this.lexeme = lexeme;
			this.truth_value = truth;
		}

		public String toString() {
			return lexeme;
		}
		
		public void lexemeUpdate(char c) {
			this.lexeme = lexeme + Character.toString(c);
		}

		public void update_truth_value(boolean new_truth_value) { truth_value = new_truth_value; }
		
	}
	
	// A function that syntactically runs through a compound proposition to evaluate if there's illegal characters.
	public static boolean PropositionValidifier(String compoundProp) {
		// Given any string compoundProp that represents a
		// compound proposition, we will do this
		// for every character within the length of compound proposition do the following operations
		for (int i = 0; i < compoundProp.length(); i++) {
			// If there exist a character where it is NOT compliant with the following
				//	Any alphabetical letter		!	&	|	->		<>	( 	)	spaces
				// return false
			// OTHER FUNCTIONS NOT COMPLETED YET, REMOVE IllegalArgumentException WHEN APPROPRIATE
			if (
					!((Character.isLetter(compoundProp.charAt(i))) ||
					( compoundProp.charAt(i) == '!' ) ||
					( compoundProp.charAt(i) == '&' ) ||
					( compoundProp.charAt(i) == '|' ) ||
					( compoundProp.charAt(i) == '-' ) ||
					( compoundProp.charAt(i) == '<' ) ||
					( compoundProp.charAt(i) == '>' ) ||
					( compoundProp.charAt(i) == '(' ) ||
					( compoundProp.charAt(i) == ')' ) ||
					( compoundProp.charAt(i) == ' ' ))
					) {
						return false;
					}
		} return true;
	}
	
	// Added another function to my PropositionTokenizer class!
	// Basically checks for proper parentheses balance in a proposition
	public static boolean CheckParenthesesBalance(String compoundProp) {
		// Given any string compoundProp that represents a compound proposition, we will do this
		// for every character within the length of compound proposition do the following operations
		
		// Preliminary checks for invalid parentheses arguments
		if ((compoundProp.charAt(0) == ')') || 
				(compoundProp.charAt(compoundProp.length() - 1) == '(')) {
			return false;
		}
		
		// Create a stack to check parentheses balance
		Stack<Integer> parentheses = new Stack<Integer>();
		// For each character, push 1 to stack if '(', pop if ')'.
		// If it cannot pop, then it catches the exception and returns false
		// Otherwise, return whether the stack is empty
		for (int i = 0; i < compoundProp.length(); i++) {
			char nextChar = compoundProp.charAt(i);
			if (nextChar == '(') {
				parentheses.push(1);
			} else if (nextChar == ')') {
				try {
					parentheses.pop();
				} catch(Exception e) {
					return false;
				}
			}
		}
		return parentheses.empty();
	}
	
	// A function that parses through and TOKENIZES every character within the compoundProp,
	// It should ideally return an ArrayList of type Character containing every little token
	// This operates regardless of whether the actual phrase is syntactically correct
	// This is not the same as our final tokenizer function, which will go through and ACTUALLY classify each eventual lexeme
		// NOTE: Each character is just a character
		// A lexeme is a string of characters representing one of the following:
			// Name, Operators, or Parentheses
	public static ArrayList<Character> PropositionCharacterizer(String compoundProp) {
		// First operation is to check through and see whether each character within compoundProp is valid.
		if (!PropositionValidifier(compoundProp)) {
			throw new IllegalArgumentException("Illegal characters found within compound proposition entered.\n"
					+ "Compound Propositions must be alphabetical and contain only "
					+ "&, |, (, ), ->, <>, spaces, and other symbols");
		}
		
		// Second operation is to check through and see whether all parentheses in the String is balanced.
		if (!CheckParenthesesBalance(compoundProp)) {
			throw new IllegalArgumentException("Imbalanced Set of Parentheses");
		}
		
		// Afterwards, initialize a new object of ArrayList<Character>, tokenizedProp
		ArrayList<Character> characterizedProp = new ArrayList<Character>();
		// Iterate through it, adding each character to the ArrayList;
		for (int i = 0; i < compoundProp.length(); i++) {
			characterizedProp.add(compoundProp.charAt(i));
		}
		return characterizedProp;
	}
	
	// --------------------------------------------------------
	// --------------------------------------------------------
	// --------------------------------------------------------
	
	// Now for the real stuff
	
	private static final Map<String, TokenType> keywords;
	
	// Might be BS code. Ignore for now until proper interpretation is added LUL
	// This was originally here to basically figure out which TokenType to add depending on the character
	// However, utilizing 
	static {
		keywords = new HashMap<>();
		keywords.put("!", TokenType.NOT);
		keywords.put("&", TokenType.AND);
		keywords.put("|", TokenType.OR);
		keywords.put("->", TokenType.IMPLIES);
		keywords.put("<>", TokenType.IF_AND_ONLY_IF);
		keywords.put("<->", TokenType.IF_AND_ONLY_IF);
		keywords.put("(", TokenType.LEFT_PAR);
		keywords.put(")", TokenType.RIGHT_PAR);
		keywords.put("", TokenType.EOP);
	}
	
	// Helper methods to automatically make adding tokens e
	// Helper function to add a name token to the list of tokens
	private static Token addToken(String lexeme) {
		return addToken(TokenType.NAME, lexeme);
	}
	
	// Helper function 
	public static Token addToken(TokenType type, String lexeme) {
		return new Token(type, lexeme);
	}
	
	public static Token addEndToken() {
		return new Token(TokenType.EOP, "EOP");
	}
	
	// Helper function to provide a quick peek for the only two letter logical operations we have
	private static boolean peek(ArrayList<Character> charList, int i) {
		if (i < charList.size()-1) {
			return ('>' == charList.get(i+1));
		}
		return false;
	}

	// Helper function to provide a quick peek for the only three letter logical operation, <->
	private static boolean peek_two(ArrayList<Character> charList, int i) {
		if (i < charList.size()-2) {
			return ('-' == charList.get(i+1)) && ('>' == charList.get(i+2));
		}
		return false;
	}
	
	// Helper function to return whether a character is alphabetic
	private static boolean isAlpha(char c) {
	    return (c >= 'a' && c <= 'z') ||
	           (c >= 'A' && c <= 'Z') ||
	            c == '_';
	}
	
	
	
	// The following function now tokenizes the whole set of characters into a set of ordered lexemes
	// Given an argument String compoundProp, it will organize the string into its correct lexemes and go from there.
	public static ArrayList<Token> PropositionTokenize(String compoundProp) {
		// First things first, make sure everything is valid and the characters go hand in hand. 
		ArrayList<Character> characterizedProp = PropositionCharacterizer(compoundProp);
		
		// Then, define the tokens variable as an ArrayList; we will use it to return our tokens.
			// To make things easier, we will use our previously defined Token class
		ArrayList<Token> tokens = new ArrayList<Token>();
		
		// Define several helper variables to iterate through each value within characterizedProp
			// counter is used to count through the characterizedProp elements
			// tokenCounter counts the specific word that is going through. 
		int counter = 0;
		int tokenCounter = -1;
		boolean newToken = true;
		
		while (counter < characterizedProp.size()) {
			char c = characterizedProp.get(counter);
			switch(c) {
				// Spaces
				case ' ':
					counter++;
					newToken = true;
					break;
					
				// Single-Letter Operations
				case '(': 
					tokens.add(addToken(TokenType.LEFT_PAR, "("));
					counter++;
					tokenCounter++;
					newToken = true;
					break;
				case ')': 
					tokens.add(addToken(TokenType.RIGHT_PAR, ")")); 
					counter++;
					tokenCounter++;
					newToken = true;
					break;
				case '!': 
					tokens.add(addToken(TokenType.NOT, "!"));
					counter++;
					tokenCounter++;
					newToken = true;
					break;
				case '&': 
					tokens.add(addToken(TokenType.AND, "&"));
					counter++;
					tokenCounter++;
					newToken = true;
					break;
				case '|': 
					tokens.add(addToken(TokenType.OR, "|"));
					counter++;
					tokenCounter++;
					newToken = true;
					break;
				
					// Two-letter Operations
				case '<':
					if (peek(characterizedProp, counter)) {
						tokens.add(addToken(TokenType.IF_AND_ONLY_IF, "<>"));
						counter += 2;
						tokenCounter++;
						newToken = true;
						break;
					} else if (peek_two(characterizedProp, counter)) {
						tokens.add(addToken(TokenType.IF_AND_ONLY_IF, "<->"));
						counter += 3;
						tokenCounter++;
						newToken = true;
						break;
					} else {
						throw new IllegalArgumentException
								("Illegal Proposition Operation: Did you mistype an if-and-only-if (<-> or <>) statement");
					}
				case '-':
					if (peek(characterizedProp, counter)) {
						tokens.add(addToken(TokenType.IMPLIES, "->"));
						counter += 2;
						tokenCounter++;
						newToken = true;
						break;
					} else {
						throw new IllegalArgumentException
								("Illegal Proposition Operation: Did you mistype an implies (->) statement?");
					}
				default:
					if (isAlpha(c)) {
						if (newToken == false) {
							tokens.get(tokenCounter).lexemeUpdate(c);
							counter++;
							break;
						} else {
							newToken = false;
							tokens.add(addToken(Character.toString(c)));
							counter++;
							tokenCounter++;
							break;
						}
					}
					break;
			}
		}
		
		tokens.add(addEndToken());
		
		return tokens;
	}
	
}

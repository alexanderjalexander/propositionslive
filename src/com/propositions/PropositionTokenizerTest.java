package com.propositions;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
//import java.io.*;
import org.junit.jupiter.api.Test;
import com.propositions.PropositionTokenizer.*;

class PropositionTokenizerTest {

	@Test
	void testPropositionValidifier() {
		assertTrue(PropositionTokenizer.PropositionValidifier("!P & !Q"));
		assertTrue(PropositionTokenizer.PropositionValidifier("!Pants | !Queso"));
		assertFalse(PropositionTokenizer.PropositionValidifier("tP92 & nQ45"));
	}
	
	@Test
	void testCheckParenthesesBalance() {
		assertTrue(PropositionTokenizer.CheckParenthesesBalance("(!P & !Q)"));
		assertTrue(PropositionTokenizer.CheckParenthesesBalance("()()(())"));
		assertFalse(PropositionTokenizer.CheckParenthesesBalance("))"));
		assertFalse(PropositionTokenizer.CheckParenthesesBalance("(("));
		assertFalse(PropositionTokenizer.CheckParenthesesBalance("((())"));
		
	}

	@Test
	void testPropositionCharacterizer() {
		ArrayList<Character> charList = PropositionTokenizer.PropositionCharacterizer("!P & !Q");
		assertEquals("[!, P,  , &,  , !, Q]", charList.toString());
		try {
			charList = PropositionTokenizer.PropositionCharacterizer("!P9 & !Q");
			fail("Did not catch exception.");
		} catch (IllegalArgumentException e) {
			assertEquals(1,1);
		}
		
	}

	@Test
	void testPropositionTokenize() {
		//fail("Need to implement better");
		ArrayList<Token> tokenList = PropositionTokenizer.PropositionTokenize("!Pants & !Q");
		assertEquals("[!, Pants, &, !, Q, EOP]", tokenList.toString());
	}
	
	@Test
	void testPropositionTokenizeType() {
		ArrayList<Token> tokenList = PropositionTokenizer.PropositionTokenize("!Pants & !Q");
		assertEquals(tokenList.get(0).type, TokenType.NOT);
		assertEquals(tokenList.get(1).type, TokenType.NAME);
		assertEquals(tokenList.get(2).type, TokenType.AND);
		assertEquals(tokenList.get(3).type, TokenType.NOT);
		assertEquals(tokenList.get(4).type, TokenType.NAME);
		assertEquals(tokenList.get(5).type, TokenType.EOP);
	}
}

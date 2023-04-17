package com.propositions;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import com.propositions.PropositionParser.*;

import org.junit.jupiter.api.Test;

class PropositionParserTest {
	
	@Test
	void test1() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
				PropositionTokenize("!P & !Q");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( & ( !P ) ( !Q ) )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse. Error: " + error.getMessage());
		}
	}
	
	@Test
	void test2() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
				PropositionTokenize("(!P) & (!Q)");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( & [ ( !P ) ] [ ( !Q ) ] )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}
	
	@Test
	void test3() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
				PropositionTokenize("!(P | Q) & (!P & !Q)");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( & ( ![ ( | P Q ) ] ) [ ( & ( !P ) ( !Q ) ) ] )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}

	@Test
	void test4() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("!P & !Q & !P & !Q");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( & ( & ( & ( !P ) ( !Q ) ) ( !P ) ) ( !Q ) )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}

	@Test
	void test5() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("a & b | c & d | e & f");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( | ( | ( & a b ) ( & c d ) ) ( & e f ) )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}

	@Test
	void test6() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("!a -> b | c <> d | e & f");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( <> ( -> ( !a ) ( | b c ) ) ( | d ( & e f ) ) )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}

	@Test
	void test7() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("!(A & B)");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( ![ ( & A B ) ] )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}

	@Test
	void test8() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A -> B -> C -> D -> E -> F -> G -> H -> I -> J");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( -> ( -> ( -> ( -> ( -> ( -> ( -> ( -> ( -> A B ) C ) D ) E ) F ) G ) H ) I ) J )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}

	@Test
	void test9() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("(A <-> B) <> C");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( <> [ ( <-> A B ) ] C )", parse.toString());
		} catch (ParseError error) {
			fail("Could not successfully parse.");
		}
	}

	@Test
	void test10() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("!!!!Dingus");
		PropositionParser parse;
		try {
			parse = new PropositionParser(tokens);
			assertEquals("( !( !( !( !Dingus ) ) ) )", parse.toString());
		} catch (ParseError error) {
			fail("Exception was caught." + error);
		}
	}

	// ----------------------- ILLEGAL TESTS ----------------------- //
	// Every single one of the tests below should result in some     //
	// sort of exception. God forbid it doesn't, then the parser     //
	// needs some work.                                              //
	// ------------------------------------------------------------- //

	@Test
	void test11() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A B C D E F G");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test12() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A & B C D E F G");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test13() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A & & B");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test14() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A | | B");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test15() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A -> -> B");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test16() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A <-> <-> B");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test17() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("A ( B ( C ( D ) ) )");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test18() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("( ( ( A ) B ) C )");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}

	@Test
	void test19() {
		ArrayList<PropositionTokenizer.Token> tokens =
				PropositionTokenizer.
						PropositionTokenize("!( !( !( A ) B ) C )");
		assertThrows(ParseError.class, () -> {
			final PropositionParser parse = new PropositionParser(tokens);
		});
	}
}

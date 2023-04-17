package com.propositions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PropositionsTest {

	@Test
	void testAndFunction() {
		assertTrue(Propositions.and(true, true));
		assertFalse(Propositions.and(true, false));
		assertFalse(Propositions.and(false, true));
		assertFalse(Propositions.and(false, false));
	}

	@Test
	void testOr() {
		assertTrue(Propositions.or(true, true));
		assertTrue(Propositions.or(true, false));
		assertTrue(Propositions.or(false, true));
		assertFalse(Propositions.or(false, false));
	}

	@Test
	void testNot() {
		assertTrue(Propositions.not(false));
		assertFalse(Propositions.not(true));
	}

	@Test
	void testImplies() {
		assertTrue(Propositions.implies(true, true));
		assertFalse(Propositions.implies(true, false));
		assertTrue(Propositions.implies(false, true));
		assertTrue(Propositions.implies(false, false));
	}

	@Test
	void testIfAndOnlyIf() {
		assertTrue(Propositions.ifAndOnlyIf(true, true));
		assertFalse(Propositions.ifAndOnlyIf(true, false));
		assertFalse(Propositions.ifAndOnlyIf(false, true));
		assertTrue(Propositions.ifAndOnlyIf(false, false));
	}

	@Test
	void testPropositionParser() {
		assertEquals("[!, P]", Propositions.propositionParser("!P").toString());
		assertEquals("[!, P, o, o, p]", Propositions.propositionParser("!Poop").toString());
		try {
			Propositions.propositionParser("78");
			fail("Expected exception");
		} catch (IllegalArgumentException E) {
			assertEquals(1,1);
		}
	}

	@Test
	void testSimpleProposition() {
		assertFalse(Propositions.simplePropositionEvaluate("!P"));
		assertTrue(Propositions.simplePropositionEvaluate("P"));
	}

}

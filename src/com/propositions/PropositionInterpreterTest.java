package com.propositions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

import com.propositions.PropositionInterpreter;
import org.junit.jupiter.api.Test;

class PropositionInterpreterTest {

    // Simple Evaluation Tests
    @Test
    void test1() {
        PropositionInterpreter propinterp = new PropositionInterpreter("!P & !Q", true);
        System.out.println("!P & !Q = " + propinterp.truth_value);
        assertFalse(propinterp.truth_value);
    }

    @Test
    void test1_1() {
        PropositionInterpreter propinterp = new PropositionInterpreter("P", true);
        System.out.println("P = " + propinterp.truth_value);
        assertTrue(propinterp.truth_value);
    }

    @Test
    void test2() {
        PropositionInterpreter propinterp = new PropositionInterpreter("!P | Q", true);
        assertTrue(propinterp.truth_value);
    }

    @Test
    void test3() {
        PropositionInterpreter propinterp = new PropositionInterpreter("!(P | Q) & (!P & !Q)", true);
        assertFalse(propinterp.truth_value);
    }

    @Test
    void test4() {
        PropositionInterpreter propinterp = new PropositionInterpreter("a & b | c & d | e & f", true);
        assertTrue(propinterp.truth_value);
    }

    @Test
    void test5() {
        PropositionInterpreter propinterp = new PropositionInterpreter("!a -> b | c <> d | e & f", true);
        assertTrue(propinterp.truth_value);
    }

    @Test
    void test6() {
        PropositionInterpreter propinterp = new PropositionInterpreter("A -> B -> C -> D -> E -> F -> G -> H -> I -> J", true);
        assertTrue(propinterp.truth_value);
    }

    @Test
    void test7() {
        PropositionInterpreter propinterp = new PropositionInterpreter("!!!!Dingus", true);
        assertTrue(propinterp.truth_value);
    }

    // Complex Proposition Tests
    @Test
    void test8() {
        ByteArrayInputStream input = new ByteArrayInputStream("f\nt\n".getBytes());
        PropositionInterpreter propinterp = new PropositionInterpreter("!P & !Q", false, input, System.out);
        assertFalse(propinterp.truth_value);
    }

    @Test
    void test9() {
        ByteArrayInputStream input = new ByteArrayInputStream("f\nf\n".getBytes());
        PropositionInterpreter propinterp = new PropositionInterpreter("!P & !Q", false, input, System.out);
        assertTrue(propinterp.truth_value);
    }

    @Test
    void test10() {
        ByteArrayInputStream input = new ByteArrayInputStream("false\nfalse\n".getBytes());
        PropositionInterpreter propinterp = new PropositionInterpreter("!P & !Q", false, input, System.out);
        assertTrue(propinterp.truth_value);
    }


}
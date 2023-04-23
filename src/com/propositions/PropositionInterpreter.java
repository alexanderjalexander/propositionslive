package com.propositions;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

public class PropositionInterpreter {

    public boolean truth_value;

    public HashMap<String, Boolean> truthmaps;

    public PropositionParser parse;

    public static class InterpError extends RuntimeException {
        InterpError(String message) {super(message); }
    }

    private static InterpError error(PropositionTokenizer.Token token, String message) {
        String errorMessage = "Error during Proposition Interpretation of [" + token.toString() + "]: " + message;
        return new InterpError(errorMessage);
    }

    public PropositionInterpreter(String proposition, boolean simple) {
        ArrayList<PropositionTokenizer.Token> tokenized = PropositionTokenizer.PropositionTokenize(proposition);
        parse = new PropositionParser(tokenized);
        if (simple) {
            // If Simple Evaluation, Assume All Values Are True
            truth_value = simple_evaluator(parse.parsed);
        } else {
            // If Complex Evaluation, Assume All Values Have Diff Types
            // Create a new HashMap to store truthmaps, initially put everything as False
            truthmaps = complex_prop_hashmap(parse.parsed);

            Scanner reader = new Scanner(System.in);
            for (String i : truthmaps.keySet()) {
                System.out.print("Please input the desired truth-value(\"true\", \"t\", \"false\", \"f\") for the proposition " + i + ": ");
                String tv = reader.nextLine();
                while (true) {
                    if ((tv.compareToIgnoreCase("t") == 0) || (tv.compareToIgnoreCase("true") == 0)) {
                        truthmaps.replace(i, true);
                        break;
                    } else if ((tv.compareToIgnoreCase("f") == 0) || (tv.compareToIgnoreCase("false") == 0)) {
                        truthmaps.replace(i, false);
                        break;
                    } else {
                        System.out.println("\"" + tv + "\" is not a valid input. Please try again.");
                    }
                }
            }

            // Evaluate using the newly made truthmaps
            truth_value = complex_evaluator(parse.parsed, truthmaps);
        }
    }

    public PropositionInterpreter(String proposition, boolean simple, boolean input) {
        ArrayList<PropositionTokenizer.Token> tokenized = PropositionTokenizer.PropositionTokenize(proposition);
        parse = new PropositionParser(tokenized);
        if (simple) {
            // If Simple Evaluation, Assume All Values Are True
            truth_value = simple_evaluator(parse.parsed);
        } else {
            // If Complex Evaluation, Assume All Values Have Diff Types
            // Create a new HashMap to store truthmaps, initially put everything as False
            truthmaps = complex_prop_hashmap(parse.parsed);

            // If input == true, ask user thru stdin to enter the respective truth values
            if (input) {
                Scanner reader = new Scanner(System.in);
                for (String i : truthmaps.keySet()) {
                    System.out.print("Please input the desired truth-value(\"true\", \"t\", \"false\", \"f\") for the proposition " + i + ": ");
                    String tv = reader.nextLine();
                    while (true) {
                        if ((tv.compareToIgnoreCase("t") == 0) || (tv.compareToIgnoreCase("true") == 0)) {
                            truthmaps.replace(i, true);
                            break;
                        } else if ((tv.compareToIgnoreCase("f") == 0) || (tv.compareToIgnoreCase("false") == 0)) {
                            truthmaps.replace(i, false);
                            break;
                        } else {
                            System.out.println("\"" + tv + "\" is not a valid input. Please try again.");
                        }
                    }
                }
            }

            // Evaluate using the newly made truthmaps
            truth_value = complex_evaluator(parse.parsed, truthmaps);
        }
    }

    // Alternate PropositionInterpreter with specified InputStream and PrintStream
    public PropositionInterpreter(String proposition, boolean simple, InputStream in, PrintStream out) {
        ArrayList<PropositionTokenizer.Token> tokenized = PropositionTokenizer.PropositionTokenize(proposition);
        PropositionParser parse = new PropositionParser(tokenized);
        if (simple) {
            // If Simple Evaluation, Assume All Values Are True
            truth_value = simple_evaluator(parse.parsed);
        } else {
            // If Complex Evaluation, Assume All Values Have Diff Types
            // Create a new HashMap to store truthmaps, initially put everything as False
            truthmaps = complex_prop_hashmap(parse.parsed);
            Scanner reader = new Scanner(in);
            for (String i : truthmaps.keySet()) {
                out.print("Please input the desired truth-value(\"true\", \"t\", \"false\", \"f\") for the proposition " + i + ": ");
                String tv = reader.nextLine();
                while (true) {
                    if ((tv.compareToIgnoreCase("t") == 0) || (tv.compareToIgnoreCase("true") == 0)) {
                        truthmaps.replace(i, true);
                        break;
                    } else if ((tv.compareToIgnoreCase("f") == 0) || (tv.compareToIgnoreCase("false") == 0)) {
                        truthmaps.replace(i, false);
                        break;
                    } else {
                        out.println("\"" + tv + "\" is not a valid input. Please try again.");
                    }
                }
            }
            // Evaluate using the newly made truthmaps
            truth_value = complex_evaluator(parse.parsed, truthmaps);
        }
    }

    // Recursive In-Order Evaluation Function, Returns Boolean Value
    public static boolean simple_evaluator(Expr root) {
        // Evaluate according to an in-order traversal
        // Evaluates based on NAME tokens' truth values
        if (root instanceof Expr.BinaryOperation) {
            // Either an AND, OR, IMPLIES, or IFF
            switch(((Expr.BinaryOperation)root).operation.type) {
                case AND : {
                    boolean left = simple_evaluator(((Expr.BinaryOperation)root).left);
                    boolean right = simple_evaluator(((Expr.BinaryOperation)root).right);
                    return Propositions.and(left, right);
                }
                case OR : {
                    boolean left = simple_evaluator(((Expr.BinaryOperation)root).left);
                    boolean right = simple_evaluator(((Expr.BinaryOperation)root).right);
                    return Propositions.or(left, right);
                }
                case IMPLIES : {
                    boolean left = simple_evaluator(((Expr.BinaryOperation)root).left);
                    boolean right = simple_evaluator(((Expr.BinaryOperation)root).right);
                    return Propositions.implies(left, right);
                }
                case IF_AND_ONLY_IF : {
                    boolean left = simple_evaluator(((Expr.BinaryOperation)root).left);
                    boolean right = simple_evaluator(((Expr.BinaryOperation)root).right);
                    return Propositions.ifAndOnlyIf(left, right);
                }
                default : throw error(((Expr.BinaryOperation)root).operation, "Binary Operation Expression has invalid Operation Token");
            }
        } else if (root instanceof Expr.Unary) {
            // UNARY Operations
            return Propositions.not( simple_evaluator( ((Expr.Unary)root).expression ) );
        } else if (root instanceof Expr.Grouping) {
            // Groupings
            return simple_evaluator( ((Expr.Grouping)root).expression );
        } else {
            // Literals, just use truth values of the actual node
            //return ((Literal) root).name.truth_value;
            return true;
        }
    }

    // Recursive Method that returns a hashmap containing all of the propositions with the
    public static HashMap<String, Boolean> complex_prop_hashmap(Expr root) {
        // Initialize Hashmap [truthmaps]
        HashMap<String, Boolean> truthmap = new HashMap<>();

        // Call on recursive helper method to update truthmaps
        // Initialize all values to false
        complex_prop_hashmap_helper(root, truthmap);

        // Return [truthmap]
        return truthmap;
    }


    private static void complex_prop_hashmap_helper(Expr root, HashMap<String, Boolean> truthm) {
        // Traverse the tree left and right,
        if (root instanceof Expr.BinaryOperation) {
            // Go left and right
            complex_prop_hashmap_helper( ((Expr.BinaryOperation)root).left, truthm );
            complex_prop_hashmap_helper( ((Expr.BinaryOperation)root).right, truthm );
        } else if (root instanceof Expr.Unary) {
            // UNARY Operations, go into expression
            complex_prop_hashmap_helper( ((Expr.Unary)root).expression, truthm );
        } else if (root instanceof Expr.Grouping) {
            // Groupings
            complex_prop_hashmap_helper( ((Expr.Grouping)root).expression, truthm );
        } else {
            // Literals, just use truth values of the actual node
            truthm.putIfAbsent(((Expr.Literal) root).name.toString(), false);
        }
    }

    public void complex_eval() {
        truth_value = complex_evaluator(parse.parsed, truthmaps);
    }

    // Recursive In-Order Proposition Searcher
    // Searches for all given proposition with truthmaps to determine whether a boolean has a truth or not
    public static boolean complex_evaluator(Expr root, HashMap<String, Boolean> truthm) {
        // Evaluate according to an in-order traversal
        // Evaluates based on NAME tokens' truth values
        if (root instanceof Expr.BinaryOperation) {
            // Either an AND, OR, IMPLIES, or IFF
            switch(((Expr.BinaryOperation)root).operation.type) {
                case AND : {
                    boolean left = complex_evaluator(((Expr.BinaryOperation)root).left, truthm );
                    boolean right = complex_evaluator(((Expr.BinaryOperation)root).right, truthm );
                    return Propositions.and(left, right);
                }
                case OR : {
                    boolean left = complex_evaluator(((Expr.BinaryOperation)root).left, truthm );
                    boolean right = complex_evaluator(((Expr.BinaryOperation)root).right, truthm );
                    return Propositions.or(left, right);
                }
                case IMPLIES : {
                    boolean left = complex_evaluator(((Expr.BinaryOperation)root).left, truthm );
                    boolean right = complex_evaluator(((Expr.BinaryOperation)root).right, truthm );
                    return Propositions.implies(left, right);
                }
                case IF_AND_ONLY_IF : {
                    boolean left = complex_evaluator(((Expr.BinaryOperation)root).left, truthm );
                    boolean right = complex_evaluator(((Expr.BinaryOperation)root).right, truthm );
                    return Propositions.ifAndOnlyIf(left, right);
                }
                default : throw error(((Expr.BinaryOperation)root).operation, "Binary Operation Expression has invalid Operation Token");
            }
        } else if (root instanceof Expr.Unary) {
            // UNARY Operations
            return Propositions.not( complex_evaluator( ((Expr.Unary)root).expression, truthm ) );
        } else if (root instanceof Expr.Grouping) {
            // Groupings
            return complex_evaluator( ((Expr.Grouping)root).expression, truthm );
        } else {
            // Literals, just use truth values of the actual node
            //return ((Literal) root).name.truth_value;
            if (truthm.get( ((Expr.Literal) root).name.toString() ) != null) {
                return truthm.get( ((Expr.Literal) root).name.toString() );
            } else {
                throw error(((Expr.Literal) root).name, "No Truth-Value provided for the proposition.");
            }
        }
    }
}

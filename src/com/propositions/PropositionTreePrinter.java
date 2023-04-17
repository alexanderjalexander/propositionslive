package com.propositions;

public class PropositionTreePrinter {
	public static String TreeStringBuilder(Expr root) {
		String result = "";
		if (root instanceof Expr.BinaryOperation) {
			result += "( " + (((Expr.BinaryOperation) root).operation).toString()
					+ " " + TreeStringBuilder(((Expr.BinaryOperation) root).left)
					+ " " + TreeStringBuilder(((Expr.BinaryOperation) root).right) + " )";
		} else if (root instanceof Expr.Unary) {
			result += "( !" + TreeStringBuilder(((Expr.Unary) root).expression) + " )";
		} else if (root instanceof Expr.Grouping) {
			result += "[ " + TreeStringBuilder(((Expr.Grouping) root).expression) + " ]";
		} else if (root instanceof Expr.Literal) {
			result += (((Expr.Literal) root).name.toString());
		}
		return result;
	}
}

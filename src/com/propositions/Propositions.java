package com.propositions;

import java.util.ArrayList;

public class Propositions {
	
	public static boolean and(boolean bool1, boolean bool2) {
		return bool1 && bool2;
	}
	
	public static boolean or(boolean bool1, boolean bool2) {
		return bool1 || bool2;
	}
	
	public static boolean not(boolean bool) {
		return !bool;
	}
	
	public static boolean implies(boolean bool1, boolean bool2) {
		return (!bool1 || bool2);
	}
	
	public static boolean ifAndOnlyIf(boolean bool1, boolean bool2) {
		if (and(bool1,bool2)) {
			return true;
		} else if (and(not(bool1),not(bool2))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static ArrayList<Character> propositionParser(String prop) {
		if (prop.length() == 0) {
			throw new IllegalArgumentException("Cannot pass empty string to propositionParser");
		}
		
		for (int i = 0; i < prop.length(); i++) {
			if (!(Character.isLetter(prop.charAt(i))) 
					&& !(Character.isLetter(prop.charAt(0)) || (prop.charAt(0) == '!')
							)) {
				throw new IllegalArgumentException("Single propositions may only contain alphabetical letters or !.");
			}
		}
		
		ArrayList<Character> parsedProp = new ArrayList<Character>();
		for (int i = 0; i < prop.length(); i++) {
			parsedProp.add(i, prop.charAt(i));
		}
		return parsedProp;
	}
	
	/*
	 * Given any multiletter proposition which follows this format:
	 * "P", "p", "!P", "!p", "BO", "!BO", etc.
	 * simpleProposition will just simply evaluate each to True or False.
	 * This is assuming, like in the Tree Method, that all methods are True
	 * E.g. "!P" = False. 
	 */
	public static boolean simplePropositionEvaluate(String prop) {
		ArrayList<Character> parsedProposition = propositionParser(prop);
		return (parsedProposition.get(0) != '!');
		
		
	}
	
	
}

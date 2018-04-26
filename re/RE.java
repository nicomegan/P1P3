package re;

import java.util.Stack;

import fa.nfa.NFA;
import fa.nfa.NFAState;

public class RE implements REInterface {

	private static NFA blank;
	private String regEx;
	private NFA nfa;
	private Stack<NFAState> starts = new Stack<NFAState>();
	private Stack<NFAState> ends = new Stack<NFAState>();
	private int numStates = 0;

	public RE(String regEx) {
		nfa = new NFA();
		this.regEx = regEx;
	}

	// will do parse
	@Override
	public NFA getNFA() {
		NFAState startState = new NFAState("s");
		nfa.addStartState("s");
		nfa.addState("s");
		starts.push(startState);
		NFA returnnfa = buildNFA(startState);
		return returnnfa;
	}

	private NFA buildNFA(NFAState start) {
		NFAState next = null;
		while (more()) {
			String nextChar = next();
			if (nextChar.equals("(")) {
				if(starts.peek()!=(start)) {
					starts.push(start);
				}
				buildNFA(start);
			} else if (nextChar.equals(")")) {

			} else if (nextChar.equals("*")) {
				
			} else if (nextChar.equals("|")) {
				next = new NFAState(""+numStates);
				nfa.addState(""+numStates);
				nfa.addTransition(start.getName(),'e', next.getName());
				numStates++;
				start=starts.peek();
				ends.push(next);
			} else {
				next = new NFAState(""+numStates);
				nfa.addState(next.getName());
				nfa.addTransition(start.getName(), nextChar.charAt(0), next.getName());
				start = next;
				numStates++;
			}
			if(!more()) {
				nfa.addFinalState(start.getName());
			}
		}
		while (!ends.isEmpty()) {
			nfa.addFinalState(ends.pop().getName());
		}
		return nfa;
	}

	// /* TODO Decent parser!!!
	// REgular expression term types
	// Specifies if loop,mult options,etc??? */
	//
	// /**
	// * gets the next regular expression
	// *
	// * @return - term/regular expression
	// */
	// private NFA re() {
	// NFA termNFA = term();
	// if (more() && peek() == '|') {
	// eat('|');
	// NFA regex = re();
	// return new Choice(termNFA, regex);
	// } else {
	// return termNFA;
	// }
	// }
	//
	// /**
	// * gets the term/factor
	// *
	// * @return - term/factor
	// */
	// private NFA term() {
	// NFA termNFA = RE.blank;
	// while (more() && peek() != ')' && peek() != '|') {
	// NFA nextFactor = factor();
	// termNFA = new Sequence(termNFA, nextFactor);
	// }
	//
	// return termNFA;
	// }
	//
	// /**
	// * gets the next factor
	// *
	// * @return
	// */
	// private NFA factor() {
	// NFA baseNFA = base();
	//
	// while (more() && peek() == '*') {
	// eat('*');
	// baseNFA = new Repetition(base);
	// }
	//
	// return baseNFA;
	// }
	//
	// /**
	// * returns a base
	// * @return - base
	// */
	// private NFA base() {
	// switch (peek()) {
	// case '(':
	// eat('(');
	// NFA r = re();
	// eat(')');
	// return r;
	//
	// default:
	// return new Primitive(next());
	// }
	// }

	/**
	 * looks at next char
	 * 
	 * @return
	 */
	private String peek() {
		return regEx.substring(0, 1);
	}

	/**
	 * remove next char
	 * 
	 * @param c
	 */
	private void eat(String c) {
		if (peek().equals(c))
			this.regEx = this.regEx.substring(1);
		else
			throw new RuntimeException("Expected: " + c + "; got: " + peek());
	}

	/**
	 * gets and removes next char
	 * 
	 * @return
	 */
	private String next() {
		String c = peek();
		eat(c);
		return c;
	}

	/**
	 * still more stuff check
	 * 
	 * @return
	 */
	private boolean more() {
		return regEx.length() > 0;
	}
}

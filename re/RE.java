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
	private NFAState startState;

	public RE(String regEx) {
		nfa = new NFA();
		this.regEx = regEx;
	}

	// will do parse
	@Override
	public NFA getNFA() {
		NFAState start = new NFAState("s");
		nfa.addState("s");
		starts.push(start);
		startState = start;
		nfa.addStartState(start.getName());
		NFA returnnfa = buildNFA(start, regEx);
		return returnnfa;
	}

	private NFA buildNFA(NFAState start, String re) {
		NFAState nextState = null;
		Stack trackrest = new Stack();
		while (re.length() > 0) {
			String nextChar = re.substring(0,1);
			if (nextChar.equals("(")) {
				re = re.substring(1);
				if (starts.peek() != (start)) {
					starts.push(start);
				}
				buildNFA(start, re);
			} else if (nextChar.equals(")")) {
				re=re.substring(1);
				start = ends.peek();
			} else if (nextChar.equals("*")) {
				while (!ends.isEmpty()) {
					nfa.addTransition(ends.peek().getName(), 'e', starts.peek().getName());
					re=re.substring(1);
					buildNFA(ends.pop(), re);
				}
				start = starts.peek();
			} else if (nextChar.equals("|")) {
				re=re.substring(1);
				NFAState prev = new NFAState("" + numStates);
				numStates++;
				nfa.addState(prev.getName());
				nfa.addTransition(prev.getName(), 'e', startState.getName());

				nextState = new NFAState("" + numStates);
				nfa.addState(nextState.getName());
				nfa.addTransition(prev.getName(), 'e', nextState.getName());

				startState = prev;
				numStates++;
				starts.push(prev);
				if(!ends.contains(start)){
					ends.push(start);
				}
				start =nextState;
			} else {
				re=re.substring(1);
				nextState = new NFAState("" + numStates);
				nfa.addState(nextState.getName());
				nfa.addTransition(start.getName(), nextChar.charAt(0), nextState.getName());
				start = nextState;
				numStates++;
				if ((re.length()>0) && re.substring(0,1).equals("*") | re.substring(0,1).equals("|") | re.substring(0,1).equals(")")) {
					ends.push(nextState);
				}
			}
			if (re.length() <=0) {
				nfa.addFinalState(start.getName());
			}
		}
		Stack<NFAState> tmp = new Stack<NFAState>();
//		while(!ends.isEmpty()){
//			tmp.push(ends.pop());
//		}
//		while (!tmp.isEmpty()) {
//			nfa.addFinalState(tmp.peek().getName());
//			ends.push(tmp.pop());
//		}
		nfa.addStartState(startState.getName());
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

//	/**
//	 * looks at next char
//	 * 
//	 * @return
//	 */
//	private String peek() {
//		return regEx.substring(0, 1);
//	}
//
//	/**
//	 * remove next char
//	 * 
//	 * @param c
//	 */
//	private void eat(String c) {
//		if (peek().equals(c))
//			this.regEx = this.regEx.substring(1);
//		else
//			throw new RuntimeException("Expected: " + c + "; got: " + peek());
//	}
//
//	/**
//	 * gets and removes next char
//	 * 
//	 * @return
//	 */
//	private String next() {
//		String c = peek();
//		eat(c);
//		return c;
//	}
//
//	/**
//	 * still more stuff check
//	 * 
//	 * @return
//	 */
//	private boolean more() {
//		return regEx.length() > 0;
//	}
}

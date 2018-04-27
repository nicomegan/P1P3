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
		int inparenth = 0;
		NFAState nextState = null;
		while (re.length() > 0) {
			String nextChar = re.substring(0, 1);
			if (nextChar.equals("(")) {
				inparenth++;
				re = re.substring(1);
				if (starts.peek() != (start)) {
					starts.push(start);
				}
				continue;
			} else if (nextChar.equals(")")) {
				inparenth++;
				re = re.substring(1);
				// ends.push(start);
				if (!ends.isEmpty()) {
					start = ends.peek();
				}
			} else if (nextChar.equals("*")) {
				nextState = ends.pop();
				nfa.addTransition(nextState.getName(), 'e', starts.peek().getName());
				re = re.substring(1);
				start = starts.peek();
			} else if (nextChar.equals("|")) {
				NFAState combine = new NFAState("" + numStates);
				nfa.addState(combine.getName());
				nfa.addTransition(nextState.getName(), 'e', combine.getName());
				numStates++;

				re = re.substring(1);
				if (inparenth <= 3) {
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
					start = nextState;
				} else {
					start = starts.peek();
					while (!starts.isEmpty()) {
						starts.pop();
					}
					while (!ends.isEmpty()) {
						ends.pop();
					}
					starts.push(startState);
				}
				ends.push(combine);
			} else {
				re = re.substring(1);
				nextState = new NFAState("" + numStates);
				nfa.addState(nextState.getName());
				nfa.addTransition(start.getName(), nextChar.charAt(0), nextState.getName());
				start = nextState;
				numStates++;
				if ((re.length() > 0) && re.substring(0, 1).equals("*") | re.substring(0, 1).equals("|")) {
					ends.push(nextState);
				} else if (re.length() > 0 && re.substring(0, 1).equals(")") && !ends.isEmpty()) {
					nfa.addTransition(nextState.getName(), 'e', ends.peek().getName());
				}
				if (inparenth == 0 && re.length() > 0 && !re.substring(0, 1).equals("*")) {
					starts.push(nextState);
				}
			}
			if (re.length() <= 0) {
				nfa.addFinalState(start.getName());
			}
		}
		if (!ends.isEmpty()) {
			nfa.addFinalState(ends.pop().getName());

		}
		Stack<NFAState> tmp = new Stack<NFAState>();
		nfa.addStartState(startState.getName());
		return nfa;
	}
}

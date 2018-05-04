
package re;

import java.util.Stack;

import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;

/**
 * @author Anne Brinegar, Megan Pierce This class reads in a Regular Expression,
 *         and converts it to an NFA
 */
public class RE implements REInterface {

	private static NFA blank;
	private String regEx;
	private NFA nfa;
	private Stack<NFAState> starts = new Stack<NFAState>();
	private Stack<NFAState> ends = new Stack<NFAState>();
	private int numStates = 1;
	private NFAState startState;

	/**
	 * Constructor, initializes NFA
	 * 
	 * @param regEx
	 */
	public RE(String regEx) {
		nfa = new NFA();
		this.regEx = regEx;
	}

	/**
	 * Creates NFA
	 **/
	@Override
	public NFA getNFA() {
		NFAState start = new NFAState("s");
		// nfa.addState("s");
		starts.push(start);
		startState = start;
		nfa.addStartState(start.getName());
		NFA returnnfa = buildNFA(start, regEx, nfa);
		returnnfa.addFinalState(finalState.getName());

		System.out.println(returnnfa.getFinalStates());
		System.out.println(returnnfa);

		return returnnfa;
	}

	int i = 0;
	Stack comboStack = new Stack(); 
	NFAState start = new NFAState("" + (++i));
	NFAState finalState = start;
	boolean inor = false;

	private NFA buildNFA(NFAState start1, String regEx2, NFA newNFA) {
		NFAState miniStartState = start;
		NFAState gobackto = start;
		int lastindex = 0;
		while (regEx2.length() > 0) {
			String re = regEx2.substring(0, 1);
			if (re.equals("(")) {
				regEx2 = regEx2.substring(1);

				// i++;
				String reg = "";
				int numP = 1;
				while (regEx2.length() > 0) {
					if (regEx2.charAt(0) == '(') {
						numP++;
					}
					if (regEx2.charAt(0) == ')') {
						numP--;
						if (numP == 0) {
							break;
						}
					}
					reg += regEx2.substring(0, 1);
					regEx2 = regEx2.substring(1);
				}
				if (regEx2.length() > 0) {
					reg += regEx2.substring(0, 1);
					regEx2 = regEx2.substring(1);
				}
				NFAState newState = new NFAState("" + (++i));
				newNFA.addTransition(start.getName(), 'e', newState.getName());
				start = newState;
				newNFA = buildNFA(start, reg, newNFA);
				gobackto=newState;
				if (regEx2.length() > 1 && regEx2.substring(1, 2).equals("*")) {
					regEx2 = regEx2.substring(1);

					newNFA.addTransition(start.getName(), 'e', gobackto.getName());
					newNFA.addTransition(gobackto.getName(), 'e', start.getName());

				}
				
			} else if (re.equals(")")) {
				return newNFA;
			} else if (re.equals("|")) { 
				regEx2 = regEx2.substring(1);
				NFAState combine = new NFAState("" + (++i));
				NFAState newState = new NFAState("" + (++i));

				// add needed States
				newNFA.addState(combine.getName());
				newNFA.addState(newState.getName());

				// add new transitions
				newNFA.addTransition(start.getName(), 'e', combine.getName());
				newNFA.addTransition(miniStartState.getName(), 'e', newState.getName());

				comboStack.push(combine);
				start = newState;
				NFAState s = start;
				buildNFA(start, regEx2, newNFA);
				if (!comboStack.isEmpty()) {
					newNFA.addTransition(start.getName(), 'e', ((State) comboStack.peek()).getName());
					start = (NFAState) comboStack.pop();
				}
				regEx2="";
			} else if (re.equals("*")) {
				regEx2 = regEx2.substring(1);

				newNFA.addTransition(start.getName(), 'e', gobackto.getName());
				newNFA.addTransition(gobackto.getName(), 'e', start.getName());

			} else {
				regEx2 = regEx2.substring(1);
				char nextchar = 0;
				if (regEx2.length() > 0) {
					nextchar = regEx2.charAt(0);
				}
				NFAState newState = new NFAState("" + (++i));
				newNFA.addTransition(start.getName(), re.charAt(0), newState.getName());
				if (nextchar == '*') {
					gobackto = start;
				}
				start = newState;
//				if (nextchar != 'a' && nextchar != 'b' && nextchar != 'e' && nextchar !='*') {
//					if (!comboStack.isEmpty()) {
//						newNFA.addTransition(start.getName(), 'e', ((State) comboStack.peek()).getName());
//						start = (NFAState) comboStack.pop();
//					}
//
//				}
			}
		}

		// newNFA.addFinalState(start.getName());
		finalState = start;
		newNFA.addStartState(miniStartState.getName());
		return newNFA;
	}

	/**
	 * Parses regular expression to build NFA
	 * 
	 * @param regEx
	 */
	private NFA buildNFA(NFAState start, String re) {
		NFAState previousState = start;
		int inparenth = 0;
		NFAState nextState = null;
		boolean selfLoop = false;
		while (re.length() > 0) {
			String nextChar = re.substring(0, 1);
			if (nextChar.equals("(")) { // checks for '('
				inparenth++;
				re = re.substring(1);
				if (starts.peek() != (start)) { // pushes new start state to start stack
					starts.push(start);
				}
				continue; // loops back to outer loop to check for another '('
			} else if (nextChar.equals(")")) { // checks for ')'
				if (starts.size() > 1 && inparenth % 2 == 0) {
					starts.pop();
				}
				inparenth++;
				re = re.substring(1);
				ends.push(start);
				if (!ends.isEmpty()) { // if we have already added an end to list, start is
					// the last place we ended
					start = ends.peek();
				}
			} else if (nextChar.equals("*")) { // checks for '*'
				nextState = ends.pop();
				if (inparenth % 2 == 0 && !selfLoop) {
					nfa.addTransition(nextState.getName(), 'e', starts.peek().getName());
					start = starts.peek();
				} else if (selfLoop) {
					nfa.addTransition(nextState.getName(), 'e', previousState.getName());
					start = previousState;
				} else {
					nfa.addTransition(nextState.getName(), 'e', starts.peek().getName()); // adds
					// empty transition to
					previousState = start;
					start = starts.peek();
					// the
				} // last starting point
				selfLoop = false;
				re = re.substring(1);
			} else if (nextChar.equals("|")) { // checks for '|'
				NFAState combine = new NFAState("" + numStates);
				nfa.addState(combine.getName());
				nfa.addTransition(nextState.getName(), 'e', combine.getName()); // adds empty
				// transition from previous
				// state to new state, so both arguments
				// between '|' branch from same state
				numStates++;

				re = re.substring(1);
				if (starts.peek().equals(startState)) { // making sure its more than one set

					NFAState prev = new NFAState("" + numStates);
					numStates++;
					nfa.addState(prev.getName());
					nfa.addTransition(prev.getName(), 'e', starts.peek().getName());

					nextState = new NFAState("" + numStates);
					nfa.addState(nextState.getName());
					nfa.addTransition(prev.getName(), 'e', nextState.getName());
					startState = prev;
					numStates++;
					starts.pop();
					starts.push(prev);
					previousState = start;
					start = nextState;
				}
				start = starts.peek();
				ends.push(combine); // pushes branching state onto ends stack
			} else { // makes transition to new state on next char in RegEx
				re = re.substring(1);
				nextState = new NFAState("" + numStates);
				nfa.addState(nextState.getName());
				nfa.addTransition(start.getName(), nextChar.charAt(0), nextState.getName());
				previousState = start;
				start = nextState;
				numStates++;
				if ((re.length() > 0) && re.substring(0, 1).equals("*") | re.substring(0, 1).equals("|")) { // looks
					// ahead to
					// next
					// character
					// after
					// symbol
					ends.push(nextState);
				} else if (re.length() > 0 && re.substring(0, 1).equals(")") && !ends.isEmpty()) {
					nfa.addTransition(nextState.getName(), 'e', ends.peek().getName());
					start = ends.pop();
				}
				if (re.length() > 0 && re.substring(0, 1).equals("*")) {
					selfLoop = true;
				}
			}
			if (re.length() <= 0) { // adds final state when RegEx is done being parsed
				nfa.addFinalState(start.getName());
			}
		} // end while
		if (!ends.isEmpty()) {
			nfa.addFinalState(ends.pop().getName());

		}

		Stack<NFAState> tmp = new Stack<NFAState>();
		nfa.addStartState(startState.getName());
		return nfa;
	}

}
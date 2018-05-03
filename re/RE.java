
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
		nfa.addState("s");
		starts.push(start);
		startState = start;
		nfa.addStartState(start.getName());
		NFA returnnfa = buildNFA(start, regEx);
		System.out.println(returnnfa.getFinalStates());
		System.out.println(returnnfa);

		return returnnfa;
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
				inparenth--;
				re = re.substring(1);
				ends.push(start);
				if (!ends.isEmpty()) { // if we have already added an end to list, start is the last place we ended
					start = ends.peek();
				}
				if(starts.size()>1) {
				starts.pop();
				}
			} else if (nextChar.equals("*")) { // checks for '*'
				nextState = ends.pop();
				if (inparenth == 0 && !selfLoop) {
					nfa.addTransition(nextState.getName(), 'e', startState.getName());
					start = startState;
				}else if (selfLoop) {
					nfa.addTransition(nextState.getName(), 'e', previousState.getName()); 
					start=previousState;
				} else {
					nfa.addTransition(nextState.getName(), 'e', starts.peek().getName()); // adds empty transition to
					previousState=start;
					start = starts.peek();
					// the
				} // last starting point
				selfLoop = false;
				re = re.substring(1);
			} else if (nextChar.equals("|")) { // checks for '|'
				NFAState combine = new NFAState("" + numStates);
				nfa.addState(combine.getName());
				nfa.addTransition(nextState.getName(), 'e', combine.getName()); // adds empty transition from previous
																				// state to new state, so both arguments
																				// between '|' branch from same state
				numStates++;

				re = re.substring(1);
				if (starts.peek().equals(startState)) { // making sure its more than one set ()
					NFAState prev = new NFAState("" + numStates);
					numStates++;
					nfa.addState(prev.getName());
					nfa.addTransition(prev.getName(), 'e', starts.peek().getName());

					nextState = new NFAState("" + numStates);
					nfa.addState(nextState.getName());
					nfa.addTransition(prev.getName(), 'e', nextState.getName());
					startState = prev;
					numStates++;
					starts.push(prev);
					previousState=start;
					start = nextState;
				}
				start = starts.peek();
				ends.push(combine); // pushes branching state onto ends stack
			} else { // makes transition to new state on next char in RegEx
				re = re.substring(1);
				nextState = new NFAState("" + numStates);
				nfa.addState(nextState.getName());
				nfa.addTransition(start.getName(), nextChar.charAt(0), nextState.getName());
				previousState=start;
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
				if (re.length() > 0 && (!re.substring(0, 1).equals("*") && !re.substring(0, 1).equals(")")
						&& !re.substring(0, 1).equals("|"))) {
					starts.push(nextState);
				}
				if (re.length() > 0 && re.substring(0, 1).equals("*")) {
					selfLoop = true;
					starts.pop();
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
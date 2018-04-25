package re;

import fa.nfa.NFA;

public class RE implements REInterface {

	private static NFA blank;
	private String regEx;
	private NFA nfa;

	public RE(String regEx) {
		this.regEx = regEx;
	}

	//will do parse
	@Override
	public NFA getNFA() {
		return nfa;
	}

	/* TODO Decent parser!!!
	REgular expression term types 
	Specifies if loop,mult options,etc??? */

	/**
	 * gets the next regular expression
	 * 
	 * @return - term/regular expression
	 */
	private NFA re() {
		NFA termNFA = term();

		if (more() && peek() == '|') {
			eat('|');
			NFA regex = re();
			return new Choice(termNFA, regex);
		} else {
			return termNFA;
		}
	}

	/**
	 * gets the term/factor
	 * 
	 * @return - term/factor
	 */
	private NFA term() {
		NFA termNFA = RE.blank;

		while (more() && peek() != ')' && peek() != '|') {
			NFA nextFactor = factor();
//			term = new Sequence(term, nextFactor);
		}

		return termNFA;
	}

	/**
	 * gets the next factor
	 * 
	 * @return
	 */
	private NFA factor() {
		NFA baseNFA = base();

		while (more() && peek() == '*') {
			eat('*');
//			base = new Repetition(base);
		}

		return baseNFA;
	}

	/**
	 * returns a base
	 * @return - base 
	 */
	private NFA base() {
		switch (peek()) {
		case '(':
			eat('(');
			NFA r = re();
			eat(')');
			return r;

		case '\\':
			eat('\\');
			char esc = next();
			return new Primitive(esc);

		default:
			return new Primitive(next());
		}
	}

	/**
	 * looks at next char
	 * @return
	 */
	private char peek() {
		return regEx.charAt(0);
	}

	/**
	 * remove next char
	 * @param c
	 */
	private void eat(char c) {
		if (peek() == c)
			this.regEx = this.regEx.substring(1);
		else
			throw new RuntimeException("Expected: " + c + "; got: " + peek());
	}

	/**
	 * gets and removes next char
	 * @return
	 */
	private char next() {
		char c = peek();
		eat(c);
		return c;
	}

	/**
	 * still more stuff check
	 * @return
	 */
	private boolean more() {
		return regEx.length() > 0;
	}
}

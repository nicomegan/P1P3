package re;

import fa.nfa.NFA;

public class RE implements REInterface {

	private static Blank blank;
	private String regEx;

	public RE(String regEx) {
		this.regEx = regEx;
	}

	//will do parse
	@Override
	public NFA getNFA() {
		// TODO Auto-generated method stub
		return null;
	}

	/* TODO Decent parser!!!
	REgular expression term types 
	Specifies if loop,mult options,etc??? */

	/**
	 * gets the next regular expression
	 * 
	 * @return - term/regular expression
	 */
	private RE re() {
		RE term = term();

		if (more() && peek() == '|') {
			eat('|');
			RE regex = re();
			return new Choice(term, regex);
		} else {
			return term;
		}
	}

	/**
	 * gets the term/factor
	 * 
	 * @return - term/factor
	 */
	private RE term() {
		RE term = RE.blank;

		while (more() && peek() != ')' && peek() != '|') {
			RE nextFactor = factor();
			term = new Sequence(term, nextFactor);
		}

		return term;
	}

	/**
	 * gets the next factor
	 * 
	 * @return
	 */
	private RE factor() {
		RE base = base();

		while (more() && peek() == '*') {
			eat('*');
			base = new Repetition(base);
		}

		return base;
	}

	/**
	 * returns a base
	 * @return - base 
	 */
	private RE base() {
		switch (peek()) {
		case '(':
			eat('(');
			RE r = re();
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


	/**
	 * private inner class.
	 * there is a choice indicated by |
	 * 
	 * @author annebrinegar, Megan Pierce
	 *
	 */
	private class Choice extends RE {
		private RE thisOne;
		private RE thatOne;

		public Choice(RE thisOne, RE thatOne) {
			super(regEx);
			this.thisOne = thisOne;
			this.thatOne = thatOne;
		}
	}

	/**
	 * private inner class.
	 * Sequence of characters
	 * 
	 * @author annebrinegar, Megan Pierce
	 *
	 */
	private class Sequence extends RE {
		private RE first;
		private RE second;

		public Sequence(RE first, RE second) {
			super(regEx);
			this.first = first;
			this.second = second;
		}
	}

	/**
	 * empty character representation
	 * 
	 * @author annebrinegar, Megan Pierce
	 *
	 */
	private class Blank extends RE {

		public Blank(String regEx) {
			super(regEx);
		}
	}

	/**
	 * indicates star?
	 * 
	 * @author annebrinegar, Megan Pierce
	 *
	 */
	private class Repetition extends RE {
		private RE internal;

		public Repetition(RE internal) {
			super(regEx);
			this.internal = internal;
		}
	}

	/**
	 * bare bones, individual char.
	 * @author annebrinegar, Megan Pierce
	 *
	 */
	private class Primitive extends RE {
		private char c;
		public Primitive(char c) {
			super(regEx);
			this.c = c;
		}
	}
}

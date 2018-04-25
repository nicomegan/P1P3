package re;

import javax.sound.midi.Sequence;

import fa.nfa.NFA;

public class RE implements REInterface {

	private static RE blank;
	private String regEx;

	public RE(String regEx) {
		this.regEx = regEx;
	}

	@Override
	public NFA getNFA() {
		// TODO Auto-generated method stub
		return null;
	}

	/* TODO Decent parser!!! */
	// Not sure if needed

	
	//not sure what this is
	public RE parse() {
		return null;
	}

	/* Recursive descent parsing internals. */

	private char peek() {
		return regEx.charAt(0);
	}

	private void eat(char c) {
		if (peek() == c)
			this.regEx = this.regEx.substring(1);
		else
			throw new RuntimeException("Expected: " + c + "; got: " + peek());
	}

	private char next() {
		char c = peek();
		eat(c);
		return c;
	}

	/* Regular expression term types. */

//	private RE term() {
//		RE factor = RE.blank ;
//
//	    while (more() && peek() != ')' && peek() != '|') {
//	      RE nextFactor = factor() ;
//	      factor = new Sequence(factor,nextFactor) ;
//	    }
//
//	    return factor ;
//	}

	private RE factor() {
		return null;
//		RegEx base = base() ;
//	    
//	    while (more() && peek() == '*') {
//	      eat('*') ;
//	      base = new Repetition(base) ;
//	    }
//
//	    return base ;
	}

	private RE base() {
		return null;
//		switch (peek()) {
//	      case '(':
//	        eat('(') ;
//	        RegEx r = regex() ;  
//	        eat(')') ;
//	      return r ;
//
//	      case '\\':
//	       eat ('\\') ;
//	       char esc = next() ;
//	      return new Primitive(esc) ;
//
//	      default:
//	      return new Primitive(next()) ;
//	    }
	}

	private boolean more() {
		return regEx.length() > 0;
	}
//Some issue with this	
//	private class Choice extends RE
//	{
//	  private RE thisOne ;
//	  private RE thatOne ;
//
//	  public Choice(RE thisOne, RE thatOne) {
//	    this.thisOne = thisOne ;
//	    this.thatOne = thatOne ;
//	  }
//	}

}

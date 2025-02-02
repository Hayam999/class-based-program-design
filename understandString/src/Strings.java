// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {
    // combine all Strings in this list into one
    String combine();
    ILoString sort();
    ILoString insert(String other);
    boolean isSorted();
    boolean compare(String other);
    ILoString interleave(ILoString given);
    ILoString interleaveHelper(ILoString given);
    ILoString interleaveGiven(ILoString basic);
    ILoString merge(ILoString given);
    ILoString mergeWithoutMtList (ILoString given);
    ILoString reverse();
    ILoString reverseHelper(ILoString acc);
    boolean isDoubledList();
    boolean isDoubledHelper(String f);
    boolean isPalindromeList();
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
    MtLoString(){}
    
    // combine all Strings in this list into one
    public String combine() {
       return "";
    }  
    public ILoString sort() {
    	return new MtLoString();
    }
    public ILoString insert(String other) {
    	return new ConsLoString(other, new MtLoString());
    }
    public boolean isSorted() {
    	return true;
    }
    public boolean compare(String other) {
    	return true;
    }
    public ILoString interleave(ILoString given) {
    	return given;
    }
    public ILoString interleaveHelper(ILoString given) {
    	return given;
    }
    public ILoString interleaveGiven(ILoString basic) {
    	return basic;
    }
    public ILoString merge(ILoString given) {
    	return given;
    }
    public ILoString mergeWithoutMtList (ILoString given) {
    	return given;
    }
    public ILoString reverse() {
    	return new MtLoString();
    }
    public ILoString reverseHelper(ILoString acc)
    {
    	return acc;
    }
    public boolean isDoubledList() {
    	return true;
    }
    public boolean isDoubledHelper(String f) {
    	return false;
    }
    public boolean isPalindromeList() {
    	return false;
    }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
    String first;
    ILoString rest;
    
    ConsLoString(String first, ILoString rest){
        this.first = first;
        this.rest = rest;  
    }
    
    /*
     TEMPLATE
     FIELDS:
     ... this.first ...         -- String
     ... this.rest ...          -- ILoString
     
     METHODS
     ... this.combine() ...     -- String
     
     METHODS FOR FIELDS
     ... this.first.concat(String) ...        -- String
     ... this.first.compareTo(String) ...     -- int
     ... this.rest.combine() ...              -- String
     
     */
    
    // combine all Strings in this list into one
    public String combine(){
        return this.first.concat(this.rest.combine());
    }  
    public ILoString sort() {
    	return this.rest.sort().insert(this.first);
    }
    public ILoString insert(String other) {
    	if (this.first.toLowerCase().charAt(0) < other.toLowerCase().charAt(0)) {
    		return new ConsLoString(this.first, this.rest.insert(other));
    	}
    	else {
    		return new ConsLoString(other, this);
    	}
    }
    public boolean isSorted() {
    	return this.rest.isSorted() && this.rest.compare(this.first);
    }
    public boolean compare(String other) {
    	return other.toLowerCase().charAt(0) <= this.first.toLowerCase().charAt(0);
    }
    public ILoString interleave(ILoString given) {
    	return interleaveHelper(given);
    }
    public ILoString interleaveHelper(ILoString given) {
    		return new ConsLoString(this.first, given.interleaveGiven(this.rest));
    }
    public ILoString interleaveGiven(ILoString basic) {
    	return new ConsLoString(this.first, basic.interleaveHelper(this.rest));
    }
    public ILoString merge(ILoString given) {
    	return mergeWithoutMtList(given).sort();
    }
    public ILoString mergeWithoutMtList (ILoString given) {
    	return new ConsLoString(this.first, this.rest.mergeWithoutMtList(given));
    }
    public ILoString reverse() {
    	return this.rest.reverseHelper(new ConsLoString(this.first, new MtLoString()));
    	}
    public ILoString reverseHelper(ILoString acc)
    {
    	return this.rest.reverseHelper(new ConsLoString(this.first, acc));
    }
    public boolean isDoubledList() {
    	return this.rest.isDoubledHelper(this.first);
    }
    public boolean isDoubledHelper(String f) {
    	if (this.first.toLowerCase().equals(f.toLowerCase())) {
    		return this.rest.isDoubledList();
    	}
    	else {
    		return false;
    	}
    }
    public boolean isPalindromeList() {
    	return interleave(reverse()).isDoubledList();
    }
}


// to represent examples for lists of strings
class ExamplesStrings{
    
    ILoString mary = new ConsLoString("Mary ",
                    new ConsLoString("had ",
                        new ConsLoString("a ",
                            new ConsLoString("little ",
                                new ConsLoString("lamb.", new MtLoString())))));
    ILoString sortedMary = this.mary.sort();
    ILoString hayam = new ConsLoString("Hayam ", new ConsLoString("Computer ",
    		new ConsLoString("so ", new MtLoString())));
    ILoString loves = new ConsLoString("loves ", new ConsLoString("science ",
    		new ConsLoString("much.", new MtLoString())));
    ILoString rice = new ConsLoString("Rice ", new ConsLoString("good ",
    		new ConsLoString("your ", new MtLoString())));
    ILoString rice2 = new ConsLoString("is ", new ConsLoString("for ",
    		new ConsLoString("health ", new ConsLoString(",So ", new ConsLoString ("eat ",
    				new ConsLoString("it ", new  ConsLoString("regularly", new MtLoString())))))));
    ILoString i = new ConsLoString("i ", new ConsLoString("your ",
    		new ConsLoString("shirt ", new ConsLoString("So ", new ConsLoString("much", new MtLoString())))));
    ILoString iGivine = new ConsLoString("love ", new MtLoString());
    ILoString doubledList = new ConsLoString("so",new ConsLoString("So", new ConsLoString("Mo",
    		new ConsLoString("mo", new ConsLoString("lo", new ConsLoString("lo", new MtLoString()))))));
    ILoString tailedList = new ConsLoString("so",new ConsLoString("So", new ConsLoString("Mo",
    		new ConsLoString("mo", new ConsLoString("lo", new ConsLoString("lo",
    				new ConsLoString("do", new MtLoString())))))));
    ILoString lst1 = new ConsLoString("i", new ConsLoString("love", new ConsLoString("i", new MtLoString())));
    
    // test the method combine for the lists of Strings
    boolean testCombine(Tester t){
        return 
            t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
    }
    boolean testSort(Tester t) {
    	return t.checkExpect(this.mary.sort(),
                    new ConsLoString("a ",
                        new ConsLoString("had ",
                            new ConsLoString("little ",
                                new ConsLoString("lamb.", new ConsLoString("Mary ",new MtLoString()))))));
    }
    boolean testIsSorted(Tester t) {
    	return t.checkExpect(sortedMary.isSorted(), true)
    	&& t.checkExpect(mary.isSorted(), false);
    }
    boolean testInterleave(Tester t) {
    	return t.checkExpect(hayam.interleave(loves).combine(), "Hayam loves Computer science so much.")
    			&& t.checkExpect(rice.interleave(rice2).combine(), "Rice is good for your health ,So eat it regularly")
    			&& t.checkExpect(i.interleave(iGivine).combine(), "i love your shirt So much");
    }
    boolean testMergeWithoutMtList(Tester t) {
    	return t.checkExpect(hayam.mergeWithoutMtList(iGivine), new ConsLoString("Hayam ", new ConsLoString("Computer ",
        		new ConsLoString("so ", iGivine))));
    }
    boolean testReverse(Tester t) {
    	return t.checkExpect(hayam.reverse(), new ConsLoString("so ",
    			new ConsLoString( "Computer ", new ConsLoString("Hayam ", new MtLoString()))));
    }
    boolean testIsDoubledList(Tester t) {
    	return t.checkExpect(doubledList.isDoubledList(), true) &&
    			  t.checkExpect(tailedList.isDoubledList(), false) &&
    			  t.checkExpect(hayam.isDoubledList(), false);
    }
    boolean testIsPalindromedList(Tester t) {
    	return t.checkExpect(lst1.isPalindromeList(), true)
    			&& t.checkExpect(loves.isPalindromeList(), false);
    }
}
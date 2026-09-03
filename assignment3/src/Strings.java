// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {
    // combine all Strings in this list into one
    String combine();
    ILoString sort();
    ILoString sortHelper(ILoString newSortedList);
    ILoString injectBiggest(String biggest, ILoString newSortedList);
    String returnBiggest(String otherBiggest);
    String getFirst();
    ILoString removeBiggest(String biggest);
    ILoString removeBiggestHelper(ILoString accList, String biggest, boolean once);
    boolean sameFirst(String str);
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
    public ILoString sortHelper(ILoString newSortedList) {
        return newSortedList;
    }
    public ILoString injectBiggest(String biggest, ILoString newSortedList) {
        return new ConsLoString(biggest, newSortedList);
    }
    public String returnBiggest(String otherBiggest) {
        return otherBiggest;
    }
    public ILoString removeBiggest(String biggest) {
        return new MtLoString();
    }
    public String getFirst() {
        return "";
    }
    public ILoString removeBiggestHelper(ILoString accList, String biggest, boolean once) {
        return accList;
    }
    public boolean sameFirst(String str) {
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
        return this.sortHelper(new MtLoString());
    }
   public ILoString sortHelper(ILoString newSortedList) {
    newSortedList = this.rest.injectBiggest(this.first, newSortedList);
    ILoString newRest = this.removeBiggest(newSortedList.getFirst());
    return newRest.sortHelper(newSortedList);
   } 
   public ILoString injectBiggest(String biggest,ILoString newSortedList) {
        String newBiggest = this.returnBiggest(biggest);
        return this.rest.injectBiggest(newBiggest, newSortedList);
   }
   public String returnBiggest(String otherBiggest) {
    if (this.first.toLowerCase().charAt(0) > otherBiggest.toLowerCase().charAt(0)) {
        return this.first;
    } else return otherBiggest;
   }
    public ILoString removeBiggest(String biggest) {
        return this.removeBiggestHelper(new MtLoString(), biggest, true);
    }
    public String getFirst() {
        return this.first;
    }
    public ILoString removeBiggestHelper(ILoString accList, String biggest, boolean once) {
       if (once) {
        if (this.sameFirst(biggest)) {
            return this.rest.removeBiggestHelper(accList, biggest, false);
        }
       }
        return this.rest.removeBiggestHelper(new ConsLoString(this.first, accList), biggest, once);
    }




    public boolean sameFirst(String str) {
        String lowerFirst = this.first.toLowerCase();
        String lowerStr = str.toLowerCase();

        if (lowerFirst.charAt(0) == lowerStr.charAt(0) && lowerFirst.length() == lowerStr.length()) {
            return true;
        }
        return false;
    }
}

// to represent examples for lists of strings
class ExamplesStrings{
    
    ILoString mary = new ConsLoString("Mary ",
                    new ConsLoString("had ",
                        new ConsLoString("a ",
                            new ConsLoString("little ",
                                new ConsLoString("lamb.", new MtLoString())))));
    ILoString sortedMary = new ConsLoString("a ",
                                new ConsLoString("had ",
                                    new ConsLoString("little ",
                                        new ConsLoString("lamb.", 
                                            new ConsLoString("Mary ", new MtLoString())))));
    ILoString mtStrings = new MtLoString();
     // a single-element list
    ILoString oneWord = new ConsLoString("hello", new MtLoString());
 
    // already sorted, no duplicates
    ILoString sortedFruits = new ConsLoString("apple",
                                new ConsLoString("banana",
                                    new ConsLoString("cherry", new MtLoString())));
 
    // reverse sorted -- worst case for an insertion-sort-style sort
    ILoString reverseFruits = new ConsLoString("cherry",
                                new ConsLoString("banana",
                                    new ConsLoString("apple", new MtLoString())));
 
    // unsorted / "random" order
    ILoString messyFruits = new ConsLoString("cherry",
                                new ConsLoString("apple",
                                    new ConsLoString("date",
                                        new ConsLoString("banana", new MtLoString()))));
 
    // ---- repetition ----
 
    // duplicate strings scattered through the list
    ILoString duplicates = new ConsLoString("banana",
                                new ConsLoString("apple",
                                    new ConsLoString("banana",
                                        new ConsLoString("apple", new MtLoString()))));
 
    // every element identical
    ILoString allSame = new ConsLoString("kiwi",
                            new ConsLoString("kiwi",
                                new ConsLoString("kiwi", new MtLoString())));
 
    // ---- corner cases ----
 
    // one string is a prefix of another -- the shorter one sorts first
    ILoString prefixes = new ConsLoString("catalog",
                            new ConsLoString("cat",
                                new ConsLoString("cats", new MtLoString())));
 
    // contains the empty string, which sorts before everything else
    ILoString withEmpty = new ConsLoString("banana",
                            new ConsLoString("",
                                new ConsLoString("apple", new MtLoString())));
 
    // mixed case -- under String's natural ordering (compareTo),
    // ALL capital letters sort before ALL lowercase letters
    ILoString mixedCase = new ConsLoString("banana",
                            new ConsLoString("Apple",
                                new ConsLoString("cherry",
                                    new ConsLoString("Banana", new MtLoString()))));
 
    // single-character strings, out of order
    ILoString letters = new ConsLoString("d",
                            new ConsLoString("b",
                                new ConsLoString("a",
                                    new ConsLoString("c", new MtLoString()))));

    
    // test the method combine for the lists of Strings
    boolean testCombine(Tester t){
        return 
            t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
    }

    boolean testsort(Tester t){
        return 
            t.checkExpect(this.mary.sort(), sortedMary);
    }
 
    // test the method sort for the lists of Strings
    boolean testSort(Tester t) {
        return
            // ---- empty and singleton ----
            t.checkExpect(this.mtStrings.sort(), new MtLoString())
 
            && t.checkExpect(this.oneWord.sort(),
                    new ConsLoString("hello", new MtLoString()))
 
            // ---- already sorted: should come back unchanged ----
            && t.checkExpect(this.sortedFruits.sort(),
                    new ConsLoString("apple",
                        new ConsLoString("banana",
                            new ConsLoString("cherry", new MtLoString()))))
 
            // ---- reverse sorted ----
            && t.checkExpect(this.reverseFruits.sort(),
                    new ConsLoString("apple",
                        new ConsLoString("banana",
                            new ConsLoString("cherry", new MtLoString()))))
 
            // ---- unsorted / random order ----
            && t.checkExpect(this.messyFruits.sort(),
                    new ConsLoString("apple",
                        new ConsLoString("banana",
                            new ConsLoString("cherry",
                                new ConsLoString("date", new MtLoString())))))
 
            // ---- repetition: duplicates must all survive the sort ----
            && t.checkExpect(this.duplicates.sort(),
                    new ConsLoString("apple",
                        new ConsLoString("apple",
                            new ConsLoString("banana",
                                new ConsLoString("banana", new MtLoString())))))
 
            && t.checkExpect(this.allSame.sort(),
                    new ConsLoString("kiwi",
                        new ConsLoString("kiwi",
                            new ConsLoString("kiwi", new MtLoString()))));
/**        // ---- corner case: one string a prefix of another ----
            && t.checkExpect(this.prefixes.sort(),
                    new ConsLoString("cat",
                        new ConsLoString("catalog",
                            new ConsLoString("cats", new MtLoString()))))
   
    
            // ---- corner case: empty string sorts first ----
            && t.checkExpect(this.withEmpty.sort(),
                    new ConsLoString("",
                        new ConsLoString("apple",
                            new ConsLoString("banana", new MtLoString()))));
 
            // ---- corner case: mixed case, capitals before lowercase ----
            && t.checkExpect(this.mixedCase.sort(),
                    new ConsLoString("Apple",
                        new ConsLoString("Banana",
                            new ConsLoString("banana",
                                new ConsLoString("cherry", new MtLoString())))));
/*  
            // ---- single-character strings ----
            && t.checkExpect(this.letters.sort(),
                    new ConsLoString("a",
                        new ConsLoString("b",
                            new ConsLoString("c",
                                new ConsLoString("d", new MtLoString())))));
       // ---- invariant: sorting never changes the total number of
            //      characters in the list (nothing lost, nothing added) ----
            && t.checkExpect(this.duplicates.sort().combine().length(),
                    this.duplicates.combine().length());
    */
     }
 

    public static void main(String[] args) {
    
        ExamplesStrings strings = new ExamplesStrings();
        ILoString result = strings.mary.sort(); 
         System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesStrings(), false, false);
    }
}


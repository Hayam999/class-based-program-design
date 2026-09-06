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
    boolean isSorted();
    
    /* 
        produce a list where the first, third, fifth...
        elements are from this list, and the second, fourth, sixth....
        elements are from the given list;
        Any leftover elements is at the end
    **/ 
    ILoString interleave(ILoString otherList);
    ILoString interleaveHelper(ILoString otheStrings, int posTracker, ILoString newList);
    ILoString addLeftOvers(ILoString newList);
    ILoString handleInterleavingOthers(ILoString originalList, int posTracker, ILoString newList);
    ILoString reverse();
    ILoString reverseHelper(ILoString newList);

    /*
        merge 2 sorted lists into one list and produce a new sorted list with all items including duplicates.
        suppose both lists been already sorted, and will not contain empty elements "", as "" will be the end of the list.
     */
    ILoString merge(ILoString otherList);
    ILoString mergeHelper(ILoString otherList, ILoString newList);
    ILoString handleMergeHelperRecur(ILoString originalList, ILoString newList);

    /*
        checks if the elements 1,2 are the same and 3,4 same ... etc
     */
    boolean isDuplicatedList();
    boolean isDuplicatedHelper(int tracker, String prevFirst);

    boolean isPalindromeList();
    boolean isPalindromeHelper(ILoString reveredList);
    boolean handlePalindromeRecur(ILoString original);
  

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
    public boolean isSorted() {
        return true;
    }
    public ILoString interleave(ILoString otherList) {
        return otherList;
    }
    public ILoString interleaveHelper(ILoString otheStrings, int posTracker, ILoString newList) {
        return otheStrings.addLeftOvers(newList);
    }
   public ILoString addLeftOvers(ILoString newList) {
    return newList.reverse();
   }
   public ILoString handleInterleavingOthers(ILoString originalList, int posTracker, ILoString newList) {
        return originalList.addLeftOvers(newList);
   }
   public ILoString reverse() {
    return new MtLoString();
   }
   public ILoString reverseHelper(ILoString newList) {
    return newList;
   }
   public ILoString merge(ILoString otherList) {
    return otherList;
   }
   public ILoString mergeHelper(ILoString otherList, ILoString newList) {
    return otherList.addLeftOvers(newList);
   }
   public ILoString handleMergeHelperRecur(ILoString originalList, ILoString newList) {
    return originalList.addLeftOvers(newList);
   }

   public boolean isDuplicatedList() {
    return false;
   }
   public boolean isDuplicatedHelper(int tracker, String prevFirst) {
    return true;
   }

   public boolean isPalindromeList() {
    return false;
   }
   public boolean isPalindromeHelper(ILoString reversedList) {
    return true;
   }
   public boolean handlePalindromeRecur(ILoString original) {
        return original.isPalindromeHelper(this);
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

    public boolean isSorted() {
        String secondFirst = this.rest.getFirst();
        if (secondFirst == "") {
            return true;
        }
        if (this.first.toLowerCase().charAt(0) > (secondFirst.toLowerCase().charAt(0))) {
            return false;
        } else {
            return this.rest.isSorted();
        }
    }
    public ILoString interleave (ILoString otherString) {
        return this.interleaveHelper(otherString, 1, new MtLoString());
    }
    public ILoString interleaveHelper(ILoString otherStrings, int posTracker, ILoString newList) {
          if (posTracker % 2 == 0) {
            // delegate recursion call to otherStrings. to avoid calling rest & first on an empty 
            return otherStrings.handleInterleavingOthers(this, posTracker, newList);
        } else {
            return this.rest.interleaveHelper(otherStrings, posTracker + 1, new ConsLoString(this.first, newList));
            }
        
    }
    public ILoString addLeftOvers(ILoString newList) {
        return this.rest.addLeftOvers(new ConsLoString(this.first, newList));
    }

    public ILoString handleInterleavingOthers(ILoString originalList, int posTracker, ILoString newList) {
        return originalList.interleaveHelper(this.rest, posTracker + 1, new ConsLoString(this.first, newList));
    }
    public ILoString reverse() {
        return reverseHelper(new MtLoString());
    }
    public ILoString reverseHelper(ILoString newList) {
        return this.rest.reverseHelper(new ConsLoString(this.first, newList));
    }

    public ILoString merge(ILoString otherList) {
        return this.mergeHelper(otherList, new MtLoString());
    }

    public ILoString mergeHelper(ILoString otherList, ILoString newList) {
        String otherFirst =  otherList.getFirst();
        if (otherFirst == "") {
            return this.addLeftOvers(newList);
        }

        if (this.first.toLowerCase().charAt(0) < otherFirst.toLowerCase().charAt(0)) {
            return this.rest.mergeHelper(otherList, new ConsLoString(this.first, newList));
        }

        // delegate recurstion to otherList
        return otherList.handleMergeHelperRecur(this,newList);
    }

   public ILoString handleMergeHelperRecur(ILoString originalList, ILoString newList) {
        // assume we will inject this.first and the comparison have been done already.
        return originalList.mergeHelper(this.rest, new ConsLoString(this.first, newList));
   }

   public boolean isDuplicatedList() {
        return this.rest.isDuplicatedHelper(1, this.first);
   }
   public boolean isDuplicatedHelper(int tracker, String prevFirst) {
        if (tracker % 2 == 0) {
            return this.rest.isDuplicatedHelper(tracker + 1, this.first);
        }
        if (this.first == prevFirst) {
            return this.rest.isDuplicatedHelper( tracker + 1, prevFirst);
        }
        return false;
   }
   public boolean isPalindromeList() {
    return isPalindromeHelper(this.reverse());
   }
   public boolean isPalindromeHelper(ILoString reversedList) {
        ILoString firstAndLast = new ConsLoString(this.first,
            new ConsLoString(reversedList.getFirst(), new MtLoString()));
        if (firstAndLast.isDuplicatedList()) {
            return reversedList. handlePalindromeRecur(this.rest);
        } 
        return false;
   }

   public boolean handlePalindromeRecur(ILoString original) {
        return original.isPalindromeHelper(this.rest);
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

    ILoString originalLitters1 = letters.sort();
    ILoString otherLitters1 = new ConsLoString("e", new ConsLoString("f", new ConsLoString ("g",
                                    new ConsLoString("h", new MtLoString()))));

    ILoString originalLitters2 = new ConsLoString("a",
                                    new ConsLoString("c",
                                        new ConsLoString("e",
                                            new ConsLoString("g", new MtLoString()))));

    ILoString otherLitters2 = new ConsLoString("b",
                                new ConsLoString("d",
                                    new ConsLoString("f", 
                                        new ConsLoString("h", new MtLoString()))));
    // ================= interleave() test data =================

    // dedicated equal-length input to pair with sortedFruits
    ILoString numbers = new ConsLoString("one",
                            new ConsLoString("two",
                                new ConsLoString("three", new MtLoString())));

    // a clean, dedicated shorter/longer pair (2 vs 4) for leftover checks
    ILoString twoLetters = new ConsLoString("x", new ConsLoString("y", new MtLoString()));
    ILoString fourLetters = new ConsLoString("p",
                                new ConsLoString("q",
                                    new ConsLoString("r",
                                        new ConsLoString("s", new MtLoString()))));

    
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

     }
     

    boolean testIsSorted(Tester t) {
        return
        t.checkExpect(mary.isSorted(), false) &&
        t.checkExpect(sortedMary.isSorted(), true) &&
        t.checkExpect(allSame.isSorted(), true) &&
        t.checkExpect(duplicates.isSorted(), false);

    }

    // ================= interleave() tests =================
    // Semantics assumed: result[0] = this[0], result[1] = given[0], result[2] = this[1], ...
    // and once either list runs out, the entire remaining tail of the OTHER list
    // is appended unchanged (in its original order) at the end.

    // both lists empty -> empty result
    boolean testInterleaveBothEmpty(Tester t) {
        return t.checkExpect(this.mtStrings.interleave(this.mtStrings), new MtLoString());
    }
 
    // this list empty, given non-empty -> result is exactly the given list
    boolean testInterleaveThisEmpty(Tester t) {
        return t.checkExpect(this.mtStrings.interleave(this.oneWord), this.oneWord);
    }

    // this list non-empty, given empty -> result is exactly this list
    boolean testInterleaveGivenEmpty(Tester t) {
        return t.checkExpect(this.oneWord.interleave(this.mtStrings), this.oneWord);
    }

    // one element in each list
    boolean testInterleaveSingleElements(Tester t) {
        return t.checkExpect(
            this.oneWord.interleave(new ConsLoString("world", new MtLoString())),
            new ConsLoString("hello", new ConsLoString("world", new MtLoString())));
    }
 
    // equal-length lists -> no leftovers at all
    boolean testInterleaveEqualLength(Tester t) {
        return t.checkExpect(this.sortedFruits.interleave(this.numbers),
            new ConsLoString("apple",
                new ConsLoString("one",
                    new ConsLoString("banana",
                        new ConsLoString("two",
                            new ConsLoString("cherry",
                                new ConsLoString("three", new MtLoString())))))));
    }

    // this list longer (4 vs 2) -> 2 leftover elements from this at the end
    boolean testInterleaveThisLonger(Tester t) {
        return t.checkExpect(this.fourLetters.interleave(this.twoLetters),
            new ConsLoString("p",
                new ConsLoString("x",
                    new ConsLoString("q",
                        new ConsLoString("y",
                            new ConsLoString("r",
                                new ConsLoString("s", new MtLoString())))))));
    }

    // given list longer (2 vs 4) -> 2 leftover elements from given at the end
    boolean testInterleaveGivenLonger(Tester t) {
        return t.checkExpect(this.twoLetters.interleave(this.fourLetters),
            new ConsLoString("x",
                new ConsLoString("p",
                    new ConsLoString("y",
                        new ConsLoString("q",
                            new ConsLoString("r",
                                new ConsLoString("s", new MtLoString())))))));
    }

    // bigger example: this longer by 2 (5 vs 3), reusing existing example data
    boolean testInterleaveThisLongerBigGap(Tester t) {
        return t.checkExpect(this.mary.interleave(this.sortedFruits),
            new ConsLoString("Mary ",
                new ConsLoString("apple",
                    new ConsLoString("had ",
                        new ConsLoString("banana",
                            new ConsLoString("a ",
                                new ConsLoString("cherry",
                                    new ConsLoString("little ",
                                        new ConsLoString("lamb.", new MtLoString())))))))));
    }

    // mirror of the above: given longer by 2 (3 vs 5)
    boolean testInterleaveGivenLongerBigGap(Tester t) {
        return t.checkExpect(this.sortedFruits.interleave(this.mary),
            new ConsLoString("apple",
                new ConsLoString("Mary ",
                    new ConsLoString("banana",
                        new ConsLoString("had ",
                            new ConsLoString("cherry",
                                new ConsLoString("a ",
                                    new ConsLoString("little ",
                                        new ConsLoString("lamb.", new MtLoString())))))))));
    }

    // duplicates on both sides must all survive, in their original relative order
    boolean testInterleaveWithDuplicates(Tester t) {
        return t.checkExpect(this.duplicates.interleave(this.allSame),
            new ConsLoString("banana",
                new ConsLoString("kiwi",
                    new ConsLoString("apple",
                        new ConsLoString("kiwi",
                            new ConsLoString("banana",
                                new ConsLoString("kiwi",
                                    new ConsLoString("apple", new MtLoString()))))))));
    }

    // a list interleaved with itself
    boolean testInterleaveWithSelf(Tester t) {
        return t.checkExpect(this.letters.interleave(this.letters),
            new ConsLoString("d",
                new ConsLoString("d",
                    new ConsLoString("b",
                        new ConsLoString("b",
                            new ConsLoString("a",
                                new ConsLoString("a",
                                    new ConsLoString("c",
                                        new ConsLoString("c", new MtLoString())))))))));
    }

    boolean testdifferentInterleavingAndMerging(Tester t) {
        return (t.checkExpect(originalLitters1.interleave(otherLitters1), new ConsLoString("a", 
            new ConsLoString("e",
                new ConsLoString("b",
                    new ConsLoString("f",
                        new ConsLoString("c",
                            new ConsLoString("g",
                                new ConsLoString("d",
                                    new ConsLoString("h", new MtLoString())))))))))) &&
         (t.checkExpect(originalLitters1.sort().merge(otherLitters1.sort()),
                    new ConsLoString("a",
                        new ConsLoString("b",
                            new ConsLoString("c",
                            new ConsLoString("d",
                            new ConsLoString("e",
                                new ConsLoString("f",
                                    new ConsLoString("g", new ConsLoString ("h",
                                        new MtLoString()))))))))));
    }
    boolean testSameInterleavingAndMerging(Tester t) {
        return t.checkExpect(originalLitters2.interleave(otherLitters2),
                                new ConsLoString("a",
                                    new ConsLoString("b",
                                        new ConsLoString("c",
                                            new ConsLoString("d",
                                                new ConsLoString("e", 
                                                    new ConsLoString("f",
                                                        new ConsLoString("g",
                                                            new ConsLoString("h", 
                                                            new MtLoString()))))))))) &&
                                                            
                t.checkExpect(originalLitters2.sort().merge(otherLitters2.sort()),
                                new ConsLoString("a",
                                    new ConsLoString("b",
                                        new ConsLoString("c",
                                            new ConsLoString("d",
                                                new ConsLoString("e", 
                                                    new ConsLoString("f",
                                                        new ConsLoString("g",
                                                            new ConsLoString("h", 
                                                            new MtLoString())))))))));

    }

    ILoString isDuplicated = new ConsLoString("art",
        new ConsLoString("art",
            new ConsLoString("Music",
                new ConsLoString("Music",
                    new ConsLoString("peace",
                        new ConsLoString("peace", new MtLoString())
                    )
                )
            )
        )
    );
    ILoString notDup = new ConsLoString("live", isDuplicated);
    ILoString uniqe = new ConsLoString("Don't", new ConsLoString("just", 
        new ConsLoString("be there", new MtLoString())));

    ILoString tailDup = new ConsLoString("art",
            new ConsLoString("Music",
                new ConsLoString("Music",
                    new ConsLoString("peace",
                        new ConsLoString("peace", new ConsLoString ("tail", new MtLoString())
                    )
                )
            )
        )
    );

    boolean testIsDuplicatedList(Tester t) {
        return t.checkExpect(duplicates.isDuplicatedList(), false) &&
        t.checkExpect(isDuplicated.isDuplicatedList(), true) &&
        t.checkExpect(uniqe.isDuplicatedList(), false) &&
        t.checkExpect(tailDup.isDuplicatedList(), false) &&
        t.checkExpect(notDup.isDuplicatedList(), false);
    }

    ILoString palinList = new ConsLoString("Stay",
        new ConsLoString("Calm", 
            new ConsLoString("So",
                new ConsLoString("Calm", new ConsLoString("Stay", new MtLoString())))));

    ILoString plainTripleinTheMiddle =  new ConsLoString("Stay",
        new ConsLoString("Calm", 
            new ConsLoString("Calm",
                new ConsLoString("Calm", new ConsLoString("Stay", new MtLoString())))));

    boolean testIsPlaindromeList(Tester t) {
        return t.checkExpect(palinList.isPalindromeList(), true) &&
        t.checkExpect(isDuplicated.isPalindromeList(), false) &&
        t.checkExpect(plainTripleinTheMiddle.isPalindromeList(), true) &&
        t.checkExpect(originalLitters1.isPalindromeList(), false) &&
        t.checkExpect(duplicates.isPalindromeList(), false);
    }
    public static void main(String[] args) {
    
        ExamplesStrings strings = new ExamplesStrings();
        ILoString result = strings.originalLitters2.merge(strings.otherLitters2);
         System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesStrings(), false, false);
    }
}


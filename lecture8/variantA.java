import tester.Tester;

interface ILoNumber {
    boolean satisfying();
    boolean satHelper(Conditions conds);
}

class MtLoNumber implements ILoNumber {
    MtLoNumber(){}

    public boolean satisfying() {
        return false;
    }
    public boolean satHelper(Conditions conds) {
        return conds.allGood();
    }
}

class ConsLoNumber implements ILoNumber{
    int first;
    ILoNumber rest;

    ConsLoNumber(int first, ILoNumber rest) {
        this.first = first;
        this.rest = rest;
    }

    public boolean satisfying() {
        Conditions conds = new Conditions(false, false, false);
        return this.satHelper(conds);
    }

    public boolean satHelper(Conditions conds) {
        if (conds.allGood()) {
            return true;
        }
        else {
            return this.rest.satHelper(conds.updateConds(this.first));
        }
    }
}

class Conditions {
    boolean isEven;
    boolean isPositiveAndOdd;
    boolean from5To10;
    boolean evenUpdated = false;
    boolean PositiveUpdated = false;
    boolean from5To10Updated = false;

    Conditions(boolean isEven, boolean isPositiveAndOdd, boolean from5To10) {
        this.isEven = isEven;
        this.isPositiveAndOdd = isPositiveAndOdd;
        this.from5To10 = from5To10;
    }
    public boolean allGood() {
        return this.isEven && this.isPositiveAndOdd && this.from5To10;
    }
    public Conditions updateConds(int num) {
        if (!evenUpdated) {
            this.isEven = checkEven(num);
            evenUpdated = isEven;
        }
        if (!PositiveUpdated) {
            this.isPositiveAndOdd = checkPosAndOdd(num);
            PositiveUpdated = isPositiveAndOdd;
        }
        if (!from5To10Updated) {
            this.from5To10 = checkRange(num);
            from5To10Updated = from5To10;
        }
        return this;
    }
    private boolean checkEven(int num) {
        return num % 2 == 0;
    }
    private boolean checkPosAndOdd(int num) {
        return (num > 0 && num % 2 != 0);
    }
    private boolean checkRange(int num) {
        return (num >= 5 && num <= 10);
    }
}

class ExamplesNumbers {
    ILoNumber empty = new MtLoNumber();
    ILoNumber satisfyingList = new ConsLoNumber(6, new ConsLoNumber(5, empty));
    ILoNumber unSutisfyingList = new ConsLoNumber(3, new ConsLoNumber(4, empty));


    boolean testSatisfyingList(Tester t) {
        return t.checkExpect(satisfyingList.satisfying(), true);
    }
    boolean testunSutisfyingList(Tester t) {
        return t.checkExpect(unSutisfyingList.satisfying(), false);
    }
    public static void main(String[] args) {
        // ============================================================
        // DEBUG MODE: use this when you want to set a breakpoint and
        // step through a specific method. Tester.runReport is NOT used
        // here, so there's no 60ms watchdog timeout to fight with the
        // debugger -- you can pause for as long as you want.
        //
        // To use it: comment out the "NORMAL MODE" line below, uncomment
        // this block, edit the method call to whatever you're currently
        // debugging, put your breakpoint inside that method, then hit
        // Debug (F5) on this file/config.
        // ------------------------------------------------------------
         ExamplesNumbers nums = new ExamplesNumbers();
         boolean result = nums.satisfyingList.satisfying(); 
         System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesNumbers(), false, false);
    }
}
import tester.*;

interface ILoNumber {
    boolean satisfying();
    boolean satHelper(IConditions conds);
    boolean strictSatisfy();
    boolean strictSatHelper(IConditions conds);
    boolean satStrictNoEx();
    boolean satStrictNoExHelper(AlarmConditions conds);
}
interface IConditions{
    boolean allGood();
    IConditions updateConds(int num);
    IConditions updateStrictConds(int num); 
}

abstract class AConditions implements IConditions{
    boolean isEven;
    boolean isPositiveAndOdd;
    boolean from5To10;
    boolean evenUpdated = false;
    boolean PositiveUpdated = false;
    boolean from5To10Updated = false;

    AConditions(boolean isEven, boolean isPositiveAndOdd, boolean from5To10) {
        this.isEven = isEven;
        this.isPositiveAndOdd = isPositiveAndOdd;
        this.from5To10 = from5To10;
    }
    public boolean allGood() {
        return this.isEven && this.isPositiveAndOdd && this.from5To10;
    }
    public IConditions updateConds(int num) {
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

    public IConditions updateStrictConds(int num) {
        updateStrictCondsHelper(num);
            return this;
    }

    public boolean updateStrictCondsHelper(int num) {
        
        if (!evenUpdated) {
            this.isEven = checkEven(num);
            if (isEven) {
                evenUpdated = isEven;
                return false;
            }
        }
        if (!PositiveUpdated) {
            this.isPositiveAndOdd = checkPosAndOdd(num);
            if (isPositiveAndOdd) {
                PositiveUpdated = isPositiveAndOdd;
                return false;
            }
        }
        if (!from5To10Updated) {
            this.from5To10 = checkRange(num);
            if (from5To10) {
                from5To10Updated = from5To10;
                return false;
            }
        } 
        return true;
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

class MtLoNumber implements ILoNumber {
    MtLoNumber(){}

    public boolean satisfying() {
        return false;
    }
    public boolean satHelper(IConditions conds) {
        return conds.allGood();
    }
    public boolean strictSatHelper(IConditions conds) {
        return conds.allGood();
    }
    public boolean strictSatisfy() {
        return false;
    }
    public boolean satStrictNoEx() {
        return false;
    }
    public boolean satStrictNoExHelper(AlarmConditions conds) {
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

    public boolean satHelper(IConditions conds) {
        if (conds.allGood()) {
            return true;
        }
        else {
            return this.rest.satHelper(conds.updateConds(this.first));
        }
    }

    public boolean strictSatisfy() {
        return strictSatHelper(new Conditions(false, false, false));
    }

    public boolean strictSatHelper(IConditions conds) {
       if (conds.allGood()) {
        return true;
       } 
       else return this.rest.strictSatHelper(conds.updateStrictConds(this.first));
    }
    
    public boolean satStrictNoEx() {
        return this.satStrictNoExHelper(new AlarmConditions(false, false, false, false));
    }
    public boolean satStrictNoExHelper(AlarmConditions conds) {
        if (conds.alarmOn) {
            return false;
        }
        else if (conds.allGood()) {
            return true;
        }
        else {
            return this.rest.satStrictNoExHelper(conds.updateCondsWithAlarm(this.first));
        }
    }

}

class Conditions extends AConditions {
    Conditions(boolean isEven, boolean isPositiveAndOdd, boolean from5To10) {
        super(isEven, isPositiveAndOdd, from5To10);
    }
}

class AlarmConditions extends AConditions {
    boolean alarmOn;
    AlarmConditions(boolean isEven, boolean isPositiveAndOdd, boolean from5To10, boolean alarmOn) {
        super(isEven, isPositiveAndOdd, from5To10);
        this.alarmOn = alarmOn;
    }

    AlarmConditions updateCondsWithAlarm(int num) {
        this.alarmOn = this.updateStrictCondsHelper(num);
        return this;
    }

}

class ExamplesNumbers {
    ILoNumber empty = new MtLoNumber();
    ILoNumber satisfyingList = new ConsLoNumber(6, new ConsLoNumber(5, empty));
    ILoNumber unSutisfyingList = new ConsLoNumber(3, new ConsLoNumber(4, empty));
    ILoNumber satisfyingStrict = new ConsLoNumber(5, new ConsLoNumber(6, new ConsLoNumber(6, empty)));
    ILoNumber ListWithEx = new ConsLoNumber(6, new ConsLoNumber(5, new ConsLoNumber(42, new ConsLoNumber(6, empty))));


    boolean testSatisfyingList(Tester t) {
        return t.checkExpect(satisfyingList.satisfying(), true);
    }
    boolean testunsutisfyinglist(Tester t) {
        return t.checkExpect(unSutisfyingList.satisfying(), false);
    }
    boolean testunsutisfyingStrict(Tester t) {
        return t.checkExpect(satisfyingList.strictSatisfy(), false);
    }
    boolean testsutisfyingStrict(Tester t) {
        return t.checkExpect(satisfyingStrict.strictSatisfy(), true);
    }
    boolean testsutisfyingStrictWithEx(Tester t) {
        return t.checkExpect(satisfyingStrict.satStrictNoEx(), true);
    }

    boolean testunsutisfyingStrictWithEx(Tester t) {
        return t.checkExpect(ListWithEx.satStrictNoEx(), false);
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
         boolean result = nums.satisfyingStrict.strictSatisfy(); 
         System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesNumbers(), false, false);
    }
}
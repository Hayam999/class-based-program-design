import tester.*;

interface MaybeInt {
    // this method uses double dispatch to test sameness of 2 MaybeInts
    boolean sameMayBeInt(MaybeInt other);
    boolean sameRealInt(int other);
    boolean sameFalseInt();
}

interface ILoInt {
    MaybeInt longestStreak();
    MaybeInt longestStreakHelper(MaybeInt runner, MaybeInt prevRunner, int currentStreak, int maxStreak);
}

class RealInt implements MaybeInt{
    int number;

    RealInt(int number) {
        this.number = number;
    }

    public boolean sameMayBeInt(MaybeInt other) {
        return other.sameRealInt(this.number);
    }

    public boolean sameRealInt(int otherRealInt) {
        return otherRealInt == this.number;
    }

    public boolean sameFalseInt() {
        return false;
    }


}

class FalseInt implements MaybeInt {
    FalseInt() {};

    public boolean sameMayBeInt(MaybeInt other) {
        return other.sameFalseInt();
    }
    
    public boolean sameRealInt(int otherRealInt) {
        return false;
    }

    public boolean sameFalseInt() {
        return true;
    }
}

class MtLoInt implements  ILoInt{
    MtLoInt() {}

    public MaybeInt longestStreak() {
        return new FalseInt();
    }

    public MaybeInt longestStreakHelper(MaybeInt runner, MaybeInt prevRunner, int currentStreak, int maxStreak) {
        if (maxStreak >= currentStreak) {
            return prevRunner;
        }
        else return runner;
    }

}

class ConsLoInt implements ILoInt {
    MaybeInt first;
    ILoInt rest;

    ConsLoInt(MaybeInt first, ILoInt rest) {
        this.first = first;
        this.rest = rest;
    }

    public MaybeInt longestStreak() {
        return this.rest.longestStreakHelper(this.first, this.first, 1, 1);
    }
    public MaybeInt longestStreakHelper(MaybeInt runner, MaybeInt prevRunner, int currentStreak, int maxStreak) {
        if (runner.sameMayBeInt(this.first)) {
            return this.rest.longestStreakHelper(runner, prevRunner, currentStreak + 1, maxStreak);
        }
        else if (currentStreak > maxStreak) {
            return this.rest.longestStreakHelper(this.first, runner, 1, currentStreak);
        }
        else {
            return this.rest.longestStreakHelper(this.first, prevRunner, 1, maxStreak);
        }
    }
}


class ExamplesMayBeInt {

    MaybeInt one = new RealInt(1);
    MaybeInt two = new RealInt(2);
    MaybeInt five = new RealInt(5);
    MaybeInt falseInt = new FalseInt();
    
    ILoInt empty = new MtLoInt();
    ILoInt streakFive = new ConsLoInt(one, new ConsLoInt(one, new ConsLoInt(falseInt,
        new ConsLoInt(five,
            new ConsLoInt(five, 
                new ConsLoInt(five,
                    new ConsLoInt(five, new ConsLoInt(falseInt,
                        new ConsLoInt(two, new ConsLoInt(one, empty))))))))));


    boolean testLongestStreak(Tester t) {
        return t.checkExpect(streakFive.longestStreak(), five);
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
        // Examples nums = new Examples();
        // boolean result = nums.satisfyingStrict.strictSatisfy(); 
        // System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesMayBeInt(), false, false);
    }
}
import tester.*;

// Represents functions of signature A -> R, for some argument type A and
// result type R
interface IFunc<A, R> {
  R apply(A input);
}

interface IFunc2<A1, A2, R> {
    R apply(A1 arg1, A2 arg2);
}
 

interface IPred<T> {
  boolean apply(T t);
}

// generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(IFunc<T, U> f);
  <R> R foldr(IFunc2<T, R, R> func, R base);
  <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup);

}
 
// empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }

  public <R> R foldr(IFunc2<T, R, R> func, R base) {
    return base;
  }

  public <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup) {
    return backup;
  }
}
 
// non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
 
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
 
  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }

  public <R> R foldr(IFunc2<T, R, R> func, R base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }

  public <U> U findSolutionOrElse(IFunc<T, U> convert, IPred<U> pred, U backup) {
    if (pred.apply(convert.apply(this.first))) {
      return convert.apply(this.first);
    } else {
      return this.rest.findSolutionOrElse(convert, pred, backup);
    }
  }
}


class  findSum implements IFunc2<Integer, Integer, Integer> {

    public Integer apply(Integer num1, Integer num2) {
        return num1 + num2;
    }

}

class ExamplesVisitors {

    IList<Integer> nums = new ConsList<Integer>(1, 
        new ConsList<Integer>(2,
            new ConsList<Integer>(3, new MtList<Integer>())));

    


    boolean testMethod(Tester t) {
        return t.checkExpect(nums.foldr(new findSum(), 0), 6);
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
        Tester.runReport(new ExamplesVisitors(), false, false);
    }
}
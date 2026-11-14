import tester.*;
// a json value
interface JSON {
  <T> T accept(JSONVisitor<T> visitor);
}

interface IFunc<A, R> {
  R apply(A input);
}

interface JSONVisitor<T> extends IFunc<JSON, T> {
  T visitJsBlank(JSONBlank js);
  T visitJsNumber(JSONNumber js);
  T visitJsBool(JSONBool js);
  T visitJsString(JSONString js);
}

// generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(IFunc<T, U> f);

}
 
// empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
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




}

// no value
class JSONBlank implements JSON {
  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitJsBlank(this);
  }
}
 
// a number
class JSONNumber implements JSON {
  int number;
 
  JSONNumber(int number) {
    this.number = number;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitJsNumber(this);
  }
}
 
// a boolean
class JSONBool implements JSON {
  boolean bool;
 
  JSONBool(boolean bool) {
    this.bool = bool;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitJsBool(this);
  }
}
 
// a string
class JSONString implements JSON {
  String str;
 
  JSONString(String str) {
    this.str = str;
  }

  public <T> T accept(JSONVisitor<T> visitor) {
    return visitor.visitJsString(this);
  }
}

class  JSONToNumber implements JSONVisitor<Integer> {

  public Integer visitJsBlank(JSONBlank js) {
    return 0;
  }
  public Integer visitJsNumber(JSONNumber js) {
    return js.number;
  }
  public Integer visitJsBool(JSONBool js) {
    if (js.bool) {
      return 1;
    }
    else {
      return 0;
    }
  }
  public Integer visitJsString(JSONString js) {
    return js.str.length();
  }
  public Integer apply(JSON js) {
    return js.accept(this);
  }
}


class ExamplesJSONS {
    IList<JSON> jsons = new ConsList<JSON>(new JSONNumber(5),
                       new ConsList<JSON>(new JSONBlank(),
                       new ConsList<JSON>(new JSONString("Hello"),
                       new ConsList<JSON>(new JSONBool(true), 
                       new ConsList<JSON>(new JSONBool(false), 
                       new ConsList<JSON>(new JSONBlank(), new MtList<JSON>()))))));
    IList<Integer> expectedNumbers = new ConsList<Integer>(5, 
      new ConsList<Integer>(0, 
        new ConsList<Integer>(5,
          new ConsList<Integer>(1,
            new ConsList<Integer>(0, 
              new ConsList<Integer>(0,
                new MtList<Integer>()
              )
            )
          )
        )
      )
    );
    


    boolean testMethod(Tester t) {
      return t.checkExpect(jsons.map(new JSONToNumber()), expectedNumbers);
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
        Tester.runReport(new ExamplesJSONS(), false, false);
    }
}
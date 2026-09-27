import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
                                // and predefined colors (Color.RED, Color.GRAY, etc.)

import javalib.worldcanvas.WorldCanvas;

class MyPosn extends Posn {
 
  // standard constructor
  MyPosn(int x, int y) {
    super(x, y);
  }
 
  // constructor to convert from a Posn to a MyPosn
  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  public MyPosn add(MyPosn p) {
    return new MyPosn(p.x + this.x , p.y + this.y);
  }

  public boolean isOffScreen(int w, int h) {
    return this.x < w && this.y < h;
  }
}


class Circle {
 
  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick
  WorldImage circleImage;
  Circle(MyPosn position, MyPosn velocity) {
    this.position = position;
    this.velocity = velocity;
    this.circleImage = new CircleImage(30, OutlineMode.SOLID, Color.GREEN);
  }
}

class ExamplesSimpleGame {
    WorldImage myImage = new RectangleImage(30, 20, OutlineMode.SOLID, Color.GRAY);
    

   boolean testImages(Tester t) {
         return t.checkExpect(new RectangleImage(30, 20, OutlineMode.SOLID, Color.GRAY),
                       new RectangleImage(30, 20, OutlineMode.SOLID, Color.GRAY));
        } 
    
    boolean testFailure(Tester t) {
        return t.checkExpect(
            new ScaleImageXY(new RectangleImage(60, 40, OutlineMode.SOLID, Color.GRAY), 0.5, 0.25),
            new RectangleImage(30, 15, OutlineMode.SOLID, Color.GRAY));
        } 
    boolean testDrawTree(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(myImage, 250, 250))
        && c.show();
    } 
        // ============================================================
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
        ExamplesSimpleGame trees = new ExamplesSimpleGame();
        trees.testDrawTree(new Tester());   // runs with no watchdog, window can actually open
      //  boolean result = 
        //System.out.println(result);
        Tester.runReport(new ExamplesSimpleGame(), false, false);
    }
}
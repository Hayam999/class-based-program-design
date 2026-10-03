import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
                                // and predefined colors (Color.RED, Color.GRAY, etc.)

import javalib.worldcanvas.WorldCanvas;

interface ILoCircle {
  ILoCircle moveAll();
  ILoCircle removeOffScreen(int w, int h);
  WorldScene placeAll(WorldScene world);
  int length();
}

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
    return this.x < 0 || this.y < 0 || this.x >= w || this.y >= h;
  }

  public int getX() {
    return this.x;
  }
  public int getY() {
    return this.y;
  }
}


class Circle {
 
  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick
  int size;
  Circle(MyPosn position, MyPosn velocity, int size) {
    this.position = position;
    this.velocity = velocity;
    this.size = size;
  }
 Circle(int x, int y) {
  this(new MyPosn(x, y), new MyPosn(1,0), 30);
 }
  public WorldImage draw() {
    return  new CircleImage(this.size, OutlineMode.SOLID, Color.GREEN);
  }

  public Circle move() {
    return new Circle( this.position.add(this.velocity), velocity, this.size);
  }

  public boolean isOffScreen(int w, int h) {
    return this.position.isOffScreen(w, h);
  }

  public WorldScene place(WorldScene world) {
    return world.placeImageXY(this.draw(), this.position.getX(), this.position.getY());
  }
}

class MtLoCircle implements ILoCircle {
  MtLoCircle() {}

  public ILoCircle moveAll() {
    return  this;
  }

  public ILoCircle removeOffScreen(int w, int h) {
    return this;
  }

  public WorldScene placeAll(WorldScene world) {
    return world;
  }

  public int length() {
    return 0;
  }
}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;

  ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }
  public ILoCircle moveAll() {
    return new ConsLoCircle(this.first.move(), this.rest.moveAll());
  }


  public ILoCircle removeOffScreen(int w, int h) {
    if (this.first.isOffScreen(w, h)) {
      return this.rest.removeOffScreen(w, h);
    } else {
      return new ConsLoCircle(this.first, this.rest.removeOffScreen(w, h));
    }
  }

  public WorldScene placeAll(WorldScene world) {
    return this.rest.placeAll(this.first.place(world));
  }

  public int length() {
    return 1 + this.rest.length();
  }
}

class CirclesGame extends World {
  int width;
  int height;
  int circlesToEnd;
  ILoCircle circles;


  CirclesGame(int width, int height, int circlesToEnd, ILoCircle circles) {
    this.width = width;
    this.height = height;
    this.circlesToEnd = circlesToEnd;
    this.circles = circles;
  }

  CirclesGame(int circlesToEnd) {
    this(500, 500, circlesToEnd, new MtLoCircle());
   }

  CirclesGame(ILoCircle circles, int circlesToEnd) {
    this(500, 500, circlesToEnd, circles);
  }
 
  public WorldScene makeScene() {
    return this.circles.placeAll(new WorldScene(width, height));
  }
  public World onTick() {
    ILoCircle movedCircles = this.circles.moveAll();
    ILoCircle newCircles = movedCircles.removeOffScreen(width, height);
    int difference = movedCircles.length() - newCircles.length();
    int newCirclesToEnd = this.circlesToEnd - difference;
    return new CirclesGame(this.width, this.height, newCirclesToEnd, newCircles);

  }  
  public World onMouseClicked(Posn pos) {
    return new CirclesGame(this.circles = new ConsLoCircle(new Circle(pos.x, pos.y), circles), this.circlesToEnd);
  }

  public WorldScene makeAFinalScene() {

    return new WorldScene(width, height).placeImageXY(new TextImage("Out Of Circles", Color.BLUE), width/2, height/2);
  }
  public WorldEnd worldEnds() {
    if (this.circlesToEnd <= 0) {
      return new WorldEnd(true, this.makeAFinalScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}

class ExamplesSimpleGame {
  // ---------- MyPosn data ----------
  MyPosn origin = new MyPosn(0, 0);
  MyPosn p1 = new MyPosn(10, 20);
  MyPosn p2 = new MyPosn(5, -7);
  MyPosn inside = new MyPosn(250, 150);
  MyPosn farRight = new MyPosn(600, 150);
  MyPosn farLeft = new MyPosn(-100, 150);
  MyPosn farBelow = new MyPosn(250, 400);
  MyPosn farAbove = new MyPosn(250, -100);
 
  // ---------- Circle data ----------
  MyPosn vRight = new MyPosn(10, 0);
  MyPosn vDiag = new MyPosn(3, 4);
  MyPosn vStill = new MyPosn(0, 0);
 
  Circle cInside = new Circle(new MyPosn(100, 100), vRight, 10);
  Circle cInside2 = new Circle(new MyPosn(200, 50), vDiag, 15);
  Circle cInside3 = new Circle(new MyPosn(300, 250), vStill, 5);
  Circle cOutRight = new Circle(new MyPosn(700, 100), vRight, 10);
  Circle cOutLeft = new Circle(new MyPosn(-50, 100), vRight, 10);
  Circle cOutBelow = new Circle(new MyPosn(100, 500), vDiag, 10);
  Circle cOutAbove = new Circle(new MyPosn(100, -80), vDiag, 10);
 
  // ---------- Lists ----------
  ILoCircle empty = new MtLoCircle();
  ILoCircle one = new ConsLoCircle(cInside, empty);
  ILoCircle three = new ConsLoCircle(cInside,
      new ConsLoCircle(cInside2, new ConsLoCircle(cInside3, empty)));
  ILoCircle allOut = new ConsLoCircle(cOutRight,
      new ConsLoCircle(cOutLeft, new ConsLoCircle(cOutBelow, empty)));
  ILoCircle mixed = new ConsLoCircle(cOutRight,
      new ConsLoCircle(cInside,
          new ConsLoCircle(cOutLeft,
              new ConsLoCircle(cInside2,
                  new ConsLoCircle(cOutBelow,
                      new ConsLoCircle(cInside3,
                          new ConsLoCircle(cOutAbove, empty)))))));
  ILoCircle mixedCleaned = three;
 
  WorldScene blank = new WorldScene(500, 300);
 
  // boolean testDrawTree(Tester t) {
  //    WorldCanvas c = new WorldCanvas(500, 500);
  //    return c.drawScene(cInside.place(blank)) && c.show();
  //  }  
  // ============================================================
  // MyPosn.add
  // ============================================================
  boolean testPosnAdd(Tester t) {
    return t.checkExpect(p1.add(p2), new MyPosn(15, 13))
        && t.checkExpect(p2.add(p1), new MyPosn(15, 13))
        && t.checkExpect(p1.add(origin), new MyPosn(10, 20))
        && t.checkExpect(origin.add(p1), new MyPosn(10, 20))
        && t.checkExpect(origin.add(origin), new MyPosn(0, 0))
        && t.checkExpect(new MyPosn(-3, -4).add(new MyPosn(-1, -1)), new MyPosn(-4, -5));
  }
 
  // add must not mutate either operand
  boolean testPosnAddNoMutation(Tester t) {
    MyPosn a = new MyPosn(1, 2);
    MyPosn b = new MyPosn(3, 4);
    MyPosn sum = a.add(b);
    return t.checkExpect(a, new MyPosn(1, 2))
        && t.checkExpect(b, new MyPosn(3, 4))
        && t.checkExpect(sum, new MyPosn(4, 6));
  }
 
  // ============================================================
  // MyPosn.isOffScreen (500 x 300)
  // ============================================================
  boolean testPosnIsOffScreen(Tester t) {
    return t.checkExpect(inside.isOffScreen(500, 300), false)
      && t.checkExpect(new MyPosn(1, 1).isOffScreen(500, 300), false)
      && t.checkExpect(new MyPosn(499, 299).isOffScreen(500, 300), false)
      && t.checkExpect(farRight.isOffScreen(500, 300), true)
      && t.checkExpect(farLeft.isOffScreen(500, 300), true)
      && t.checkExpect(farBelow.isOffScreen(500, 300), true)
      && t.checkExpect(farAbove.isOffScreen(500, 300), true)
      && t.checkExpect(new MyPosn(600, 400).isOffScreen(500, 300), true)
      && t.checkExpect(new MyPosn(-1, -1).isOffScreen(500, 300), true);
  }
 
  // ============================================================
  // MyPosn getters
  // ============================================================
  boolean testPosnGetters(Tester t) {
    return t.checkExpect(p1.getX(), 10)
        && t.checkExpect(p1.getY(), 20)
        && t.checkExpect(p2.getY(), -7);
  }
 
  // ============================================================
  // Circle.move
  // ============================================================
  boolean testCircleMove(Tester t) {
    return t.checkExpect(cInside.move(),
        new Circle(new MyPosn(110, 100), vRight, 10))
        && t.checkExpect(cInside2.move(),
            new Circle(new MyPosn(203, 54), vDiag, 15))
        && t.checkExpect(cInside3.move(), cInside3) // zero velocity
        && t.checkExpect(cInside.move().move(),
            new Circle(new MyPosn(120, 100), vRight, 10));
  }
 
  // move must not mutate the original
  boolean testCircleMoveNoMutation(Tester t) {
    Circle c = new Circle(new MyPosn(1, 1), new MyPosn(2, 2), 5);
    Circle moved = c.move();
    return t.checkExpect(c, new Circle(new MyPosn(1, 1), new MyPosn(2, 2), 5))
        && t.checkExpect(moved, new Circle(new MyPosn(3, 3), new MyPosn(2, 2), 5));
  }

  // ============================================================
  // Circle.isOffScreen (delegates to position)
  // ============================================================
  boolean testCircleIsOffScreen(Tester t) {
    return t.checkExpect(cInside.isOffScreen(500, 300), false)
        && t.checkExpect(cInside3.isOffScreen(500, 300), false)
        && t.checkExpect(cOutRight.isOffScreen(500, 300), true)
        && t.checkExpect(cOutLeft.isOffScreen(500, 300), true)
        && t.checkExpect(cOutBelow.isOffScreen(500, 300), true)
        && t.checkExpect(cOutAbove.isOffScreen(500, 300), true)
        // same circle, smaller screen -> now offscreen
        && t.checkExpect(cInside.isOffScreen(50, 50), true);
  }
  // a circle that leaves the screen after enough ticks
  boolean testCircleLeavesAfterTicks(Tester t) {
    Circle c = new Circle(new MyPosn(480, 100), new MyPosn(10, 0), 10);
    return t.checkExpect(c.isOffScreen(500, 300), false)
        && t.checkExpect(c.move().move().move().isOffScreen(500, 300), true);
  }
  
  // ============================================================
  // Circle.draw
  // NOTE: adjust the color below if your circle uses a different one.
  // ============================================================
  boolean testCircleDraw(Tester t) {
    return t.checkExpect(cInside.draw(),
        new CircleImage(10, OutlineMode.SOLID, Color.GREEN))
        && t.checkExpect(cInside2.draw(),
            new CircleImage(15, OutlineMode.SOLID, Color.GREEN));
  }
 
  // ============================================================
  // Circle.place
  // ============================================================
 
{/**
  boolean testCirclePlace(Tester t) {
    return t.checkExpect(cInside.place(blank), blank.placeImageXY(cInside.draw(), 100, 100))
       &&  t.checkExpect(cInside2.place(blank),
           blank.placeImageXY(cInside2.draw(), 200, 50));
     ///  && t.checkExpect(cInside.place(cInside2.place(blank)),
     //     blank.placeImageXY(cInside2.draw(), 200, 50)
     //         .placeImageXY(cInside.draw(), 100, 100));
  } //
   */} 
  // ============================================================
  // ILoCircle.moveAll  (every circle moved, order preserved)
  // ============================================================
  boolean testMoveAll(Tester t) {
    ILoCircle oneMoved = new ConsLoCircle(cInside.move(), empty);
    ILoCircle threeMoved = new ConsLoCircle(cInside.move(),
        new ConsLoCircle(cInside2.move(),
            new ConsLoCircle(cInside3.move(), empty)));

    return t.checkExpect(empty.moveAll(), empty)
        && t.checkExpect(one.moveAll(),oneMoved)
        && t.checkExpect(three.moveAll(), threeMoved)
       && t.checkExpect(three.moveAll().moveAll(),
           new ConsLoCircle(cInside.move().move(),
                new ConsLoCircle(cInside2.move().move(),
                    new ConsLoCircle(cInside3.move().move(), empty))));
  }
  
 
 
  // moveAll must not change the original list
  boolean testMoveAllNoMutation(Tester t) {
    ILoCircle moved = three.moveAll();
    return t.checkExpect(three, new ConsLoCircle(cInside,
        new ConsLoCircle(cInside2, new ConsLoCircle(cInside3, empty))))
        && t.checkExpect(moved,
            new ConsLoCircle(cInside.move(),
                new ConsLoCircle(cInside2.move(),
                    new ConsLoCircle(cInside3.move(), empty))));
  }
 
  // ============================================================
  // ILoCircle.removeOffScreen  (order of survivors preserved)
  // ============================================================
  boolean testRemoveOffScreen(Tester t) {
    return t.checkExpect(empty.removeOffScreen(500, 300), empty)
        && t.checkExpect(one.removeOffScreen(500, 300), one)
        && t.checkExpect(three.removeOffScreen(500, 300), three)
        && t.checkExpect(allOut.removeOffScreen(500, 300), empty)
        && t.checkExpect(mixed.removeOffScreen(500, 300), mixedCleaned)
        && t.checkExpect(new ConsLoCircle(cOutRight, empty).removeOffScreen(500, 300), empty)
        && t.checkExpect(new ConsLoCircle(cOutRight, one).removeOffScreen(500, 300), one)
        && t.checkExpect(new ConsLoCircle(cInside, new ConsLoCircle(cOutRight, empty))
            .removeOffScreen(500, 300), one);
  }
 
  // a smaller screen removes more circles
  boolean testRemoveOffScreenSmallScreen(Tester t) {
    return t.checkExpect(three.removeOffScreen(150, 150),
        new ConsLoCircle(cInside, empty));
  }
 
  // ============================================================
  // ILoCircle.placeAll
  // ============================================================
  boolean testPlaceAll(Tester t) {
    return t.checkExpect(empty.placeAll(blank), blank)
        && t.checkExpect(one.placeAll(blank),
            blank.placeImageXY(cInside.draw(), 100, 100))
        && t.checkExpect(three.placeAll(blank),
            blank.placeImageXY(cInside.draw(), 100, 100)
                .placeImageXY(cInside2.draw(), 200, 50)
                .placeImageXY(cInside3.draw(), 300, 250));
  }

  // ============================================================
  // Combined tick: move, then remove offscreen (what onTick will do)
  // ============================================================
  boolean testMoveThenRemove(Tester t) {
    Circle edge = new Circle(new MyPosn(495, 100), new MyPosn(10, 0), 10);
    Circle stay = new Circle(new MyPosn(100, 100), new MyPosn(1, 1), 10);
    ILoCircle start = new ConsLoCircle(edge, new ConsLoCircle(stay, empty));
    return t.checkExpect(start.moveAll().removeOffScreen(500, 300),
        new ConsLoCircle(stay.move(), empty));
  }

  boolean testBigBang(Tester t) {
    CirclesGame w = new CirclesGame(10);
    int worldWidth = 500;
    int worldHeight = 500;
    double tickRate = 1.0/28.0;
    return w.bigBang(worldWidth, worldHeight, tickRate);
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
        // boolean result = nums.satisfyingStrict.strictSatisfy(); 
          ExamplesSimpleGame game = new ExamplesSimpleGame();
       // WorldScene result = game.cInside.place(game.blank);   // runs with no watchdog, window can actually open
       //  System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        game.testBigBang(new Tester());
      
       Tester.runReport(new ExamplesSimpleGame(), false, false);
    }
}
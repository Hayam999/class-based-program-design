 import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
                                // and predefined colors (Color.RED, Color.GRAY, etc.)
import javalib.worldcanvas.WorldCanvas;

interface ITree { 
  WorldImage draw();
  boolean isDrooping();
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree);
 }
 
class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  public WorldImage draw() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }
  public boolean isDrooping() {
    return false;
  }
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    return this;
  }

}
 
class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  Stem(int length, double theta, ITree tree) {
   this.length = length;
   this.theta = theta;
   this.tree = tree;
  }
  public WorldImage draw() {
    double thetaInRadians = fromDegreesToRadians();
    int x = calculateX(thetaInRadians);
    int y = calculateY(thetaInRadians);

    WorldImage stem = new LineImage(new Posn(x, y), Color.BLACK);

    // TODO connect the stem to the rest of the tree
    return stem;
  }
  public boolean isDrooping() {
    return false;
  }
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    return this;
  }

  // returns the value of this.theta in radians
  private double fromDegreesToRadians() {
    return  Math.round(this.theta * (Math.PI / 180));
  }

  private int calculateX(double thetaInRadians) {
    return (int) Math.round(this.length * Math.cos(thetaInRadians));
  }

  private int calculateY(double thetaInRadians) {
    return (int) Math.round(this.length * Math.sin(thetaInRadians));
  } 
}
 
class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left, ITree right) {
    this.leftLength = leftLength;
    this.rightLength =  rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta =  rightTheta;
    this.left = left;
    this.right = right;
  }

  public WorldImage draw() {
   return new LineImage(new Posn(40, 40), Color.RED); 
  }
  public boolean isDrooping() {
    return false;
  }
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    return this;
  }
}


class ExamplesTree {
  
    ITree redLeaf = new Leaf(15, Color.RED);
    ITree verticalStem = new Stem(30, 90, redLeaf);
    boolean testDrawTree(Tester t) {
      WorldCanvas c = new WorldCanvas(500, 500);
      WorldScene s = new WorldScene(500, 500);
      return c.drawScene(s.placeImageXY(verticalStem.draw(), 250, 250))
          && c.show();
    } 
        //ITree myTree =  new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE))
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
         
        ExamplesTree trees = new ExamplesTree();
        trees.testDrawTree(new Tester());   // runs with no watchdog, window can actually open
        WorldImage result = trees.verticalStem.draw();
        System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)        

        Tester.runReport(new ExamplesTree(), false, false);
    }
}
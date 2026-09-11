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
  double run();
  ITree rotateTree(double rotationDegree);
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
    
    double lr = 90 - (180 - leftTheta);
    double rr = 90 - (180 - rightTheta);
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.rotateTree(lr), otherTree.rotateTree(rr));
  }
  public double run() {
    return this.size;
  }
  public ITree rotateTree(double rotationDegree) {
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
    double x = calculateX(thetaInRadians);
    double y = calculateY(thetaInRadians);
    double halfLenght = length / 2.0;

    double tipX =  (halfLenght * Math.cos(thetaInRadians));
    double tipY =  (halfLenght * Math.sin(thetaInRadians));

    WorldImage stem = new LineImage(new Posn((int) x, (int) y), Color.BLACK);
    WorldImage restOfTree = this.tree.draw();
    
    return new OverlayImage(restOfTree, stem.movePinhole(-tipX, -tipY)).movePinhole(tipX * 2, tipY * 2);
  }
  public boolean isDrooping() {
    if (Math.sin(fromDegreesToRadians()) < 0) {
      return true;
    }
    return this.tree.isDrooping();
  }
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    double lr = 90 - (180 - leftTheta);
    double rr = 90 - (180 - rightTheta);
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.rotateTree(lr), otherTree.rotateTree(rr));
  }
  public ITree rotateTree(double rotationDegree) {
    return new Stem(this.length, this.theta + rotationDegree, this.tree.rotateTree(rotationDegree));
  }

  // returns the value of this.theta in radians
  private double fromDegreesToRadians() {
    return  this.theta * (Math.PI / 180);
  }

  private double calculateX(double thetaInRadians) {
    return  this.length * Math.cos(thetaInRadians);
  }

  private double calculateY(double thetaInRadians) {
    return  this.length * Math.sin(thetaInRadians);
  }
  public double run() {
    return calculateX(fromDegreesToRadians());
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
    ITree leftStem = leftStem();
    ITree rightStem = rightStem();
    return new OverlayImage(rightStem.draw(), leftStem.draw());
  }
  private ITree leftStem() {
    return  new Stem(this.leftLength, this.leftTheta, this.left);
  }
  private ITree rightStem() {
   return new Stem(this.rightLength, this.rightTheta, this.right);
  }


  public boolean isDrooping() {
    if (leftStem().isDrooping()) {
      return true;
    }
    return  rightStem().isDrooping();
  }
  public ITree rotateTree(double rotationDegree) {

    return new Branch(leftLength, rightLength, this.leftTheta + rotationDegree, this.rightTheta + rotationDegree, left.rotateTree(rotationDegree), right.rotateTree(rotationDegree));
  }
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
    double lr = 90 - (180 - leftTheta);
    double rr = 90 - (180 - rightTheta);
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, this.rotateTree(lr), otherTree.rotateTree(rr));

  }
  public double run() {
   ITree leftStem = new Stem(this.leftLength, this.leftTheta, this.left);
   return  leftStem.run();
  }
}


class ExamplesTree {
    // left stems have more than 90 degree angles
    ITree redLeaf = new Leaf(15, Color.RED);
    ITree greenLeaf = new Leaf(15, Color.GREEN);
    ITree blueLeaf = new Leaf(14, Color.BLUE);
    ITree verticalStem = new Stem(120, 100, redLeaf);
    ITree bendToLeftStem = new Stem(30, 135, greenLeaf);
    ITree bendToRightStem = new Stem(30, 45, blueLeaf);
    ITree horizontalStem = new Stem(45, 0, redLeaf);
    ITree leftOfTree1 = new Stem(30, 45, redLeaf);
    ITree rightOfTree1 = new Stem(30, 135, greenLeaf);
    ITree tree1 = new Branch(30, 30, 45, 135, new Leaf(15, Color.BLUE), new Leaf(10, Color.RED));
    ITree tree2 = new Branch(30, 30, 65, 115, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));
    ITree drooping1 = new Branch(45, 35, 270, 120, redLeaf, greenLeaf);
    ITree drooping2 = new Stem(40, 270, redLeaf);
    ITree droopingMinus = new Stem(100, -20, greenLeaf);
    

    ITree growingTree1 = new Stem(40, 90, tree1);
    ITree otherTree1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
    ITree combine1 = tree1.combine(40, 50, 150, 30, tree2);


    boolean testDrawTree(Tester t) {
      WorldCanvas c = new WorldCanvas(500, 500);
      WorldScene s = new WorldScene(500, 500);
      return c.drawScene(s.placeImageXY(combine1.draw(), 250, 250))
          && c.show();
    }

    boolean testIsDrooping(Tester t) {
      return  t.checkExpect(tree1.isDrooping(), false) &&
             t.checkExpect(horizontalStem.isDrooping(), false) &&
             t.checkExpect(drooping1.isDrooping(), true) &&
             t.checkExpect(drooping2.isDrooping(), true) &&
             t.checkExpect(droopingMinus.isDrooping(), true); 
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
        boolean result = trees.drooping2.isDrooping();
        System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)        

        Tester.runReport(new ExamplesTree(), false, false);
    }
}
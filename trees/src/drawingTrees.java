import java.awt.Color;
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
                                // and predefined colors (Color.RED, Color.GRAY, etc.)
import javalib.worldimages.Posn;
import java.awt.Image;
import javalib.worldcanvas.*;
interface ITree { 
	WorldImage draw();
	WorldImage setPinhole(double delta);
	boolean isDrooping();
	ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree);
	ITree combineHelper(double deltaTheta);
}
abstract class ATree implements ITree {
	int length;
	double theta;
	ITree tree;
	ATree(int length, double theta, ITree tree) {
		this.length = length;
		this.theta = theta; 
		this.tree = tree;
				}
	}
 
class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it
  Leaf(int size, Color color) {
	  this.size = size;
	  this.color = color;
  }
 public WorldImage draw() {
	 return new CircleImage(this.size, "solid", this.color);
 		}
 public WorldImage setPinhole(double delta) {
	 return this.draw().movePinhole(size * (Math.cos(Math.toRadians(delta))), size * (Math.sin(Math.toRadians(delta))));
 }
 public boolean isDrooping() {
	 return false;
 }
 public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
	return this;
 }
 	public ITree combineHelper(double deltaTheta) {
 		return this;
 	}
 }
class Stem extends ATree {
	Stem(int length, double theta, ITree tree) {
	super(length, theta, tree);
	}
 public  WorldImage draw() {
	 int x  = (int)  Math.round(this.length * Math.cos(Math.toRadians(theta)));
	 int y  = (int) Math.round(this.length * Math.sin(Math.toRadians(theta)));
	 Posn p = new Posn(x, y);
	 WorldImage line = new LineImage(p, Color.BLACK);
	 WorldImage circ = this.tree.setPinhole(this.theta);
	 return new OverlayImage(line.movePinhole(-1 * (x / 2), -1 * (y / 2)),  circ);
 }
 	public WorldImage setPinhole(double delta) {
 		 int x  = (int)  Math.round(this.length * Math.cos(Math.toRadians(theta)));
 		 int y  = (int) Math.round(this.length * Math.sin(Math.toRadians(theta)));
 		return this.draw().movePinhole(x, y);
 	} 
 public boolean isDrooping() {
	 double n = Math.floor(this.theta / 360);
	 double angle = theta - (360 * n);
	 return (((360 >= angle)  && (angle >= 180)) || ((0 >= angle) && (angle >= -180)));
}
 public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
	 double deltaLeftTheta = leftTheta - 90;
	 double deltaRightTheta = rightTheta - 90;
	 return new Stem(this.length, this.theta + deltaLeftTheta, this.tree.combineHelper(deltaRightTheta));
 }
	public ITree combineHelper(double deltaTheta) {
 		return new Stem(this.length, this.theta + deltaTheta, this.tree)
 	}
}

class Branch implements ITree{
  int leftLength;
  int rightLength;
  double leftTheta;
  double rightTheta;
  ITree leftTree;
  ITree rightTree;
  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree leftTree, ITree rightTree) {
	  this.leftLength = leftLength;
	  this.rightLength = rightLength;
	  this.leftTheta = leftTheta;
	  this.rightTheta = rightTheta;
	  this.leftTree = leftTree;
	  this.rightTree = rightTree;
  }
 public  WorldImage draw() {
	 ITree leftStem = new Stem(this.leftLength, this.leftTheta, this.leftTree);
	 ITree rightStem = new Stem(this.rightLength, this.rightTheta, this.rightTree);
	 WorldImage leftStemImage = leftStem.setPinhole((double) this.leftLength);
	 WorldImage rightStemImage = rightStem.setPinhole((double) this.rightLength);
	 return new OverlayImage(leftStemImage, rightStemImage);
 }
	public WorldImage setPinhole(double theta) {
 		return this.draw();
 	}
public boolean isDrooping() {
	 ITree leftStem = new Stem(this.leftLength, this.leftTheta, this.leftTree);
	 ITree rightStem = new Stem(this.rightLength, this.rightTheta, this.rightTree);
	 return (leftStem.isDrooping() || rightStem.isDrooping());
	 }
public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree otherTree) {
	 double deltaLeftTheta = leftTheta - 90;
	 double deltaRightTheta = rightTheta - 90;
	 ITree twistThis = new Branch(this.leftLength, this.rightLength, this.leftTheta + deltaLeftTheta,
			 this.rightTheta + deltaLeftTheta, this.leftTree.combineHelper(deltaLeftTheta), 
			 this.rightTree.combineHelper(deltaLeftTheta));
	 return new Branch(leftLength, rightLength, leftTheta, rightTheta, twistThis, otherTree.combineHelper(deltaRightTheta));
}
	public ITree combineHelper(double deltaTheta) {
		return new Branch(this.leftLength, this.rightLength, this.leftTheta + deltaTheta,
				 this.rightTheta + deltaTheta, this.leftTree.combineHelper(deltaTheta), 
				 this.rightTree.combineHelper(deltaTheta));
 	}
}
class ExampleTree {
	ITree tree1 = new Branch(30, 30, 135, 40, new Leaf(10, Color.RED), new Leaf(15, Color.BLUE));
	ITree tree2 = new Branch(30, 30, 115, 65, new Leaf(15, Color.GREEN), new Leaf(8, Color.ORANGE));
	ITree treeOnStem1 = new Stem(40, 90, tree1);
	ITree treeOnStem2 = new Stem(50, 90, tree2);
	ITree test = new Branch(40, 50, 150, 30, tree1, tree2);
	ITree combinedTrees = tree1.combine(40, 50, 150, 30, tree2);
	
	boolean testDrawTree(Tester t) {
		  WorldCanvas c = new WorldCanvas(500, 500);
		  WorldScene s = new WorldScene(500, 500);
		  return c.drawScene(s.placeImageXY(combinedTrees.draw(), 250, 250))
		      && c.show();
		} 
	}



import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import javalib.worldcanvas.WorldCanvas;

import java.awt.Color;          // general colors (as triples of red,green,blue values)
import java.util.Random;
                                // and predefined colors (Color.RED, Color.GRAY, etc.)
class MyPosn extends Posn {
	
	  // standard constructor
	  MyPosn(int x, int y) {
	    super(x, y);
	  }
	  
	  // constructor to convert from a Posn to a MyPosn
	  MyPosn(Posn p) {
	    this(p.x, p.y);
	  }
	  
	  MyPosn add(MyPosn p) {
		  return new MyPosn(this.x + p.x, this.y + p.y);
	  }
	  
	  boolean isOffscreen(int width, int height) {
		  // not sure if the y value should be greater than or smaller than the height
		  // assure that the logic is right
		  
		   return ((this.x >= width) || (this.x <= 0) || (this.y <= 0) || (this.y >= height));
	  }
	  }
	

class Circle {
	
	  int radius;
	  OutlineMode fill;
	  Color color;
	  MyPosn position; // in pixels
	  MyPosn velocity; // in pixels/tick
	  
	  Circle(int radius, OutlineMode fill, Color color, MyPosn position, MyPosn velocity) {
		  this.radius = radius;
		  this.fill = fill;
		  this.color = color;
		  this.position = position;
		  this.velocity = velocity;
	  }
	  Circle(MyPosn position, MyPosn velocity) {
		  this(25, OutlineMode.SOLID, Color.RED, position, velocity);
	  }
	  Circle move() {
		  return new Circle(this.radius, this.fill, this.color, 
				  new MyPosn(this.position.x + this.velocity.x ,this.position.y + this.velocity.y), this.velocity);
	  }
	  
	  boolean isOffscreen(int width, int height) {
		  return this.position.isOffscreen(width, height);
	  }
	  
	  WorldImage draw() {
		  return new CircleImage(this.radius, this.fill, this.color);
	  }
	  
	  WorldScene place(WorldScene scene) {
		  return scene.placeImageXY(this.draw(), this.position.x, this.position.y);
	  }
}

class MyWorld extends World {
	
	int numOfCirclesTillEnd;
	int width;
	int height;
	ILoCircle circles;
	Random rand;
	
	MyWorld(int numOfCirclesTillEnd, int width, int height, ILoCircle circles, Random rand) {
		this.numOfCirclesTillEnd = numOfCirclesTillEnd;
		this.width = width;
		this.height = height;
		this.circles = circles;
		this.rand = rand;
	}
	
	MyWorld(int numOfCirclesTillEnd) {
		this(numOfCirclesTillEnd, 1000, 1000, new MtLoCircle(), new Random());
	}
	
	public WorldScene makeScene() {
		WorldScene scene = new WorldScene(this.width, this.height);
		return this.circles.placeAll(scene);
	}
	
	public WorldEnd worldEnds() {
		  if (this.numOfCirclesTillEnd == 0) {
		    return new WorldEnd(true, new WorldScene(this.width, this.height));
		  } else {
		    return new WorldEnd(false, this.makeScene());
		  }
		}
	// rand = new Random
	// rand.nextInt(11);
	public MyWorld onMouseClicked(Posn pos){
		int randX = this.rand.nextInt(900);
		int randY = this.rand.nextInt(900);
		int randVX = this.rand.nextInt(10);
		int randVY = this.rand.nextInt(10);
		ILoCircle newCircles = new ConsLoCircle(new Circle(new MyPosn(randX, randY),
				new MyPosn(randVX, randVY)), this.circles);
		return new MyWorld(this.numOfCirclesTillEnd, this.width, this.height, newCircles, this.rand);
	}
	
	public MyWorld onTick() {
		ILoCircle newCircles = this.circles.moveAll();

		int numOfRemovedCircles = newCircles.removedCircles(0, this.width, this.height);
		int newNumOfCirclesTillEnd = this.numOfCirclesTillEnd - numOfRemovedCircles;
		ILoCircle newCircles2 = newCircles.removeOffscreen(this.width, this.height);
		return new MyWorld(newNumOfCirclesTillEnd, this.width, this.height, newCircles2, this.rand);
		}
}

interface ILoCircle {
	
	ILoCircle moveAll();
	ILoCircle removeOffscreen(int width, int height);
	WorldScene placeAll(WorldScene scene);
	int removedCircles(int acc, int width, int height);
}

class MtLoCircle implements ILoCircle {
	
	MtLoCircle() {};
	public ILoCircle moveAll() {
		return new MtLoCircle();
	}

	
	public ILoCircle removeOffscreen(int width, int height) {
		return new MtLoCircle();
	}
	
	public WorldScene placeAll(WorldScene scene) {
		return scene;
	}
	
	public int removedCircles( int acc, int width, int height) {
		return acc;
	}
}

class ConsLoCircle implements ILoCircle {
	Circle first;
	ILoCircle rest;
	
	ConsLoCircle(Circle first, ILoCircle rest){
		this.first = first;
		this.rest = rest;
		}
	
	public ILoCircle moveAll() {
		return new ConsLoCircle(this.first.move(), this.rest.moveAll());
	}

	
	public ILoCircle removeOffscreen(int width, int height) {
		if (this.first.isOffscreen(width, height)) {
			return this.rest.removeOffscreen(width, height);
		}
		else {
			return new ConsLoCircle(this.first, this.rest.removeOffscreen(width, height));
		}
	}
	
	public WorldScene placeAll(WorldScene scene) {
		return this.rest.placeAll(this.first.place(scene));
	}
	
	public int removedCircles(int acc, int width, int height) {
		if (this.first.isOffscreen(width, height)) {
			return this.rest.removedCircles(acc + 1, width, height);
		}
		else {
			return this.rest.removedCircles(acc, width, height);
		}
		}
	}


class ExamplesGames{

	    boolean testBigBang(Tester t) {
	        MyWorld w = new MyWorld(10);
	        int worldWidth = 1000;
	        int worldHeight = 1000;
	        double tickRate = .01;
	        return w.bigBang(worldWidth, worldHeight, tickRate);
	      }

}
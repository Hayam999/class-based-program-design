import tester.*;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;
import java.util.Random;


class DistanceCalculator {
    public static double distance(double x, double y, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
    }
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
  // add given position to this position
  MyPosn add(MyPosn p) {
	  return new MyPosn(this.x + p.x, this.y + p.y);
  }
  // checks if given Position is out of the screen with given width and height or not
  boolean isOffScreen(int width, int height) {
	  return ((this.x >= width) || (this.x <= 0) || (this.y >= height) || (this.y <= 0));
  }
  /**
   * Determines if the given position is within 10 units of this position.
   * @param p The position to check proximity to
   * @return true if the positions are within 10 units of each other
   */
  public boolean isNear(MyPosn p) {
      double result = DistanceCalculator.distance(this.x, this.y, p.x, p.y);
      return (result <= 10);
  }
  
}

interface IGamePiece {
public MyPosn getPosition();
public int getCircleSize();
public int getExplosionNum();
public IGamePiece move() ;
public WorldImage draw();
public boolean isOffScreen(int width, int height);	
public boolean hitAny(ILoGamePiece otherPieces);
public boolean isNear(IGamePiece piece);
}	  
abstract class GamePiece implements IGamePiece{
MyPosn velocity;
MyPosn position;
GamePiece(MyPosn velocity, MyPosn position) {
	this.velocity = velocity;
	this.position = position;
}
public boolean isOffScreen(int width, int height) {
	return (this.position.isOffScreen(width, height));
}
public boolean hitAny(ILoGamePiece otherPieces) {
	return otherPieces.anyHaveHit(this);
}
public boolean isNear(IGamePiece piece) {
	// check the problem with the piece.position;
	return this.position.isNear(piece.getPosition());
}
public int getExplosionNum() {
	return 0;
}
public int getCircleSize() {
return 0;	
}
}


//////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////

class Ship extends GamePiece{
	Ship(MyPosn position) {
		super(new MyPosn(1, 0), position);
	}
	public WorldImage draw() {
		return new CircleImage(50, OutlineMode.SOLID, Color.GREEN);
	}
	
	public IGamePiece move() {
		return new Ship(this.position.add(this.velocity));
	}
	public MyPosn getPosition() {
		return this.position;
	}
}

class Bullet extends GamePiece{
	int explosionNum;
	int circleSize;
	Bullet(MyPosn position) {
		super(new MyPosn(0, -1), position);
		this.explosionNum = 1;
		this.circleSize = 10;
	}
	Bullet(MyPosn velocity,MyPosn position, int explosionNum, int circleSize) {
		super(velocity, position);
		this.explosionNum = explosionNum;
		this.circleSize = circleSize;
	}
	public IGamePiece move() {
		return new Bullet(this.position.add(this.velocity));
	}
	public WorldImage draw() {
		return new CircleImage(this.circleSize, OutlineMode.SOLID, Color.RED);
	}
	public int getExplosionNum() {
	return this.explosionNum;
	}
	public MyPosn getPosition() {
		return this.position;
	}
	public int getCircleSize() {
	return this.circleSize;	
	}
}

interface ILoGamePiece {
	public ILoGamePiece moveAll();
	public ILoGamePiece removeOffScreen(int width, int height);
	public int countCollideShips(ILoGamePiece bullets, int acc);
	public boolean anyHaveHit(IGamePiece piece);
	public ILoGamePiece spawn(int height, int width);
	public ILoGamePiece handleCollision(ILoGamePiece otherPieces);
}

abstract class MtLoGamePiece implements ILoGamePiece{
	MtLoGamePiece(){};
	public int countCollideShips(ILoGamePiece bullets, int acc) {
		return acc;
	}
	public boolean anyHaveHit(IGamePiece piece) {
		return false;
	}

}
abstract class  ConsLoGamePiece implements ILoGamePiece {
	IGamePiece first;
	ILoGamePiece rest;
	ConsLoGamePiece(IGamePiece first, ILoGamePiece rest) {
		this.first = first;
		this.rest = rest;
	}
	public int countCollideShips(ILoGamePiece bullets, int acc) {
		if (this.first.hitAny(bullets)) {
			return this.rest.countCollideShips(bullets, acc + 1);
		}
		else {
			return this.rest.countCollideShips(bullets, acc);
			} 
	}
	
		public boolean anyHaveHit(IGamePiece piece) {
			if (this.first.isNear(piece)) {
				return true;
			}
			else {
				return this.rest.anyHaveHit(piece);
			}
		}
}
class MtLoShip extends MtLoGamePiece {
	MtLoShip() {};
	public ILoGamePiece spawn(int height, int width) {
		return new ConsLoShip(new Ship(new MyPosn(0, height/ 2)),new MtLoShip());
	}
	public ILoGamePiece moveAll() {
		return new MtLoShip();
	}
	public ILoGamePiece removeOffScreen(int width, int height) {
		return new MtLoShip();
	}
	public ILoGamePiece handleCollision(ILoGamePiece pieces) {
		return new MtLoShip();
	}
}
class ConsLoShip extends ConsLoGamePiece {
	Random rand;
	ConsLoShip(IGamePiece first, ILoGamePiece rest) {
		super(first, rest);
		this.rand = new Random();
	}
	public ILoGamePiece spawn(int height, int width) {
			int shipsNum = this.rand.nextInt(5);
			while (shipsNum == 0) {
				shipsNum = this.rand.nextInt(5);
			}
			ILoGamePiece newShips = this;
			while (shipsNum > 0) {
				int yPos = this.rand.nextInt(height - 50);
				while (yPos < 50) {
					yPos = this.rand.nextInt(height - 50);
				}
				newShips = new ConsLoShip(new Ship(new MyPosn(0, yPos)), newShips);
			}
			return newShips;
	}
	public ILoGamePiece moveAll() {
		return new ConsLoShip (this.first.move(), this.rest.moveAll());
	}
	public ILoGamePiece removeOffScreen(int width, int height) {
		if (this.first.isOffScreen(width, height)) {
		return this.rest.removeOffScreen(width, height); 
		}
		else {
			return new ConsLoShip(this.first, this.rest.removeOffScreen(width, height));
		}
	}
	public ILoGamePiece handleCollision(ILoGamePiece bullets) {
		if (this.first.hitAny(bullets)) {
			return this.rest.handleCollision(bullets);
		}
		else {
			return new ConsLoShip(this.first, this.rest.handleCollision(bullets));
		}}}

class MtLoBullet extends MtLoGamePiece {
	MtLoBullet() {};
	public ILoGamePiece moveAll() {
		return new MtLoBullet();
	}
	public ILoGamePiece removeOffScreen(int width, int height) {
		return new MtLoBullet();
	}
	public ILoGamePiece handleCollision(ILoGamePiece otherPieces) {
		return new MtLoBullet(); 
		}
	public ILoGamePiece spawn(int height, int width) {
		return new ConsLoBullet(new Bullet(new MyPosn(width/2, height - 10)), new MtLoBullet()) ;
	}
}
class ConsLoBullet extends ConsLoGamePiece {

	ConsLoBullet(IGamePiece first, ILoGamePiece rest) {
		super(first, rest);
	}
	public ILoGamePiece moveAll() {
		return new ConsLoBullet(this.first.move(), this.rest.moveAll());
	}
	public ILoGamePiece removeOffScreen(int width, int height) {
		if (this.first.isOffScreen(width, height)) {
		return this.rest.removeOffScreen(width, height); 
		}
		else {
			return new ConsLoBullet(this.first, this.rest.removeOffScreen(width, height));
		}
	}
	public ILoGamePiece handleCollision(ILoGamePiece otherPieces) {
		if (this.first.hitAny(otherPieces)) {
			ILoGamePiece newBullets = null;
			int n = this.first.getExplosionNum();
			int numOfBulletsToProduce= n + 1;
			int newExpNum = n + 1;
			int i = 1;
			int oldCircleSize = this.first.getCircleSize();
			int newCircleSize = oldCircleSize;
			if (oldCircleSize < 50)
			{ newCircleSize = oldCircleSize + 5;};
			MyPosn currentPosition = this.first.getPosition();
			while (i <= numOfBulletsToProduce) {
				MyPosn currentVelocity = calcVelocity(i, n);
				if (newBullets == null) {
					newBullets = new ConsLoBullet(new Bullet(currentVelocity, currentPosition, newExpNum
							,newCircleSize),
							this.rest.handleCollision(otherPieces));
					i++;
		   	}
			else {
				newBullets = new ConsLoBullet (new Bullet(currentVelocity, currentPosition, newExpNum,
						newCircleSize), newBullets);
				i++;
			} 
		} 
			return newBullets; 
		}
		else {
			return new ConsLoBullet(this.first, this.rest.handleCollision(otherPieces));
		}
}
	public MyPosn calcVelocity(int i, int n) {
			double angleInDegrees = i * (360 / (n + 1));
			double theta = Math.toRadians(angleInDegrees);
			int x = (int) Math.cos(theta);
			int y = (int) Math.sin(theta);
			return new MyPosn(x, y);
		}
	public ILoGamePiece spawn(int height, int width) {
		return new ConsLoBullet(new Bullet(new MyPosn(width/2, height -10)), this);
	}

}
class MyGame extends World{
	ILoGamePiece ships;
	ILoGamePiece bullets;
	int destroyedShipsNum;
	int bulletsOnScreen;
	int numOfBullets;
	int WIDTH;
	int HEIGHT;
	int currentTick;
	int finalTick;

	boolean randomText;
	boolean randomCircles;
	boolean welcomeScreen;
	
	MyGame(ILoGamePiece ships, ILoGamePiece bullets, int destroyedShipsNum, int bulletsOnScreen, int numOfBullets,int width, int height,
			int currentTick, int endTick){
		if ( width < 0 || height < 0 || endTick < 2) {
			throw new IllegalArgumentException("Invalid arguments passed to constructor.");
		}
		this.ships = ships;
		this.bullets = bullets;
		this.destroyedShipsNum = 0;
		this.bulletsOnScreen = 0;
		this.numOfBullets = numOfBullets;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.currentTick = currentTick;
		this.finalTick = endTick;
		this.randomText = false;
		this.randomCircles = false;
		this.welcomeScreen = true;
	}
	
	MyGame(ILoGamePiece ships, ILoGamePiece bullets,
			int destroyedShipsNum, int bulletsOnScreen, int numOfBullets, int width, int height, int currentTick, int endTick, boolean randomText, boolean randomCircles){
		if ( width < 0 || height < 0 || endTick < 2) {
			throw new IllegalArgumentException("Invalid arguments passed to constructor.");
		}
		this.ships = ships;
		this.bullets = bullets;
		this.destroyedShipsNum = destroyedShipsNum;
		this.bulletsOnScreen = bulletsOnScreen;
		this.numOfBullets = numOfBullets;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.currentTick = currentTick;
		this.finalTick = endTick;
		this.randomText = randomText;
		this.randomCircles = randomCircles;
		this.welcomeScreen = false;
	}
	MyGame(int numOfBullets) {
		this(new MtLoShip(), new MtLoBullet(),  0, 0, numOfBullets, 1000, 1000, 0, 1000);
	}
	
	
	@Override
	public WorldScene makeScene() {
		//Make a new scene.
		WorldScene scene = new WorldScene(this.WIDTH, this.HEIGHT);
		
		if (this.welcomeScreen ) {
			scene = this.addWelcomeMessage(scene);
		}
		if ( this.randomText ) {
			scene = this.addRandomTextToScene(scene);
		}
		if ( this.randomCircles ) {
			scene = this.addRandomCirclesToScene(scene);
		}
		scene = this.addInfoToScene(scene);	
		return scene;
	}
	
	WorldScene addWelcomeMessage(WorldScene scene) {
		return scene.placeImageXY( new TextImage("Game will start shortly.", Color.green), 250,250);
	}
	
	WorldScene addRandomTextToScene(WorldScene scene) {
		//Generate random coordinates between 0 and this.WIDTH (non inclusive)
		int randX = (new Random()).nextInt(this.WIDTH); 
		int randY = (new Random()).nextInt(this.HEIGHT);
		
		//Create a String displaying the random coordinates
		String randomText = Integer.toString(randX ) + "," + Integer.toString(randY );
		
		//Add it to the scene and return the scene. 
		return scene.placeImageXY( new TextImage(randomText, Color.blue), randX, randY);
	}
	WorldScene addRandomCirclesToScene(WorldScene scene) {
		//Generate random coordinates between 0 and this.WIDTH (non inclusive)
		int randX = (new Random()).nextInt(this.WIDTH); 
		int randY = (new Random()).nextInt(this.HEIGHT);

		//Add a circle to the scene and return the scene. 
		return scene.placeImageXY( new CircleImage(20, OutlineMode.SOLID, Color.green), randX, randY);
	}
	WorldScene addInfoToScene(WorldScene scene) {
		return scene.placeImageXY( new TextImage("Final tick: " + Integer.toString(this.finalTick) + "  Current tick: " + Integer.toString(this.currentTick) 
		+ "Bullets left: " + this.numOfBullets + "Destroyed ships: " + this.destroyedShipsNum, Color.black),100, 20);
	}
	@Override
	//This method gets called every tickrate seconds ( see bellow in example class).
	public MyGame onTick() {
		return this.incrementGameTick().produceShips()
				.removeOffScreenPieces().movePieces().countDestroyedShips().handleCollision();
	}
	public MyGame countDestroyedShips() {
		int newDestroyedShipsNum = this.ships.countCollideShips(this.bullets, 0);
		return new MyGame(this.ships, this.bullets, newDestroyedShipsNum + this.destroyedShipsNum
				, this.bulletsOnScreen,
				this.numOfBullets, this.WIDTH, this.HEIGHT,
				this.currentTick, this.finalTick);	
	}
	
	public MyGame handleCollision() {
		ILoGamePiece newShips = this.ships.handleCollision(this.bullets);
		ILoGamePiece newBullets = this.bullets.handleCollision(this.ships);
		return new MyGame(newShips, newBullets,this.destroyedShipsNum, this.bulletsOnScreen,
				this.numOfBullets, this.WIDTH, this.HEIGHT, this.currentTick, this.finalTick);	
		
	}
	public MyGame movePieces() {
		ILoGamePiece newShips = this.ships.moveAll().spawn(this.HEIGHT, this.WIDTH);
		ILoGamePiece newBullets = this.bullets.moveAll();
		return new MyGame(newShips, newBullets, this.destroyedShipsNum, this.bulletsOnScreen,
				this.numOfBullets, this.WIDTH, this.HEIGHT,
				this.currentTick, this.finalTick);	
	}
	
	public MyGame removeOffScreenPieces() {
	ILoGamePiece newShips = this.ships.removeOffScreen(WIDTH, HEIGHT);
	ILoGamePiece newBullets = this.bullets.removeOffScreen(WIDTH, HEIGHT);
	return new MyGame(newShips, newBullets, this.destroyedShipsNum, this.bulletsOnScreen,
			this.numOfBullets, this.WIDTH, this.HEIGHT,
			this.currentTick, this.finalTick);
	}
	public MyGame produceShips() {
		ILoGamePiece newShips = this.ships.spawn(this.HEIGHT, this.WIDTH);
		return new MyGame(newShips, this.bullets, this.destroyedShipsNum, this.bulletsOnScreen,
				this.numOfBullets, this.WIDTH, this.HEIGHT,
				this.currentTick, this.finalTick);
	}
	public MyGame addRandomText() {
		return new MyGame(this.ships, this.bullets, this.destroyedShipsNum,
				this.bulletsOnScreen, this.numOfBullets, this.WIDTH, this.HEIGHT, this.currentTick, this.finalTick, true, this.randomCircles);
	}
	
	public MyGame addRandomCircles() {
		return new MyGame(this.ships, this.bullets, this.destroyedShipsNum,
				this.bulletsOnScreen, this.numOfBullets, this.WIDTH, this.HEIGHT, this.currentTick, this.finalTick, this.randomText, true);
	}
	
	public MyGame incrementGameTick() {
		return new MyGame(this.ships, this.bullets, this.destroyedShipsNum,
				this.bulletsOnScreen, this.numOfBullets, this.WIDTH, this.HEIGHT, 
				this.currentTick + 1, this.finalTick,this.randomText, this.randomCircles);
	}
	
	public MyGame onKeyEvent(String key) {
		 //did we press the space update the final tick of the game by 10. 
		 if (key.equals(" ") ) {
			 return new MyGame(this.ships, this.bullets.spawn(this.HEIGHT, this.WIDTH), this.destroyedShipsNum,
						this.bulletsOnScreen, this.numOfBullets,
						this.WIDTH, this.HEIGHT, this.currentTick, this.finalTick+10, true, false);
		 }else {
			 return this;
		 }
	}
	
	//Check to see if we need to end the game.
	@Override
    public WorldEnd worldEnds() {
	    if ((this.currentTick == this.finalTick) || ((this.numOfBullets == 0) && (this.bulletsOnScreen == 0)) ){
	      return new WorldEnd(true, this.makeEndScene());
	    } else {
	      return new WorldEnd(false, this.makeEndScene());
	    }
	  }

    
    public WorldScene makeEndScene() {
    	WorldScene endScene = new WorldScene(this.WIDTH, this.HEIGHT);
    	return endScene.placeImageXY( new TextImage("Game Over", Color.red), 250, 250);
    	
    }

}
        
class ExamplesMyWorldProgram {
	int width = 500;
	int height = 500;
	MyPosn ps1 = new MyPosn(10, 10);
	MyPosn ps2 = new MyPosn(2, 2);
	MyPosn offSP1 = new MyPosn(500, 200);
	MyPosn offSP2 = new MyPosn(800, 100);
	MyPosn offSP3 = new MyPosn(0, 20);
	MyPosn offSP4 = new MyPosn(-1, 100);
	MyPosn offSP5 = new MyPosn(200, 500);
	MyPosn offSP6 = new MyPosn(88, 900);
	MyPosn offSP7 = new MyPosn(45, 0);
	MyPosn offSP8 = new MyPosn(22, -20);
	MyPosn onSP1 = new MyPosn(33, 65);
	MyPosn ps3 = new MyPosn(3, 4);
	MyPosn nearPs3 = new MyPosn(7, 7);
	MyPosn ps4 = new MyPosn(22, 98);
	MyPosn ps5 = new MyPosn(33, 10);
	MyPosn pb1 = new MyPosn(50, 50);
	MyPosn pb2 = new MyPosn(98, 39);
	MyPosn pb3 = new MyPosn(102, 38);
	MyPosn pb4 = new MyPosn(93, 43);
	
	Ship ship1 = new Ship(ps1);
	Ship onScreenShip = new Ship(onSP1);
	Ship ship2 = new Ship(ps3);
	Ship nearShip2 = new Ship(nearPs3);
	ILoGamePiece onAOffScreenships = new ConsLoShip(ship1, new ConsLoShip(onScreenShip, 
			new ConsLoShip(ship2, new ConsLoShip(nearShip2, new MtLoShip()))));
	
	
	Bullet offScreenBullet = new Bullet(offSP4);
	Bullet bullet1 = new Bullet(pb1);
	Bullet bullet2 = new Bullet(pb2);
	Bullet bullet3 = new Bullet(pb3);
	Bullet bullet4 = new Bullet(pb4);
	Bullet hitNearShip2 = new Bullet(nearPs3);
	ILoGamePiece onAOffScreenBullets = new ConsLoBullet(offScreenBullet, 
			new ConsLoBullet(bullet1, new ConsLoBullet(bullet2,
					new ConsLoBullet(bullet3, new ConsLoBullet(bullet4, 
							new MtLoBullet())))));
	
	
	
	boolean testMyPosn(Tester t) {
		return t.checkExpect(ps1.add(ps2), new MyPosn(12, 12)) &&
				t.checkExpect(onSP1.isOffScreen(width, height), false) &&
				t.checkExpect(ps1.isOffScreen(width, height), false) &&
				t.checkExpect(offSP1.isOffScreen(width, height), true) &&
				t.checkExpect(offSP2.isOffScreen(width, height), true) &&
				t.checkExpect(offSP3.isOffScreen(width, height), true) &&
				t.checkExpect(offSP4.isOffScreen(width, height), true) &&
				t.checkExpect(offSP5.isOffScreen(width, height), true) &&
				t.checkExpect(offSP6.isOffScreen(width, height), true) &&
				t.checkExpect(offSP7.isOffScreen(width, height), true) &&
				t.checkExpect(offSP8.isOffScreen(width, height), true) &&
				t.checkExpect(ps3.isNear(nearPs3), true) &&
				t.checkExpect(ps4.isNear(ps5), false);
		}
	boolean testGamePiece(Tester t) {
		return t.checkExpect(offScreenBullet.isOffScreen(width, height), true) &&
				t.checkExpect(ship1.isNear(offScreenBullet), false) &&
				t.checkExpect(ship2.isNear(nearShip2), true) &&
				t.checkExpect(hitNearShip2.hitAny(onAOffScreenships), true) &&
				t.checkExpect(ship1.hitAny(onAOffScreenBullets), false);
				
		
	}
	
	boolean testShip(Tester t) {
	
		return t.checkExpect(ship1.move(),new Ship(new MyPosn(11, 10))) &&
			   t.checkExpect(ship1.draw(), 
						new CircleImage(50, OutlineMode.SOLID, Color.GREEN)) &&
			   t.checkExpect(ship1.getPosition(), new MyPosn(10, 10)) &&
			   t.checkExpect(ship1.isOffScreen(10, 10), true) &&
			   t.checkExpect(ship1.isOffScreen(1, 5), true) &&
			   t.checkExpect(ship1.isOffScreen(23, 10), true) &&
			   t.checkExpect(ship1.isOffScreen(-5, 12), true) &&
			   t.checkExpect(ship1.isOffScreen(2, -10), true) &&
			   t.checkExpect(ship1.isOffScreen(-5, 89), true) &&
			   t.checkExpect(onScreenShip.isOffScreen(width, height), false);
	}
	boolean testBullet(Tester t) {
	return t.checkExpect(bullet1.move(), new Bullet(new MyPosn(50, 49))) &&
		   t.checkExpect(bullet1.draw(), new CircleImage(10, OutlineMode.SOLID, Color.RED)) &&
		   t.checkExpect(bullet1.getExplosionNum(), 1) &&
		   t.checkExpect(bullet1.getPosition(), new MyPosn(50, 50)) &&
		   t.checkExpect(bullet1.getCircleSize(), 10);
	}

	//boolean testBigBang(Tester t) {
	//MyGame world = new MyGame(10);
	////width, height, tickrate = 0.5 means every 0.5 seconds the onTick method will get called.
    //return world.bigBang(500, 500, 7.0/28.0);
  //}
}


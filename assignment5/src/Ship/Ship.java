import java.awt.Color;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import java.util.Random;
import tester.*;               

interface ILoShip {
   ILoShip spawn(int rand, int width, int height);
   ILoShip moveAll(int width);
   WorldScene placeAll(WorldScene scene);
}

public interface Ship {
    WorldScene place(WorldScene scene); 
    WorldImage draw();
    Ship move(int width);
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

abstract class AShip implements Ship {
    MyPosn pos;
    MyPosn velocity;
    int size;

    AShip(MyPosn pos, MyPosn velocity, int size) {
        this.pos = pos;
        this.velocity = velocity;
        this.size = size;
    }
     
    public WorldScene place(WorldScene scene) {
        return scene.placeImageXY(this.draw(), this.pos.getX(), this.pos.getY());
    }

    

}

class FightShip extends AShip{

    FightShip(MyPosn pos, MyPosn velocity, int size) {
        super(pos, velocity, size);
    }

    public WorldImage draw() {
        return new CircleImage(this.size, OutlineMode.SOLID, Color.GREEN);
    }

    public Ship move(int widht) {
        int currentX = this.pos.getX();
        int currentY = this.pos.getY();
        MyPosn newPos;
        MyPosn newVelo;
        if (currentX >= widht) {
            newPos = new MyPosn(currentX - 1, currentY);
            newVelo = new MyPosn(-1, 0);
        } else if (currentX <= 0) {
            newPos = new MyPosn(currentX + 1, currentY);
            newVelo = new MyPosn(1, 0);
        } else {
            newPos = new MyPosn(currentX + this.velocity.getX(), currentY);
            newVelo = this.velocity;
        }
        return new FightShip(newPos, newVelo, this.size);
    }
}

class EnemyShip extends AShip {

    EnemyShip(MyPosn pos, MyPosn velocity, int size) {
        super(pos, velocity, size);
    }
    

    public WorldImage draw() {
        return new CircleImage(this.size, OutlineMode.SOLID, Color.RED);
    }

    public Ship move(int widht) {
        int currentX = this.pos.getX();
        int currentY = this.pos.getY();
        MyPosn newPos;
        MyPosn newVelo;
        if (currentX >= widht) {
            newPos = new MyPosn(currentX - 1, currentY);
            newVelo = new MyPosn(-1, 0);
        } else if (currentX <= 0) {
            newPos = new MyPosn(currentX + 1, currentY);
            newVelo = new MyPosn(1, 0);
        } else {
            newPos = new MyPosn(currentX + this.velocity.getX(), currentY);
            newVelo = this.velocity;
        }
        return new EnemyShip(newPos, newVelo, this.size);
    }
}



class MtLoShip implements ILoShip {
    MtLoShip() {}

    public WorldScene placeAll(WorldScene scene) {
        return scene;
    }

    public ILoShip spawn(int rand, int width, int height) {
        return new MtLoShip();
    }

    public ILoShip moveAll(int width) {
        return new MtLoShip();
    }
}

class ConsLoShip implements ILoShip {
    Ship first;
    ILoShip rest;

    ConsLoShip(Ship first, ILoShip rest) {
        this.first = first;
        this.rest = rest;
    } 

    public WorldScene placeAll(WorldScene scene) {
        return this.rest.placeAll(this.first.place(scene));
    }

    public ILoShip spawn(int rand, int width, int height) {
        if (rand <= 0) {
            return this;
        }
        else {
            // TODO refactor this piece of code into a seperated method and test it
            //     as we may be entered an infinite loop;
            int topBoundry = height/6;
            int bottomBoundry = height - topBoundry;
            Random randObj = new Random();
            int y = topBoundry;
    
            while (y <= topBoundry) {
                y = randObj.nextInt(bottomBoundry);
            }
            
            int leftBoundry = width - (width / 6);
            int rightBoundry = width / 6;
            int x = rightBoundry;
            while (x <= leftBoundry) {
                x = randObj.nextInt(rightBoundry);
            }
            
            int xVelocity;
            if (randObj.nextInt(10) <=5 ) {
                xVelocity = -1;
            } else {
                xVelocity = 1;
            }

            MyPosn pos = new MyPosn(x, y);
            MyPosn velo = new MyPosn(xVelocity, 0);     
            Ship newShip = new EnemyShip(pos, velo, 15);
            ILoShip newShips = new ConsLoShip(newShip, this);

            return newShips.spawn(rand - 1, width, height);
        }
    }

    public ILoShip moveAll(int width) {
        return new ConsLoShip(this.first.move(width), this.rest.moveAll(width));
    }
}


class ExamplesShip {
    
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
        //    ExamplesShip ship = new Examplesship();
        // WorldScene result = game.cInside.place(game.blank);   // runs with no watchdog, window can actually open
        //  System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        
      
       Tester.runReport(new ExamplesShip(), false, false);
    }
}

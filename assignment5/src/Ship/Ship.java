import java.awt.Color;

import javalib.funworld.WorldScene;
import javalib.worldimages.*;

interface ILoShip {
   ILoShip spawn(int rand);
   ILoShip moveAll();
   ILoShip removeOffScreen();
   WorldScene placeAll(WorldScene scene);
}

public interface Ship {
    WorldScene place(WorldScene scene); 
    WorldImage draw();
}

abstract class AShip implements Ship {
    MyPosn pos;
    int size;

    AShip(MyPosn pos, int size) {
        this.pos = pos;
        this.size = size;
    }
    
    public WorldScene place(WorldScene scene) {
        return scene.placeImageXY(this.draw(), this.pos.getX(), this.pos.getY());
    }

}

class FightShip extends AShip{

    FightShip(MyPosn pos, int size) {
        super(pos, size);
    }

    public WorldImage draw() {
        return new CircleImage(this.size, OutlineMode.SOLID, Color.GREEN);
    }
}


class EnemyShip extends AShip {

    EnemyShip(MyPosn pos, int size) {
        super(pos, size);
    }

    public WorldImage draw() {
        return new CircleImage(this.size, OutlineMode.SOLID, Color.RED);
    }
}



class MtLoShip implements ILoShip {
    MtLoShip() {}

    public WorldScene placeAll(WorldScene scene) {
        return scene;
    }

    public ILoShip spawn(int rand) {
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

    public ILoShip spawn(int rand) {
        // generate a random number
        // create call spawn helper to create the ships until that number is 0
        // add the new ships to the current ships and return them together

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
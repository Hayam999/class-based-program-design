import java.awt.Color;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;

 interface ILoBullet {
    int length();
    ILoBullet moveAll();
    ILoBullet removeOffScreen(int widht, int height);
    WorldScene placeAll(WorldScene scene);
}
public class Bullet {
    MyPosn pos;
    MyPosn velocity;
    int xplosionDegree;

    Bullet(MyPosn pos, MyPosn velocity, int xplosionDegree) {
        this.pos = pos;
        this.velocity = velocity;
        this.xplosionDegree = xplosionDegree;
    }
    public WorldImage draw() {
        return new EllipseImage(10, 25, OutlineMode.SOLID, Color.RED); 
    }

    public Bullet move() {
        return new Bullet(new MyPosn(this.pos.getX(),
             this.pos.getY() + this.velocity.getY()), this.velocity, this.xplosionDegree);
    }

    public boolean isOffScreen(int width, int height) {
        return this.pos.isOffScreen(width, height);
    }

    public WorldScene place(WorldScene scene) {
        return scene.placeImageXY(this.draw(), this.pos.getX(), this.pos.getY());
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
class MtLoBullet implements ILoBullet {
    MtLoBullet() {}
    
    public int length() {
        return 0;
    }
    
    public ILoBullet moveAll() {
       return new MtLoBullet();
    }

    public ILoBullet removeOffScreen(int width, int height) {
        return new MtLoBullet();
    }

    public WorldScene placeAll(WorldScene scene) {
        return scene;
    }

}

class ConsLoBullet implements ILoBullet {
    Bullet first;
    ILoBullet rest;

    ConsLoBullet(Bullet first, ILoBullet rest) {
        this.first = first;
        this.rest  = rest;
    }

    public int length() {
        return 1 + this.rest.length();
    }
    
    public ILoBullet moveAll() {
       return new ConsLoBullet(this.first.move(), this.rest.moveAll());
    }
    
    public ILoBullet removeOffScreen(int width,int height) {
        if (this.first.isOffScreen(width, height)) {
            return this.rest.removeOffScreen(width, height);
        }
        else {

            return new ConsLoBullet(this.first, this.rest.removeOffScreen(width, height));
        }
    }

    public WorldScene placeAll(WorldScene scene) {
        return this.rest.placeAll(this.first.place(scene));
    }
}
import java.awt.Color;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import tester.*;
// 
 interface ILoBullet {
    int length();
    ILoBullet moveAll();
    ILoBullet removeOffScreen(int widht, int height);
    WorldScene placeAll(WorldScene scene);
    boolean collided(MyPosn pos);
    ILoBullet updateCollision(ILoShip ships);
    ILoBullet replaceCollisionHandler(ILoBullet collidedBullets, ILoShip ships  );
    ILoBullet explode();
}
public class Bullet {
    MyPosn pos;
    MyPosn velocity;
    int xplosionDegree;
    MyPosn oldPos;
    double thetaInRadians;

    Bullet(MyPosn pos, MyPosn velocity, int xplosionDegree,
        MyPosn oldPos, double thetaInRadians) {
        this.pos = pos;
        this.oldPos = oldPos;
        this.velocity = velocity;
        this.xplosionDegree = xplosionDegree;
        this.thetaInRadians = thetaInRadians;
    }

    // overloaded that constructor for making an initial bullet fired from the ship
    Bullet(MyPosn pos, MyPosn velocity) {
        this(pos, velocity, 1, pos, 90.0);
    }
    public WorldImage draw() {
        return new EllipseImage(10, 25, OutlineMode.SOLID, Color.RED); 
    }

    public Bullet move() {

        int newX = (int) Math.round(this.calculateX(this.thetaInRadians));
        int newY = (int) Math.round(this.calculateY(this.thetaInRadians));

        return new Bullet(
            new MyPosn(newX, newY),
             this.velocity, this.xplosionDegree, 
            this.oldPos, this.thetaInRadians);
    }

    public boolean isOffScreen(int width, int height) {
        return this.pos.isOffScreen(width, height);
    }

    public WorldScene place(WorldScene scene) {
        return scene.placeImageXY(this.draw(), this.pos.getX(), this.pos.getY());
    }

    public boolean collided(MyPosn pos) {
        return this.pos.checkCollision(pos);
    }

    public boolean collidsAny(ILoShip ships) {
        return ships.collided(this.pos);
    }

    public Bullet duplicate(int i) {
        int newXpDegree = this.xplosionDegree + 1;
        int fireOffAt = i * (360 / newXpDegree);
        double thetaInRadians = this.fromDegreesToRadians(fireOffAt);
        int xVelocity = -1;
        if (i % 2 == 0) {
            xVelocity = 1;
        }

        return new Bullet(this.pos, new MyPosn(xVelocity, -5), newXpDegree, this.pos, thetaInRadians);
    }

    private double fromDegreesToRadians(int inDegrees) {
    return  inDegrees * (Math.PI / 180);
    }
    private double calculateX(double thetaInRadians) {
     return  (this.velocity.getX() * Math.cos(thetaInRadians)) + this.pos.getX();
    }

    private double calculateY(double thetaInRadians) {
      return  (this.velocity.getY() * Math.round(Math.sin(thetaInRadians))) + this.pos.getY();
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

    public boolean collided(MyPosn pos) {
        return false;
    }

    public ILoBullet updateCollision(ILoShip ships) {
        return new MtLoBullet();
    }


    public ILoBullet replaceCollisionHandler(ILoBullet collidedBullets, ILoShip ships) {
        return collidedBullets.explode();
    }

    public ILoBullet explode() {
        
        return new MtLoBullet();
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
    
    public boolean collided(MyPosn pos) {
        if (this.first.collided(pos)) {
            return true;
        }
        else return this.rest.collided(pos);
    }

    public ILoBullet explode() {
        return this.explodeHandler(this.first);
    }
    
    public ILoBullet explodeHandler(Bullet father) {
        ILoBullet restOfBullets =  this.rest.explode();
        int numOfBulletsToGenerate = father.xplosionDegree + 1;
        int i = 0;
        
        while (numOfBulletsToGenerate > 0) {
            restOfBullets = new ConsLoBullet(father.duplicate(i) , restOfBullets);
            numOfBulletsToGenerate--; 
            i++;
        }
        
        return restOfBullets;

    }


    public ILoBullet updateCollision(ILoShip ships) {
        return replaceCollisionHandler(new MtLoBullet(),  ships);
    }

    public ILoBullet replaceCollisionHandler(ILoBullet collidedBullets, ILoShip ships) {
        if (this.first.collidsAny(ships)) {
            return this.rest.replaceCollisionHandler(
                new ConsLoBullet(this.first, collidedBullets), ships);
        }
        else {
            return new ConsLoBullet(this.first, this.rest.replaceCollisionHandler(collidedBullets, ships));
        }
    }
}


class ExamplesBullet {
    Bullet b1 = new Bullet(new MyPosn(50, 50), new MyPosn(0, -5));
    Bullet b1Moved = new Bullet(new MyPosn(50, 45), new MyPosn(0, -5), 1,
new MyPosn(50, 50), 90.0);

    boolean testMove(Tester t) {
       return t.checkExpect(b1.move(), b1Moved);
    }

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
           ExamplesBullet game = new ExamplesBullet();
           Bullet result = game.b1.move();   // runs with no watchdog, window can actually open
          // System.out.println(result..getX() + " " + result.velocity.getY());
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        // game.testBigBang(new Tester());
        
         Tester.runReport(new ExamplesBullet(), false, false);
    }
}
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
import java.util.Random;



class NBullets extends World {
    int WIDTH = 1500;
    int HEIGHT = 900;
    int FixedReq = 50;
    int spawnRate;
    int destroyedShips;
    int bulletsToEnd;
    ILoBullet bullets;
    ILoShip ships;
    Random rand;
    Ship fighter;

    NBullets(int spawnRate, int destroyedShips, int bulletsToEnd, ILoBullet bullets, ILoShip ships, Ship fighter) {
        this.spawnRate = spawnRate;
        this.destroyedShips = destroyedShips;
        this.bulletsToEnd = bulletsToEnd;
        this.bullets = bullets;
        this.ships = ships;
        this.rand = new Random();
        this.fighter = fighter;
    }

    
    NBullets(int bulletsToEnd, Random rand) {
        Ship fighter = (new FightShip(
            new MyPosn(this.WIDTH/2,  this.HEIGHT - 15), new MyPosn(2, 0), 30));
        this.rand = rand;
        this.destroyedShips = 0;
        this.spawnRate = 0;
        this.bulletsToEnd = bulletsToEnd;
        this.ships = new ConsLoShip(fighter, new MtLoShip());
        this.bullets = new MtLoBullet();
        this.fighter = fighter;
    }
    
    NBullets(int bulletsToEnd) {
        this(bulletsToEnd, new Random());
    }
    public WorldScene makeScene() {
        return this.ships.placeAll(this.bullets.placeAll(this.drawInfo()));
        
    }

    public World onTick() {
        ILoShip newShips = this.spawn().removeCollision(this.bullets).moveAll(WIDTH);
        ILoBullet newBullets = this.bullets.removeOffScreen(WIDTH, HEIGHT).moveAll(); 

        return new NBullets(this.newSpawnRate(), this.destroyedShips,
                 this.bulletsToEnd, newBullets, newShips,this.fighter.move(WIDTH));  
      }  

    public World onKeyEvent(String key) {
        if (key.equals(" ")) {
            return new NBullets(this.spawnRate, this.destroyedShips, 
                this.bulletsToEnd, this.addABullet(), this.ships, this.fighter);
        } else {
            return this;
        }
    }
    
    public ILoBullet addABullet() {
        return new ConsLoBullet(

                new Bullet(new MyPosn(this.fighter.getX(), HEIGHT - 50), new MyPosn(0, -5), 1),
                 this.bullets);

    }

    public ILoShip spawn() {
        if (spawnRate == FixedReq) {
            int randomNum = 0;
            while (randomNum == 0) {   
               randomNum = this.rand.nextInt(6);
            }
            return  this.ships.spawn(randomNum,WIDTH, HEIGHT);
            
        } else {
          return this.ships;
        }
      }  

      public int newSpawnRate() {
        if (spawnRate < FixedReq) {
            return spawnRate + 1;
        }
        else {
            return 0;
        }
      }
    
    // Draws how many bullets are left and how many ships have been destroyed so far.
    public WorldScene drawInfo() {
        int w = WIDTH/10;
        int h = HEIGHT/20;
        WorldImage whiteBox = new RectangleImage(w, h, OutlineMode.OUTLINE, Color.WHITE);
        WorldImage bulletsText = new TextImage("Bullets Remained: " + String.valueOf(this.bulletsToEnd), 25, FontStyle.BOLD, Color.BLACK);
        WorldImage destroyedShipsText = new TextImage("Destroyed Ships: " + String.valueOf(this.destroyedShips), 25, FontStyle.BOLD, Color.BLACK);
        WorldImage textInfo = new AboveImage(destroyedShipsText, bulletsText);
        WorldImage info =  textInfo.overlayImages(whiteBox);


        return new WorldScene(WIDTH, HEIGHT).placeImageXY(info, w, h);
    }
    
    public WorldEnd worldEnds() {
        if (bulletsToEnd <= 0 && bullets.length() <= 0) {
          return new WorldEnd(true, this.makeAFinalScene());
        } else {
          return new WorldEnd(false, this.makeScene());
        }
    }

    public WorldScene makeAFinalScene() {
        return new WorldScene(this.WIDTH, this.HEIGHT).placeImageXY(
            new TextImage("Game End", Color.RED), WIDTH/2, WIDTH/2);
    }

}

class ExamplesNBullets {

   
    boolean testBigBang(Tester t) {
        NBullets w = new NBullets(10);
        int worldWidth = 1500;
        int worldHeight = 900;
        double tickRate = 1.0/28.0;
        return w.bigBang(worldWidth, worldHeight, tickRate);
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
            ExamplesNBullets game = new ExamplesNBullets();
        // WorldScene result = game.cInside.place(game.blank);   // runs with no watchdog, window can actually open
        //  System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
           game.testBigBang(new Tester());
      
       Tester.runReport(new ExamplesNBullets(), false, false);
    }
}

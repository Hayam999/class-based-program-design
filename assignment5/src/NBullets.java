import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
import java.awt.Rectangle;

import javax.print.attribute.standard.NumberUp;

// and predefined colors (Color.RED, Color.GRAY, etc.)
import javalib.worldcanvas.WorldCanvas;



class NBullets extends World {
    int width;
    int height;
    int destroyedShips;
    ILoBullet bullets;
    int bulletsToEnd;
    ILoShip ships;


    NBullets(int bulletsToEnd) {
        this.bulletsToEnd = bulletsToEnd;
        this.ships = new ConsLoShip(new FightShip(), new MtLoShip());
        this.width = 1500;
        this.height = 1500;
        this.destroyedShips = 0;
    }

    public WorldScene makeScene() {
        return this.ships.placeAll(this.drawInfo());
    }

    // Draws how many bullets are left and how many ships have been destroyed so far.
    public WorldScene drawInfo() {
        int w = this.width/10;
        int h = this.height/20;
        WorldImage whiteBox = new RectangleImage(w, h, OutlineMode.OUTLINE, Color.WHITE);
        WorldImage bulletsText = new TextImage("Bullets Remained: " + String.valueOf(this.bulletsToEnd), 25, FontStyle.BOLD, Color.BLACK);
        WorldImage destroyedShipsText = new TextImage("Destroyed Ships: " + String.valueOf(this.destroyedShips), 25, FontStyle.BOLD, Color.BLACK);
        WorldImage textInfo = new AboveImage(destroyedShipsText, bulletsText);
        WorldImage info =  textInfo.overlayImages(whiteBox);


        return new WorldScene(width, height).placeImageXY(info, w, h/2);
    }

    public WorldEnd worldEnds() {
        if (bulletsToEnd <= 0 && bullets.length() <= 0) {
          return new WorldEnd(true, this.makeAFinalScene());
        } else {
          return new WorldEnd(false, this.makeScene());
        }
    }

    public WorldScene makeAFinalScene() {
        return new WorldScene(width, height).placeImageXY(new TextImage("Game End", Color.RED), width/2, width/2);
    }

}

class ExamplesNBullets {

   
    boolean testBigBang(Tester t) {
        NBullets w = new NBullets(10);
        int worldWidth = 1500;
        int worldHeight = 1500;
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

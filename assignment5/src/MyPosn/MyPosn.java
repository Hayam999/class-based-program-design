
import javalib.worldimages.Posn;


class MyPosn extends Posn {
  int COLLISION_DIST = 15; 
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

  public boolean checkCollision(MyPosn pos) {
    return Math.abs(this.x - pos.x) <= COLLISION_DIST &&
           Math.abs(this.y - pos.y) <= COLLISION_DIST; 
  }
}
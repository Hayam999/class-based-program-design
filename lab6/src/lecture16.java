/** interface IList<T> {
// In IList<T>:
<U> IList<U> map(IFunc<T, U> func);

}


interface IShape {
<R> R accept(IVisitorShape<R> visitor);
}


interface IVisitorShape<R> {
  R visitRect(Rect rect);
  R visitCirlce(Circle circle);
  R visitSquare(Square square);
}




class MtList<T> implements IList<T>{

    public <U> IList<U> map(IFunc<T, U> func) {
        return new MtList<U>();
    }
}

class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    public <U> IList<U> map(IFunc<T, U> func) {
        return new ConsList<U>(this.first.beAppliedToBy(func), this.rest.map(func));
    }
}

class Circle implements IShape {
  int x, y;
  int radius;
  String color;
  Circle(int x, int y, int r, String color) {
    this.x = x;
    this.y = y;
    this.radius = r;
    this.color = color;
  }
  public <R> R accept(IVisitorShape<R> visitor) {
    return visitor.visitCirlce(this);
  }

}
class Rect implements IShape {
  int x, y, w, h;
  String color;
  Rect(int x, int y, int w, int h, String color) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.color = color;
  }

  public <R> R accept(IVisitorShape<R> visitor) {
    visitor.visitRect(this);
  }

}

class Square implements IShape {
  int x, y, size;
  String color;
  Square(int x, int y, int size, String color) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.color = color;
  }

  public <R> R accept(IVisitorShape<R> visitor) {
    visitor.visitSquare();
  }
  
}

**/
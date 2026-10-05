import javalib.funworld.WorldScene;

interface ILoShip {
   WorldScene placeAll(WorldScene scene);
}

public interface Ship {    
}
class FightShip implements Ship{   
}

class EnemyShip implements Ship {
    EnemyShip() {}
}

class MtLoShip implements ILoShip {
    MtLoShip() {}

    public WorldScene placeAll(WorldScene scene) {
        return scene;
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
        return scene;
    }
}
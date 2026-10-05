public class Bullet {
    Bullet() {}
}
interface ILoBullet {
    int length();
}

class MtLoBullet implements ILoBullet {
    MtLoBullet() {}
    
    public int length() {
        return 0;
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
}
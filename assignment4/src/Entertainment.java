import tester.*;

interface IEntertainment {
    //compute the total price of this Entertainment
    double totalPrice();
    //computes the minutes of entertainment of this IEntertainment
    int duration();
    //produce a String that shows the name and price of this IEntertainment
    String format();
    //is this IEntertainment the same as that one?
    boolean sameEntertainment(IEntertainment that);
}

class Magazine implements IEntertainment {
    String name;
    double price; //represents price per issue
    String genre;
    int pages;
    int installments; //number of issues per year
    
    Magazine(String name, double price, String genre, int pages, int installments) {
        this.name = name;
        this.price = price;
        this.genre = genre;
        this.pages = pages;
        this.installments = installments;
    }
    
    //computes the price of a yearly subscription to this Magazine
    public double totalPrice() {
        return this.price * this.installments;
    }
    
    //computes the minutes of entertainment of this Magazine, (includes all installments)
    public int duration() {
        return 0;
    }
    
    //is this Magazine the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        return true;
    }
    
    //produce a String that shows the name and price of this Magazine
    public String format() {
        return "";
    }
}

class TVSeries implements IEntertainment {
    String name;
    double price; //represents price per episode
    int installments; //number of episodes of this series
    String corporation;
    
    TVSeries(String name, double price, int installments, String corporation) {
        this.name = name;
        this.price = price;
        this.installments = installments;
        this.corporation = corporation;
    }
    
    //computes the price of a yearly subscription to this TVSeries
    public double totalPrice() {
        return this.price * this.installments;
    }
    
    //computes the minutes of entertainment of this TVSeries
    public int duration() {
        return 0;
    }
    
    //is this TVSeries the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        return true;
    }
    
    //produce a String that shows the name and price of this TVSeries
    public String format() {
        return "";
    }
}

class Podcast implements IEntertainment {
    String name;
    double price; //represents price per issue
    int installments; //number of episodes in this Podcast
    
    Podcast(String name, double price, int installments) {
        this.name = name;
        this.price = price;
        this.installments = installments;
    }
    
    //computes the price of a yearly subscription to this Podcast
    public double totalPrice() {
        return this.price * this.installments;
    }
    
    //computes the minutes of entertainment of this Podcast
    public int duration() {
        return 0;
    }
    
    //is this Podcast the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        return true;
    }
    
    //produce a String that shows the name and price of this Podcast
    public String format() {
        return "";
    }
}

class ExamplesEntertainment {
    IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
    IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
    IEntertainment serial = new Podcast("Serial", 0.0, 8);
    
    //testing total price method
    boolean testTotalPrice(Tester t) {
        return t.checkInexact(this.rollingStone.totalPrice(), 2.55*12, .0001) 
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25*13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001);
    }
    
}
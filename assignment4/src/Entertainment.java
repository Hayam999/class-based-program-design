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
    boolean sameMagazine(Magazine other);
    boolean samePodcast(Podcast other);
    boolean sameTVSeries(TVSeries other);
}

abstract class AnEntertainment implements IEntertainment {
    String name;
    double price;
    int installments;

    AnEntertainment(String name, double price, int installments) {
        this.name = name;
        this.price = price;
        this.installments = installments;
    }

    
    public String format() {
        return this.name + " ," + this.price + ".";
    }
    public double totalPrice() {
        return this.price * this.installments;
    }
    public int duration() {
        return 50 * this.installments;
    }
    public boolean sameMagazine(Magazine other) {
        return false;
    }
    public boolean samePodcast(Podcast other) {
        return false;
    }
    public boolean sameTVSeries(TVSeries other) {
        return false;
    }
}

class Magazine extends AnEntertainment {
    String genre;
    int pages;
    
    Magazine(String name, double price, String genre, int pages, int installments) {
        super(name, price, installments);
        this.genre = genre;
        this.pages = pages;
    }
    
    //computes the minutes of entertainment of this Magazine, (includes all installments)
    public int duration() {
        return 5 * this.pages;
    }
    
    //is this Magazine the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        return that.sameMagazine(this);
    }
    
    public boolean sameMagazine(Magazine other) {
        return this.name == other.name && this.pages == other.pages && this.price == other.price
               && this.genre == other.genre && this.installments == other.installments;
    }
}

class TVSeries extends AnEntertainment{
    String corporation;
    
    TVSeries(String name, double price, int installments, String corporation) {
        super(name, price, installments);
        this.corporation = corporation;
    }
    
    //is this TVSeries the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        return true;
    }
    
    public boolean sameTVSeries(TVSeries other) {
        return this.name == other.name && this.installments == other.installments &&
               this.corporation == other.corporation && this.price == other.price;
    }
}

class Podcast extends AnEntertainment {
    
    Podcast(String name, double price, int installments) {
        super(name, price, installments);
    }
     
    //is this Podcast the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        return that.samePodcast(this);
    }
     
    public boolean samePodcast(Podcast other) {
        return this.name == other.name && this.price == this.price && this.installments == other.installments;
    }
}

class ExamplesEntertainment {
    IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
    IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
    IEntertainment serial = new Podcast("Serial", 0.0, 8);
    IEntertainment micky = new Magazine( "Micky mouse", 5.5, "Cartoon", 130, 8);
    IEntertainment wednesday = new TVSeries("Wednesday", 7, 4, "Netflix");
    IEntertainment roza = new Podcast("Rosa in the street", 70, 20);
    IEntertainment otherSerial = new Podcast("Serial", 0.0, 8);
    //testing total price method
    boolean testTotalPrice(Tester t) {
        return t.checkInexact(this.rollingStone.totalPrice(), 2.55*12, .0001) 
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25*13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001);
    }

    boolean testSameEntertainment(Tester t) {
        return t.checkExpect(roza.sameEntertainment(houseOfCards), false) &&
               t.checkExpect(micky.sameEntertainment(micky), true) &&
               t.checkExpect(serial.sameEntertainment(otherSerial), true);
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
         ExamplesBagel bagel = new ExamplesBagel();
         boolean result = bagel.bagelInOunc1.sameRecipe(bagel.bagelInOunc1);
         System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesBagel(), false, false);
    }
}
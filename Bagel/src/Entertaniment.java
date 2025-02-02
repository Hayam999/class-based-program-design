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
    boolean sameTVSeries(TVSeries other);
    boolean samePodcast(Podcast other);
  
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
	public boolean sameMagazine(Magazine other) {
		return false;
	}
	  public boolean sameTVSeries(TVSeries other) {
		  return false;
	  }
	    public boolean samePodcast(Podcast other) {
	    	return false;
	    }
	    public double totalPrice() {
	        return this.price * this.installments;
	    }  
	    //produce a String that shows the name and price of this Podcast
	    public String format() {
	        return this.name + ", " + String.valueOf(this.price) + ".";
	    }
	    public int duration() {
	        return this.installments * 50;
	    }
}
class Magazine extends AnEntertainment {
    String genre;
    int pages;
    
    Magazine(String name, double price, int installments, String genre,   int pages) {
    	super(name, price, installments);
        this.genre = genre;
        this.pages = pages;
   
    }
    //computes the minutes of entertainment of this Magazine, (includes all installments)
    public int duration() {
        return this.pages * 5;
    }
    public boolean sameEntertainment(IEntertainment other) {
    	return other.sameMagazine(this);
    }

    public boolean sameMagazine(Magazine other) {
    	return this.name.equals(other.name) &&
    			(Math.abs(this.price - other.price) < 0.001) &&
    			this.genre.equals(other.genre) &&
    			this.pages == other.pages &&
    			this.installments == other.installments;
    }
}

class TVSeries extends AnEntertainment {
    String corporation;
    
    TVSeries(String name, double price, int installments, String corporation) {
    	super(name, price, installments);
        this.corporation = corporation;
    }
    //computes the minutes of entertainment of this TVSeries

    //is this TVSeries the same as that IEntertainment?
    public boolean sameEntertainment(IEntertainment that) {
        return that.sameTVSeries(this);
    }
    public boolean sameTVSeries(TVSeries other) {
    	return this.name.equals(other.name) &&
    			(Math.abs(this.price - other.price) < 0.001) &&
    			this.installments == other.installments &&
    			this.corporation.equals(other.corporation);
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
    	return this.name.equals(other.name) &&
    			(Math.abs(this.price - other.price) < 0.0001) &&
    			this.installments == other.installments;
    }
}

class ExamplesEntertainment {
    IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, 12, "Music", 60);
    IEntertainment rol = new Magazine("Rolling Stone", 2.55, 12, "Music", 60);
    IEntertainment times = new Magazine("Times", 3, 7,  "Socience", 70);
    IEntertainment tim = new Magazine("Times", 3, 7,  "Socience", 70);
    IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
    IEntertainment breakingbad = new TVSeries("Breaking Bad", 9, 12, "Netflix");
    IEntertainment bre =  new TVSeries("Breaking Bad", 9, 12, "Netflix");
    IEntertainment serial = new Podcast("Serial", 0.0, 8);
    IEntertainment sama = new Podcast("Sama", 10, 20);
    IEntertainment sam = new Podcast("Sama", 10, 20);
    
    //testing total price method
    boolean testTotalPrice(Tester t) {
        return t.checkInexact(this.rollingStone.totalPrice(), 2.55*12, .0001) 
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25*13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001) &&
        t.checkInexact(sama.totalPrice(), 200.0, 0.0001) &&
        t.checkInexact(breakingbad.totalPrice(), 12.0 * 9, .0001) &&
        t.checkInexact(times.totalPrice(), 3.0 * 7, .0001);
    }
    boolean testSameEntertainment(Tester t) {
    	return t.checkExpect(times.sameEntertainment(tim), true) &&
    			t.checkExpect(breakingbad.sameEntertainment(bre), true) &&
    			t.checkExpect(sama.sameEntertainment(sam), true) &&
    			t.checkExpect(rol.sameEntertainment(times), false) &&
    			t.checkExpect(breakingbad.sameEntertainment(houseOfCards), false) &&
    			t.checkExpect(sama.sameEntertainment(sama), true) &&
    			t.checkExpect(serial.sameEntertainment(sama), false);
    }
}
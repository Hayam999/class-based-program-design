import tester.*;
interface IBook {
	int daysOverdue(int today);
	boolean isOverdue(int day);
	int computeFine(int day);
}

abstract class ABook implements IBook {
	String title;
	int dayTaken;
	ABook(String title, int dayTaken) {
		this.title = title;
		this.dayTaken = dayTaken;
	}
	public int daysOverdue(int today) {
		return 14 - (today - this.dayTaken);
	}
	public boolean isOverdue(int day) {
		return this.daysOverdue(day) <= 0;
	}
	public int computeFine(int day) {
		int daysOverdue = this.daysOverdue(day);
		if (daysOverdue < 0 ) {
			return (daysOverdue * -1) * 10;
		}
		else {
			return 0;
		}
	}
}


class Book extends ABook {
	String author;
	Book(String title, String author, int dayTaken) {
		super(title, dayTaken);
		this.author = author;
}
}
class RefBook extends ABook {
	RefBook(String title, int dayTaken) {
		super(title, dayTaken);
	}
	// @override
	public int daysOverdue(int today) {
		return 2 - (today - this.dayTaken);
	}
	}
class AudioBook extends ABook {
	String author;
	AudioBook(String title, String author, int dayTaken) {
		super(title, dayTaken);
		this.author = author;
	}
	public int computeFine(int day) {
		int daysOverdue = this.daysOverdue(day);
		if (daysOverdue < 0 ) {
			return (daysOverdue * -1) * 20;
		}
		else {
			return 0;
		}
	}
}
class ExamplesBooks {
	IBook harryPotter = new Book("Harry Potter", "J.K.Rowling", 200);
	IBook war = new AudioBook("War of Love", "Sun Zu", 430);
	IBook wiki = new RefBook ("Wikipidia", 584);
	boolean testDaysOverdue(Tester t) {
		return t.checkExpect(harryPotter.daysOverdue(210), 4)
		&& t.checkExpect(war.daysOverdue(432), 12)
		&& t.checkExpect(wiki.daysOverdue(590), -4);
	}
	boolean testIsOverdue(Tester t) {
		return t.checkExpect(harryPotter.isOverdue(210), false) &&
				t.checkExpect(war.isOverdue(600), true) &&
				t.checkExpect(wiki.isOverdue(599), true);
	}
	boolean testComputeFine(Tester t) {
		return t.checkExpect(war.computeFine(432), 0) &&
		t.checkExpect(war.computeFine(447), 60) &&
		t.checkExpect(wiki.computeFine(590), 40);
	} 
}
import tester.*;


interface IBook { 
    public int daysOverdue(int today);
    public boolean isOverdue(int givenDay);
    public double computeFine(int returnDay);
}

abstract class ABook implements IBook{
    String title;
    int dayTaken;

    ABook(String title, int dayTaken) {
        this.title = title;
        this.dayTaken = dayTaken;
    }    
    // regular books & audio books will be overdue in 14 days from dayTaken
    public int daysOverdue(int today) {
            int overdueDay = dayTaken + 14;
            return today - overdueDay;     
    }
    public boolean isOverdue(int givenDay) {
        int overdueDay = dayTaken + 14;
        return givenDay - overdueDay >= 0;
    }
    // the fine is 0.1$ per day for RefBooks and Regular books
    public double computeFine(int returnDay) {
        int daysOverdue = this.daysOverdue(returnDay);
        if (daysOverdue <= 0) {
            return 0.0;
        }
        return daysOverdue * 0.1;
    }
}

class Book extends ABook{
    String author;

    Book(String title, String author, int dayTaken) {
    super(title, dayTaken);
    this.author = author;
    }

}

class AudioBook extends ABook {
    String author;

    AudioBook(String title, String author, int dayTaken) {
        super(title, dayTaken);
        this.author = author;
    }

    public double computeFine(int returnDay) {
        int daysOverdue = this.daysOverdue(returnDay);
        if (daysOverdue <= 0) {
            return 0.0;
        }
        return daysOverdue * 0.2;
    }
     

}

class RefBook extends ABook {
    RefBook(String title, int dayTaken) {
        super(title, dayTaken);
    }

    public int daysOverdue(int today) {
            int overdueDay = dayTaken + 2;
            return today - overdueDay;     
    }

    public boolean isOverdue(int givenDay) {
        int overdueDay = dayTaken + 2;
        return givenDay - overdueDay >= 0;
    }
}



class ExamplesBooks {
    IBook regBook1 = new Book("Thus Spoke Zarathustra", "Friedrich Nietzsche", 10);
    IBook refBook1 = new RefBook("Oxford English Dictionary", 199);
    IBook audioBook1 = new AudioBook("Harry Potter", "J. K. Rowling", 802);
    boolean testDaysOverdue(Tester t) {
        return t.checkExpect(regBook1.daysOverdue(17), -7) &&
               t.checkExpect(regBook1.daysOverdue(26), 2) &&
               t.checkExpect(refBook1.daysOverdue(244), 43) &&
               t.checkExpect(regBook1.daysOverdue(1), -23);
    }
    boolean testIsOverdue(Tester t) {
        return t.checkExpect(regBook1.isOverdue(14), false) && 
               t.checkExpect(regBook1.isOverdue(24), true) &&
               t.checkExpect(regBook1.isOverdue(28), true) && 
               t.checkExpect(refBook1.isOverdue(24), false) &&
               t.checkExpect(refBook1.isOverdue(200), false) &&
               t.checkExpect(refBook1.isOverdue(1934), true);
    }
    boolean testComputeFine(Tester t) {
        return t.checkExpect(regBook1.computeFine(1), 0.0) &&
               t.checkExpect(regBook1.computeFine(54), 3.0) &&
               t.checkExpect(refBook1.computeFine(201), 0.0) &&
               t.checkExpect(audioBook1.computeFine(900), (84 * .2));
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
       //  Examples books = new ExamplesBooks();
       //  boolean result = books.
                 // System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesBooks(), false, false);
    }
}
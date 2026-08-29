import tester.Tester;

interface IDocument {
    boolean sameAuthor(String otherAuthor);
    boolean sameTitle(String otherTitle);
    String getAuthor();
    String getTitle();
}


interface ILoDocument {
    ILoDocument sort();
    
    // Remove any duplicates from the object and only keep one version of the document;
    ILoDocument removeDuplicates();

    // Iterate over a filtered list from repetition and stor only one version from each repeated doc
    ILoDocument removDuplicatesHelper(ILoDocument newListAcc);

    // Remove any doc = currentDoc and produce a new filteredList free from repetition
    ILoDocument filterFromFirst(IDocument currentDoc, ILoDocument filteredList);
}

class Book implements IDocument {
    String author;
    String title;
    String publisher;
    ILoDocument bibliography;

    Book(String author, String title, String publisher, ILoDocument bibliography) {
        this.author = author;
        this.title = title;
        this.publisher = publisher;
        this.bibliography = bibliography;
    }

    public boolean sameAuthor(String otherAuthor) {
        return this.author.equals(otherAuthor);
    }
    public boolean sameTitle(String otherTitle) {
        return this.title.equals(otherTitle);
    }

    public String getAuthor() {
        return this.author;
    }
    public String getTitle() {
        return this.title;
    }
}

class WikiArticle implements IDocument {
    String author;
    String title;
    String url;
    ILoDocument bibliography;

    WikiArticle(String author, String title, String url, ILoDocument bibliography) {
        this.author = author;
        this.title = title;
        this.url = url;
        this.bibliography = bibliography;
    }

    public boolean sameAuthor(String otherAuthor) {
        return this.author.equals(otherAuthor);
    }
    public boolean sameTitle(String otherTitle) {
        return this.title.equals(otherTitle);
    }

    public String getAuthor() {
        return this.author;
    }
    public String getTitle() {
        return this.title;
    }
}

class MtLoDocument implements ILoDocument {
    MtLoDocument() {}

    public ILoDocument sort() {
        return new MtLoDocument();
    }

    public ILoDocument removeDuplicates() {
        return new MtLoDocument();
    }

    public ILoDocument removDuplicatesHelper(ILoDocument newListAcc) {
        return newListAcc;
    }
    public ILoDocument filterFromFirst(IDocument currentDoc, ILoDocument filteredList) {
        return filteredList;
    }
}

class ConsLoDocument implements ILoDocument {
    IDocument first;
    ILoDocument rest;

    ConsLoDocument(IDocument first, ILoDocument rest) {
        this.first = first;
        this.rest = rest;
    }

    public ILoDocument sort() {
        return new MtLoDocument();
    }

    public ILoDocument removeDuplicates() {
        return this.removDuplicatesHelper(new MtLoDocument());
    }

    public ILoDocument removDuplicatesHelper(ILoDocument newListAcc) {
        ILoDocument filteredList = filterFromFirst(this.first, newListAcc);
        newListAcc = new ConsLoDocument(first, newListAcc);
        return filteredList.removDuplicatesHelper(newListAcc);
    }

    public ILoDocument filterFromFirst(IDocument currentDoc, ILoDocument filteredList) {
            if (currentDoc.sameAuthor(this.first.getAuthor()) && (currentDoc.sameTitle(this.first.getTitle()))) {
                return this.rest.filterFromFirst(currentDoc, filteredList);
        } 
        else {
            filteredList = new ConsLoDocument(this.first, filteredList);
            return this.rest.filterFromFirst(currentDoc, filteredList);
        }
    }

}

class ExamplesDocuments {

    // ================= Empty bibliography =================
    ILoDocument emtpyBib = new MtLoDocument();

    // ================= Books =================
    IDocument book1 = new Book("Nagib Mahfouz", "Awlad Haretna", "Dar El-Adab", emtpyBib);
    ILoDocument oneDocBib = new ConsLoDocument(book1, emtpyBib);

    IDocument book2 = new Book("George Orwell", "Nineteen Eighty-Four", "Secker & Warburg", oneDocBib);
    ILoDocument twoDocBib = new ConsLoDocument(book2, oneDocBib);

    IDocument book3 = new Book("George Orwell", "Animal Farm", "Secker & Warburg", emtpyBib);
    IDocument book4 = new Book("Isaac Asimov", "Foundation", "Gnome Press", emtpyBib);
    IDocument book5 = new Book("Agatha Christie", "Murder on the Orient Express", "Collins Crime Club", emtpyBib);
    IDocument book6 = new Book("Jane Austen", "Pride and Prejudice", "T. Egerton, Whitehall", emtpyBib);
    IDocument book7 = new Book("Ursula K. Le Guin", "The Left Hand of Darkness", "Ace Books", emtpyBib);
    IDocument book8 = new Book("J.R.R. Tolkien", "The Hobbit", "George Allen & Unwin", emtpyBib);

    // Same author + same title as book2, but different publisher/object -> counts as a duplicate
    IDocument book2Duplicate = new Book("George Orwell", "Nineteen Eighty-Four", "Penguin Books", emtpyBib);
    // A third copy, to check that removeDuplicates collapses more than one repeat at a time
    IDocument book2Triplicate = new Book("George Orwell", "Nineteen Eighty-Four", "Signet Classics", emtpyBib);

    // Same title as book7, but a different author -> must NOT be treated as a duplicate
    IDocument book9 = new Book("Michael Ende", "The Left Hand of Darkness", "Thienemann Verlag", emtpyBib);

    // ================= Wiki Articles =================
    // Wiki articles are not authoritative themselves, but their bibliographies can still lead
    // (transitively) to real books.
    IDocument article1 = new WikiArticle("Wikipedia Contributors", "Nineteen Eighty-Four",
            "https://en.wikipedia.org/wiki/Nineteen_Eighty-Four", oneDocBib);

    IDocument article2 = new WikiArticle("Wikipedia Contributors", "Animal Farm",
            "https://en.wikipedia.org/wiki/Animal_Farm", new ConsLoDocument(book3, emtpyBib));

    IDocument article3 = new WikiArticle("Jane Doe", "Science Fiction Novels",
            "https://en.wikipedia.org/wiki/Science_fiction",
            new ConsLoDocument(book4, new ConsLoDocument(book2, emtpyBib)));

    IDocument article4 = new WikiArticle("John Smith", "Crime Fiction",
            "https://en.wikipedia.org/wiki/Crime_fiction", new ConsLoDocument(book5, emtpyBib));

    IDocument article5 = new WikiArticle("Anonymous", "Untitled Draft", null, emtpyBib);

    // ================= Bibliography lists for sort() tests =================
    // Unsorted order: Orwell (Animal Farm), Mahfouz, Asimov, Christie, Austen
    ILoDocument unsortedBooksBib = new ConsLoDocument(book3,
            new ConsLoDocument(book1,
                    new ConsLoDocument(book4,
                            new ConsLoDocument(book5,
                                    new ConsLoDocument(book6, emtpyBib)))));

    // Correctly alphabetized by author's last name: Asimov, Austen, Christie, Mahfouz, Orwell
    ILoDocument sortedBooksBib = new ConsLoDocument(book4,
            new ConsLoDocument(book6,
                    new ConsLoDocument(book5,
                            new ConsLoDocument(book1,
                                    new ConsLoDocument(book3, emtpyBib)))));

    // ================= Bibliography lists for removeDuplicates() tests =================
    ILoDocument duplicatesBib = new ConsLoDocument(book2,
            new ConsLoDocument(book2Duplicate,
                    new ConsLoDocument(book1,
                            new ConsLoDocument(book2Triplicate, emtpyBib))));

    ILoDocument dedupedBib = new ConsLoDocument(book2, new ConsLoDocument(book1, emtpyBib));

    ILoDocument noDuplicatesBib = new ConsLoDocument(book7, new ConsLoDocument(book8, emtpyBib));

    ILoDocument sameAuthorDifferentTitleBib = new ConsLoDocument(book2, new ConsLoDocument(book3, emtpyBib));

    ILoDocument sameTitleDifferentAuthorBib = new ConsLoDocument(book7, new ConsLoDocument(book9, emtpyBib));
    /** 
    // ================= sort() tests =================
    boolean testSortEmptyBib(Tester t) {
        return t.checkExpect(this.emtpyBib.sort(), new MtLoDocument());
    }

    boolean testSortSingleBookBib(Tester t) {
        return t.checkExpect(this.oneDocBib.sort(), this.oneDocBib);
    }

    boolean testSortAlreadySortedBib(Tester t) {
        return t.checkExpect(this.sortedBooksBib.sort(), this.sortedBooksBib);
    }

    boolean testSortUnsortedBib(Tester t) {
        return t.checkExpect(this.unsortedBooksBib.sort(), this.sortedBooksBib);
    }
*/
    // ================= removeDuplicates() tests =================
    boolean testRemoveDuplicatesEmptyBib(Tester t) {
        return t.checkExpect(this.emtpyBib.removeDuplicates(), new MtLoDocument());
    }
   // DEBUG that test, the problem is about wether to preserve the order or not
   // so i've created that 2 options
   // OPTION 1: sort everyThing before returning and before testing so that
   // you make sure every thing is sorted, and this is the easy to do 
   // unclean solution as i think
   // OPTION 2: and that option is i guess more educative and will make me 
   // understand the recursion process and the call stack more deepley, which is 
   // to try and return the new list in the same old order, and if you are to do this
   // you should redesing your methods to make this happen + understand the process
   // deeply using the debugre
    // TODO make the dubugger work for that file 
    boolean testRemoveDuplicatesNoDuplicates(Tester t) {
        return t.checkExpect(this.noDuplicatesBib.removeDuplicates(), this.noDuplicatesBib);
    }
/** 
    boolean testRemoveDuplicatesWithDuplicates(Tester t) {
        return t.checkExpect(this.duplicatesBib.removeDuplicates(), this.dedupedBib);
    }

    boolean testSameAuthorDifferentTitleIsNotADuplicate(Tester t) {
        // book2 (Orwell, "Nineteen Eighty-Four") and book3 (Orwell, "Animal Farm") share an
        // author but have different titles, so neither should be removed.
        return t.checkExpect(this.sameAuthorDifferentTitleBib.removeDuplicates(), this.sameAuthorDifferentTitleBib);
    }

    boolean testSameTitleDifferentAuthorIsNotADuplicate(Tester t) {
        // book7 and book9 share a title but have different authors, so neither should be removed.
        return t.checkExpect(this.sameTitleDifferentAuthorBib.removeDuplicates(), this.sameTitleDifferentAuthorBib);
    }

    // ================= Sanity checks on the example data itself =================
    boolean testArticleBibliographyWiring(Tester t) {
        // article1's bibliography should be exactly oneDocBib, showing that a book (book1) is
        // reachable transitively through a wiki article's own bibliography.
        return t.checkExpect(((WikiArticle) this.article1).bibliography, this.oneDocBib);
    }

    boolean testConsLoDocumentFirst(Tester t) {
        return t.checkExpect(((ConsLoDocument) this.twoDocBib).first, this.book2);
    }

    boolean testConsLoDocumentRest(Tester t) {
        return t.checkExpect(((ConsLoDocument) this.twoDocBib).rest, this.oneDocBib);
    }
*/
    public static void main(String[] args) {
        Tester.runReport(new ExamplesDocuments(), false, false);
    }
}
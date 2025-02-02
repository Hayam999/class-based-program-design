import tester.*;
interface IDocument {}
class Book implements IDocument{
	Author author;
	String title;
	IBibLines bibliography;
	String publisher;
	Book(Author author, String title,
			IBibLines bibliography, String publisher) {
		this.author =  author;
		this.title = title;
		this.bibliography = bibliography;
		this.publisher = publisher;
	}
}
class WikiArticle implements IDocument {
	Author author;
	String title;
	IBibLines bibliography;
	String url;
	WikiArticle(Author author, String title,
			IBibLines bibliography, String url) {
	this.author =  author;
	this.title = title;
	this.bibliography = bibliography;
	this.url = url;
	}
}
class Author {
	String first;
	String last;
	Author(String first, String last) {
		this.first = first;
		this.last = last;
	}
}

class BibLine {
	String line;
	Book reference;
	BibLine(String line, Book reference) {
		this.line = line;
		this.reference = reference;
	}
}
interface IBibLines {
	IBibLines insert(BibLine other);
	IBibLines sort();
	IBibLines removeDuplicates();
	IBibLines removeDuplicatesHelper(IBibLines accList);
	boolean match(BibLine element);
}
class Bibliography implements IBibLines{
	IBibLines biblines;
	Bibliography(IBibLines biblines) {
		this.biblines = biblines;
	}
	public IBibLines insert(BibLine other) {
		return this.biblines.insert(other);
	}
	public IBibLines sort(){
		return this.biblines.sort();
	}
	public IBibLines removeDuplicates() {
		return this.biblines.removeDuplicatesHelper(new MTBibliography());
	}
	public IBibLines removeDuplicatesHelper(IBibLines accList) {
		return this.biblines.removeDuplicatesHelper(accList);
	}
	public boolean match(BibLine element) {
		return this.biblines.match(element);
	}
	
}
class MTBibliography implements IBibLines{
	MTBibliography() {};
	public IBibLines insert(BibLine other) {
		return new ConsBibliography(other, new MTBibliography());
	}
	public IBibLines sort() {
		return new MTBibliography();
	}
	public IBibLines removeDuplicates() {
		return new MTBibliography();
	}
	public IBibLines removeDuplicatesHelper(IBibLines accList) {
		return accList;
	}
	public boolean match(BibLine element) {
		return false;
	}
}
class ConsBibliography implements IBibLines{
	BibLine first;
	IBibLines rest;
	ConsBibliography(BibLine first, IBibLines rest) {
		this.first = first;
		this.rest = rest;
	}
		public IBibLines insert(BibLine other) {
			if (this.first.line.charAt(0) < other.line.charAt(0)) {
				return new ConsBibliography(new BibLine(this.first.line , this.first.reference), this.rest.insert(other)); 
				}
				else {
					return new ConsBibliography(new BibLine (other.line, other.reference), this);
				}
			}
		public IBibLines sort() {
			return this.rest.sort().insert(this.first);
		}
		public IBibLines removeDuplicates() {
			if (this.rest.match(this.first)) {
				return removeDuplicatesHelper(new MTBibliography());
			}
			else {
				return removeDuplicatesHelper(new ConsBibliography(new BibLine(this.first.line, this.first.reference),
						new MTBibliography()));
			}
		}
		public IBibLines removeDuplicatesHelper(IBibLines accList) {
			if (this.rest.match(this.first)) {
				return this.rest.removeDuplicatesHelper(accList);
			}
			else {
				return this.rest.removeDuplicatesHelper(accList.insert(this.first));
			}
		}
		public boolean match(BibLine element) {
			return element.line == this.first.line && this.rest.match(element);
		}
		}

class ExamplesDocument {
	Author william = new Author("William", "Shakespeare");
	Author agatha = new Author("Agatha", "Christie");
	Author nageeb = new Author("Nageeb", "Mahfouz");
	Author dan = new Author("Danielle", "Steel");
	IBibLines emptyBib = new Bibliography (new MTBibliography());
	IBibLines oneBookBib = new ConsBibliography(new BibLine("Shakespeare, William. 'Hamlet'.", 
			this.b1), emptyBib);
	IBibLines twoBookBib = new ConsBibliography(new BibLine("Christie, Agatha. 'Whodunits'.", this.b2),
			oneBookBib);
	IBibLines threeBookBib = new ConsBibliography(new BibLine("Mahfouz, Nageeb. 'Fiction'.", this.b3),
			twoBookBib);
	Book b1 = new Book(this.william, "Hamlet", this.emptyBib, "Someone");
	Book b2 = new Book(this.agatha, "Whodunits", oneBookBib, "Adam");
	Book b3 = new Book(this.nageeb, "Fiction", twoBookBib, "Salama");
	Book b4 = new Book(this.dan, "Dream", threeBookBib, "Ahmed");
	boolean test(Tester t) {
		return t.checkExpect(emptyBib.insert(new BibLine("Shakespeare, William. 'Hamlet'.", 
				this.b1)), new ConsBibliography(new BibLine("Shakespeare, William. 'Hamlet'.", 
				this.b1), new MTBibliography())) &&
				t.checkExpect(oneBookBib.insert(new BibLine("Christie, Agatha. 'Whodunits'.", this.b2)),
						new ConsBibliography(new BibLine("Christie, Agatha. 'Whodunits'.", this.b2), oneBookBib))
				&& t.checkExpect(new ConsBibliography
						(new BibLine("Christie, Agatha. 'Whodunits'.", this.b2), 
								new  ConsBibliography (new BibLine("Christie, Agatha. 'Whodunits'.", this.b2),
										new ConsBibliography (new BibLine("Shakespeare, William. 'Hamlet'.", 
												this.b1), new MTBibliography()))).removeDuplicates(), 
								new ConsBibliography
								(new BibLine("Christie, Agatha. 'Whodunits'.", this.b2), 
												new ConsBibliography (new BibLine("Shakespeare, William. 'Hamlet'.", 
														this.b1), new MTBibliography())));
	}
	}
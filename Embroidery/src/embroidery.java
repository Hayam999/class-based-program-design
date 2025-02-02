import tester.*;
class EmbroideryPiece {
	String name;
	IMotif motif;
	
	EmbroideryPiece(String name, IMotif motif) {
		this.name = name;
		this.motif = motif;
		// Computes average difficulty of all stitches in the piece		
	}
	double averageDifficulty() {
		return this.motif.averageDifficultyHelper();
	}
	String embroideryInfo() {
		return this.name +":" + this.motif.nameAndType((int) this.motif.countNums()) + ".";
	}
}

interface IMotif {
	double averageDifficultyHelper();
	double sumNums();
	double countNums();
	String nameAndType(int last);
}

class CrossStitchMotif implements IMotif {
	String description;
	Double difficulty;
	
	CrossStitchMotif(String description, Double difficulty) {
		this.description = description;
		this.difficulty = difficulty;
	}
	public double averageDifficultyHelper() {
		return this.difficulty;
	}
	public double sumNums() {
		return this.difficulty;
	}
	public double countNums() {
		return 1;
	}
	public String nameAndType(int last) {
		if (last == 1) {
			return " " + this.description + " (cross stitch)";
		}
		else {
			return " " + this.description + " (cross stitch),";
		}
	}
}
class ChainStitchMotif implements IMotif {
String description;
double difficulty;
ChainStitchMotif(String description, double difficulty) {
	this.description = description;
	this.difficulty = difficulty;
}
public double averageDifficultyHelper() {
	return this.difficulty;
}
public double sumNums() {
	return this.difficulty;
}
public double countNums() {
	return 1;
}
public String nameAndType(int last) {
	if (last == 1) {
		return " " + this.description + " (chain stitch)" ;
	}
	else {
		return " " + this.description + " (chain stitch),";
	}
}
}
class GroupMotif implements IMotif {
	String description;
	ILoMotifs motifs;
	GroupMotif(String description, ILoMotifs motifs) {
		this.description = description;
		this.motifs = motifs;
	}
	public double averageDifficultyHelper() {
		return this.motifs.sumNumOfList() / this.motifs.countNumOfList();
	}
	public double sumNums() {
		return this.motifs.sumNumOfList();
	}
	public double countNums() {
		return this.motifs.countNumOfList();
	}
	public String nameAndType(int last) {
		return this.motifs.nameAndTypeList(last);
	}
}
interface ILoMotifs {
	double sumNumOfList();
	double countNumOfList();
	String nameAndTypeList(int last);
}

class MtLoMotifs implements ILoMotifs{
	MtLoMotifs() {}
    public double sumNumOfList() {
    	return 0;
    }
    public double countNumOfList() {
    	return 0;
    }
    public String nameAndTypeList(int last) {
    	return "";
    }
}
class ConsLoMotifs implements ILoMotifs {
	IMotif first;
	ILoMotifs rest;
	ConsLoMotifs(IMotif first, ILoMotifs rest) {
		this.first = first;
		this.rest = rest;
	}
	public double sumNumOfList() {
		 return this.first.sumNums() + this.rest.sumNumOfList();
	}
	public double countNumOfList() {
		return this.first.countNums() + this.rest.countNumOfList();
	}
	   public String nameAndTypeList(int last) {
	    		return this.first.nameAndType(last) + this.rest.nameAndTypeList(last - 1);
}
}

class ExamplesEmbroidery {
	IMotif bird = new CrossStitchMotif("bird", 4.5);
	IMotif tree = new ChainStitchMotif("tree", 3.0);
	IMotif rose = new CrossStitchMotif("rose", 5.0);
	IMotif poppy = new ChainStitchMotif("poppy", 4.75);
	IMotif daisy = new CrossStitchMotif("daisy", 3.2);
	ILoMotifs flowers = new ConsLoMotifs(rose,
			new ConsLoMotifs(poppy,
			new ConsLoMotifs(daisy, new MtLoMotifs())));
	IMotif nature = new GroupMotif("nature",
			new ConsLoMotifs(bird, (new ConsLoMotifs (tree,
					flowers))));
	EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover",
			nature);
	double sum = nature.sumNums();
	double count = nature.countNums();
	boolean test(Tester t) {
		return t.checkExpect(pillowCover.averageDifficulty(),  sum / count) &&
				t.checkExpect(pillowCover.embroideryInfo(),
						"Pillow Cover: bird (cross stitch), tree (chain stitch), rose (cross stitch), poppy (chain stitch), daisy (cross stitch).");
	}

}
import tester.Tester;


interface IMotif {
    double averageDifficulty();
    double sumOfDifficulties();
    int numOfStitches();
    String embroideryInfo();
   
}

interface ILoMotif {
    double averageDifficulty(); 
    double averageDifficultyHelper(double sumOfPreDifficulties , int numOfPreStitches);
    double sumOfDifficulties();
    double sumOfDifficultiesHelper(double sumOfPreDifficulties);
    int numOfStitches();
    int numOfStitchesHelper(int numOfPreStitches); 
    String embroideryInfo();  
    String embroideryInfoHelper();
}

class CrossStitchMotif implements IMotif{
    String description;
    double difficulty;

    CrossStitchMotif(String description, double difficulty) {
        this.description = description;
        this.difficulty = difficulty;
    }

    public double averageDifficulty() {
        return this.difficulty;
    }

    public double sumOfDifficulties() {
        return this.difficulty;
    }

    public int numOfStitches() {
        return 1;
    }
    public String embroideryInfo() {
        return this.description + " (cross stitch)";
    }

  
}

class ChainStitchMotif implements IMotif {
    String description;
    double difficulty;

    ChainStitchMotif(String description, double difficulty) {
        this.description = description;
        this.difficulty = difficulty;
    }

    public double averageDifficulty() {
        return this.difficulty;
    }

    public double sumOfDifficulties() {
        return this.difficulty;
    }

    public int numOfStitches() {
            return 1;
        }

    public String embroideryInfo() {
        return   this.description + " (chain stitch)";
    }
   
}

class GroupMotif implements IMotif {
    String description;
    ILoMotif motifs;

    GroupMotif(String description, ILoMotif motifs) {
        this.description = description;
        this.motifs = motifs;
    }

    public double averageDifficulty() {
        return this.motifs.averageDifficulty();
    } 
    
    public double sumOfDifficulties() {
        return this.motifs.sumOfDifficulties();
    }
    
    public int numOfStitches() {
            return this.motifs.numOfStitches();
        }
    
    public String embroideryInfo() {
        return this.motifs.embroideryInfo();
    }
  
}

class MtLoMotif implements ILoMotif{
    MtLoMotif() {}

    public double averageDifficulty() {
        return 0;
    } 
    public double averageDifficultyHelper(double sumOfPreDifficulties, int numOfPreStitches) {
        return sumOfPreDifficulties / numOfPreStitches;
    }
    public double sumOfDifficulties() {
        return 0;
    }
    public int numOfStitches() {
        return 0;
    }
    public double sumOfDifficultiesHelper(double sumOfPreDifficulties) {
        return sumOfPreDifficulties;
    }
    public int numOfStitchesHelper(int numOfPreStitches) {
        return numOfPreStitches;
    }

     public String embroideryInfo() {
          return  "";

    }

     public String embroideryInfoHelper() {
          return  "";

    }
}

class ConsLoMotif implements ILoMotif {
    IMotif first;
    ILoMotif rest;

    ConsLoMotif(IMotif first, ILoMotif rest) {
        this.first = first;
        this.rest = rest;
    }

    public double averageDifficulty() {
        // first motif meight be a group of stitches or only one stithc,
        // in both cases i need to only need to count them and sum their difficulties
        double sumOfFirstDifficulties = this.first.sumOfDifficulties();
        int numOfFirstStitches = this.first.numOfStitches();

        return this.rest.averageDifficultyHelper(sumOfFirstDifficulties, numOfFirstStitches);

    }

    public double averageDifficultyHelper(double sumOfPreDifficulties, int numOfPreStitches) {
         sumOfPreDifficulties = this.first.sumOfDifficulties() + sumOfPreDifficulties;
         numOfPreStitches = this.first.numOfStitches() + numOfPreStitches;

        return this.rest.averageDifficultyHelper(sumOfPreDifficulties, numOfPreStitches);

    }


    public double sumOfDifficulties() {
        double sumOfFirstDifficulties = this.first.sumOfDifficulties();
        return this.rest.sumOfDifficultiesHelper(sumOfFirstDifficulties);
    };

    public int numOfStitches() {
        int numOfFirstStitches = this.first.numOfStitches();
        return this.rest.numOfStitchesHelper(numOfFirstStitches);
    }
    
    public double sumOfDifficultiesHelper(double sumOfPreDifficulties) {
        sumOfPreDifficulties = this.first.sumOfDifficulties() + sumOfPreDifficulties;
        return this.rest.sumOfDifficultiesHelper(sumOfPreDifficulties);
    };

    public int numOfStitchesHelper(int numOfPreStitches) {
        numOfPreStitches = this.first.numOfStitches() + numOfPreStitches;
        return this.rest.numOfStitchesHelper(numOfPreStitches);
    }

    public String embroideryInfo() {
        return this.first.embroideryInfo() + this.rest.embroideryInfoHelper();
    }
    public String embroideryInfoHelper() {
        return ", " +  this.first.embroideryInfo() + this.rest.embroideryInfoHelper();

    }

    
}

class EmbroideryPiece {
    String name;
    IMotif motif;

    EmbroideryPiece(String name, IMotif motif) {
        this.name = name;
        this.motif = motif;
    }

    // computes the average difficulty of all stitches in the piece;
    public double averageDifficulty() {
        return this.motif.averageDifficulty();
    }

    // produces one string that has in it all names of motifs in the piece
    public String embroideryInfo() {

        return this.name + ": " + this.motif.embroideryInfo() + ".";
    }
} 


class ExamplesEmbroidery {
    IMotif bird = new CrossStitchMotif("bird", 4.5);
    IMotif tree = new ChainStitchMotif("tree", 3.0);
    IMotif rose = new CrossStitchMotif("rose", 5.0);
    IMotif poppy = new ChainStitchMotif("puppy", 4.75);
    IMotif daisy = new CrossStitchMotif("daisy", 3.2);

    ILoMotif loFlowerMotifs = new ConsLoMotif(rose, new ConsLoMotif(poppy, new ConsLoMotif(daisy, new MtLoMotif())));
    IMotif flowers = new GroupMotif("flowers", loFlowerMotifs);

    ILoMotif loNatureMotifs = new ConsLoMotif(bird, new ConsLoMotif(tree, loFlowerMotifs));
    IMotif nature = new GroupMotif("nature", loNatureMotifs);


    ILoMotif mtLo = new MtLoMotif();
    ILoMotif oneMotifList = new ConsLoMotif(bird, new MtLoMotif());
    IMotif emptyGroup = new GroupMotif("empty", new MtLoMotif());

    EmbroideryPiece singleMotifPiece = new EmbroideryPiece("Coaster", new CrossStitchMotif("star", 2.0));
 
    ILoMotif twoMotifList = new ConsLoMotif(new ChainStitchMotif("wave", 2.5),
                                new ConsLoMotif(new CrossStitchMotif("sun", 3.5), new MtLoMotif()));
    IMotif twoMotifGroup = new GroupMotif("two motifs", twoMotifList);
    EmbroideryPiece twoMotifPiece = new EmbroideryPiece("Bookmark", twoMotifGroup);
 
 
    EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", nature);


    boolean testSingleMotifAverageDifficulty(Tester t) {
        return t.checkInexact(this.bird.averageDifficulty(), 4.5, 0.001)
            && t.checkInexact(this.rose.averageDifficulty(), 5.0, 0.001)
            && t.checkInexact(this.poppy.averageDifficulty(), 4.75, 0.001);
    }
 
    boolean testSingleMotifSumOfDifficulties(Tester t) {
        return t.checkInexact(this.tree.sumOfDifficulties(), 3.0, 0.001)
            && t.checkInexact(this.daisy.sumOfDifficulties(), 3.2, 0.001)
            && t.checkInexact(this.poppy.sumOfDifficulties(), 4.75, 0.001);
    }
 
    boolean testSingleMotifNumOfStitches(Tester t) {
        return t.checkExpect(this.bird.numOfStitches(), 1)
            && t.checkExpect(this.tree.numOfStitches(), 1)
            && t.checkExpect(this.poppy.numOfStitches(), 1);
    }
 
    // ---------------------------------------------------------------
    // Tests for MtLoMotif (the empty list of motifs)
    // ---------------------------------------------------------------
 
    boolean testMtLoMotifBaseCases(Tester t) {
        return t.checkInexact(this.mtLo.averageDifficulty(), 0.0, 0.001)
            && t.checkInexact(this.mtLo.sumOfDifficulties(), 0.0, 0.001)
            && t.checkExpect(this.mtLo.numOfStitches(), 0);
    }
 
    boolean testMtLoMotifHelpers(Tester t) {
        return t.checkInexact(this.mtLo.averageDifficultyHelper(20.45, 5), 4.09, 0.001)
            && t.checkInexact(this.mtLo.sumOfDifficultiesHelper(12.95), 12.95, 0.001)
            && t.checkExpect(this.mtLo.numOfStitchesHelper(3), 3);
    }
 
    // ---------------------------------------------------------------
    // Tests for ConsLoMotif with exactly one motif in the list
    // ---------------------------------------------------------------
 
    boolean testConsLoMotifSingleElement(Tester t) {
        return t.checkInexact(this.oneMotifList.averageDifficulty(), 4.5, 0.001)
            && t.checkInexact(this.oneMotifList.sumOfDifficulties(), 4.5, 0.001)
            && t.checkExpect(this.oneMotifList.numOfStitches(), 1);
    }
 
    // ---------------------------------------------------------------
    // Tests for ConsLoMotif with several motifs
    // loFlowerMotifs = [rose(5.0), poppy(4.75), daisy(3.2)]
    // loNatureMotifs = [bird(4.5), tree(3.0), rose(5.0), poppy(4.75), daisy(3.2)]
    // ---------------------------------------------------------------
 
    boolean testConsLoMotifAverageDifficulty(Tester t) {
        return t.checkInexact(this.loFlowerMotifs.averageDifficulty(), 4.316667, 0.001)
            && t.checkInexact(this.loNatureMotifs.averageDifficulty(), 4.09, 0.001);
    }
 
    boolean testConsLoMotifSumOfDifficulties(Tester t) {
        return t.checkInexact(this.loFlowerMotifs.sumOfDifficulties(), 12.95, 0.001)
            && t.checkInexact(this.loNatureMotifs.sumOfDifficulties(), 20.45, 0.001);
    }
 
    boolean testConsLoMotifNumOfStitches(Tester t) {
        return t.checkExpect(this.loFlowerMotifs.numOfStitches(), 3)
            && t.checkExpect(this.loNatureMotifs.numOfStitches(), 5);
    }
 
    // ---------------------------------------------------------------
    // Tests for GroupMotif (delegates straight to its ILoMotif)
    // ---------------------------------------------------------------
 
    boolean testGroupMotifAverageDifficulty(Tester t) {
        return t.checkInexact(this.flowers.averageDifficulty(), 4.316667, 0.001)
            && t.checkInexact(this.nature.averageDifficulty(), 4.09, 0.001);
    }
 
    boolean testGroupMotifSumOfDifficulties(Tester t) {
        return t.checkInexact(this.flowers.sumOfDifficulties(), 12.95, 0.001)
            && t.checkInexact(this.nature.sumOfDifficulties(), 20.45, 0.001);
    }
 
    boolean testGroupMotifNumOfStitches(Tester t) {
        return t.checkExpect(this.flowers.numOfStitches(), 3)
            && t.checkExpect(this.nature.numOfStitches(), 5);
    }
 
    // Edge case: a GroupMotif built on an empty list of motifs should not
    // blow up (e.g. divide by zero) and should behave like the empty case.
    boolean testGroupMotifEmpty(Tester t) {
        return t.checkInexact(this.emptyGroup.averageDifficulty(), 0.0, 0.001)
            && t.checkInexact(this.emptyGroup.sumOfDifficulties(), 0.0, 0.001)
            && t.checkExpect(this.emptyGroup.numOfStitches(), 0);
    }
 
    // ---------------------------------------------------------------
    // Tests for EmbroideryPiece
    // ---------------------------------------------------------------
 
    boolean testEmbroideryPieceAverageDifficulty(Tester t) {
        return t.checkInexact(this.pillowCover.averageDifficulty(), 4.09, 0.001);
    }
    
      boolean testEmbroideryInfoPillowCover(Tester t) {
        return t.checkExpect(this.pillowCover.embroideryInfo(),
            "Pillow Cover: bird (cross stitch), tree (chain stitch), rose (cross stitch), "
            + "puppy (chain stitch), daisy (cross stitch).");
    }
 
    //Single leaf motif, no group/list involved at all — checks the
    // no-comma, single-entry case.
    boolean testEmbroideryInfoSingleMotif(Tester t) {
        return t.checkExpect(this.singleMotifPiece.embroideryInfo(),
            "Coaster: star (cross stitch).");
    }
 
    // Exactly two motifs, chain stitch listed first — checks comma
    // placement and that stitch-type labeling isn't order-dependent.
    boolean testEmbroideryInfoTwoMotifs(Tester t) {
        return t.checkExpect(this.twoMotifPiece.embroideryInfo(),
            "Bookmark: wave (chain stitch), sun (cross stitch).");
    }


public static void main(String[] args) {
    Tester.runReport(new ExamplesEmbroidery(), false, false);
  }
}
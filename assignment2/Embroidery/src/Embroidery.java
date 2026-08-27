interface IMotif {

}

interface ILoMotif {

}

class CrossStitchMotif implements IMotif{
    String description;
    double difficulty;

    CrossStitchMotif(String description, double difficulty) {
        this.description = description;
        this.difficulty = difficulty;
    }

}

class ChainStitchMotif implements IMotif {
    String description;
    double difficulty;

    ChainStitchMotif(String description, double difficulty) {
        this.description = description;
        this.difficulty = difficulty;
    }
}

class GroupMotif implements IMotif {
    String description;
    ILoMotif motifs;

    GroupMotif(String description, ILoMotif motifs) {
        this.description = description;
        this.motifs = motifs;
    }
}

class MtLoMotif implements ILoMotif{
    MtLoMotif() {}
}

class ConsLoMotif implements ILoMotif {
    IMotif first;
    ILoMotif rest;

    ConsLoMotif(IMotif first, ILoMotif rest) {
        this.first = first;
        this.rest = rest;
    }
}

class EmbroideryPiece {
    String name;
    IMotif motif;

    EmbroideryPiece(String name, IMotif motif) {
        this.name = name;
        this.motif = motif;
    }
} 

// TODO create EamplesEmbroidery class 

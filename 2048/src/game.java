import tester.*;

interface IGamePiece {
	int getValue();
	IGamePiece merge(IGamePiece with);
	boolean isValid();
}

class BaseTile implements IGamePiece {
	int value;
	BaseTile(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
	public IGamePiece merge(IGamePiece with) {
		return new MergeTile(this, with);
	}
	public boolean isValid() {
		return true;
	}
}

class MergeTile implements IGamePiece {
	IGamePiece piece1;
	IGamePiece piece2;
	MergeTile(IGamePiece piece1, IGamePiece piece2) {
		this.piece1 = piece1;
		this.piece2 = piece2;
	}
	public int getValue() {
		return this.piece1.getValue() + this.piece2.getValue();
	}
	public IGamePiece merge(IGamePiece with) {
		return new MergeTile(this, with);
	}
	public boolean isValid() {
		return this.piece1.getValue() == this.piece2.getValue();
	}
}

class ExamplesGame {
	  IGamePiece four = new MergeTile(new BaseTile(2), new BaseTile(2));
	  boolean testGetValue(Tester t) {
	    return t.checkExpect(four.getValue(), 4);
	  }
}
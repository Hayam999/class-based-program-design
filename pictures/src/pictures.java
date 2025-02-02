import tester.*;
interface IPicture {
	String scale();
	String overlay(IPicture picture);
	String overlayHelper(String sentence);
	String beside(IPicture picture);
	String besideHelper(String sentence);
	int countShapes();
}
class Shape implements IPicture {
	String kind;
	int size;
	Shape(String kind, int size) {
		this.kind = kind;
		this.size = size;
	}
	public String scale() {
		return "the result of scaling " + this.kind;
	}
	public String overlay(IPicture picture) {
		return picture.overlayHelper("A " + this.kind + " on " );
	}
	public String overlayHelper(String sentence) {
		return sentence + this.kind;
	}
	public String beside(IPicture picture) {
		return picture.besideHelper("A " + this.kind + "beside ");
	}
	public String besideHelper(String sentence) {
		return sentence + "A " + this.kind;
	}
	public int countShapes() {
		return 1;
	}
}
class Combo implements IPicture{
	String name;
	IOperation operation;
	Combo(String name, IOperation operation) {
		this.name = name;
		this.operation = operation;
	}
	public String scale() {
		return "the result of scaling " + this.name;
	}
	public String overlay(IPicture picture) {
		return picture.overlayHelper(this.name + " on ");
	}
	public String overlayHelper(String sentence) {
		return sentence + this.name;
	}
	public String beside(IPicture picture) {
		return picture.besideHelper(this.name + " beside ");
	}
	public String besideHelper(String sentence) {
		return sentence + this.name;
	}
	public int countShapes() {
		return this.operation.countShapesHelper();
	}
}
interface IOperation {
	int countShapesHelper();
}
class Scale implements IOperation {
	Scale() {}
	public int countShapesHelper() {
		return 1;
	}
}
class Beside implements IOperation {
	Beside() {}
	public int countShapesHelper() {
		return 2;
	}
}
class Overlay implements IOperation {
	Overlay() {}
	public int countShapesHelper() {
		return 2;
	}
}
class Operations implements IOperation {
	ILOperations operations;
	Operations(ILOperations operations) {
		this.operations = operations;
	}
	public int countShapesHelper() {
		return this.operations.countLOShapes();
	}
}
interface ILOperations {
	int countLOShapes();
}
class MTLOperations implements ILOperations {
	MTLOperations() {}
	public int countLOShapes() {
		return 0;
	}
}
class ConsOperations implements ILOperations {
	IOperation first;
	ILOperations rest;
	ConsOperations(IOperation first, ILOperations rest) {
		this.first = first;
		this.rest = rest;
	}
	public int countLOShapes() {
		return this.first.countShapesHelper() + this.rest.countLOShapes();
	}
}
class ExamplesPicture {
	IPicture circle = new Shape("circle",  20);
	IPicture square = new Shape("square", 30);
	IPicture bigCircle = new Combo(this.circle.scale(), new Scale());
	IPicture squareOnCircle = new Combo(this.square.overlay(this.bigCircle),
			new Operations (new ConsOperations(new Scale(), new ConsOperations (new Overlay(), new MTLOperations()))));
	IPicture doubledSquareOnCircle = new Combo(this.squareOnCircle.beside(this.squareOnCircle),
			new Operations (new ConsOperations(new Scale(),
					new ConsOperations (new Overlay(),new ConsOperations(new Beside(), new MTLOperations())))));
	boolean test(Tester t) {
		return t.checkExpect(squareOnCircle.countShapes(), 3) &&
		t.checkExpect(doubledSquareOnCircle.countShapes(), 5);
	}
	
}
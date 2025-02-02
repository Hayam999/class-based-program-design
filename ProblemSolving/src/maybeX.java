import tester.*;
interface IMaybeInt {
	boolean sameAs(IMaybeInt other);
	boolean sameInt(int otherX);
	boolean sameAbs();
}
interface LstOfMaybeInt {
	IMaybeInt LongestAppear();
	IMaybeInt HelperLongestAppear(MaxAppear max, int acc, IMaybeInt XFirst);
}

abstract class MaybeInt implements IMaybeInt{
	public boolean sameInt(int otherX) {
		return false;
	}
	public boolean sameAbs() {
		return false;
	}
}
class Int extends MaybeInt {
	int x;
	Int(int x) {
		this.x = x;
	}
	public boolean sameAs(IMaybeInt other) {
		return other.sameInt(this.x);
	}
	public boolean sameInt(int otherX) {
		return this.x == otherX;
	}
}
	class Absent extends MaybeInt {
		Absent() {};
		public boolean sameAs(IMaybeInt other) {
			return other.sameAbs();
		}
		public boolean sameAbs() {
			return true;
		}
	}

class MTMaybeInt implements LstOfMaybeInt{
	MTMaybeInt() {}
		public IMaybeInt LongestAppear() {
			return new Absent();
		}
		public IMaybeInt HelperLongestAppear(MaxAppear max, int acc, IMaybeInt XFirst) {
			if (max.acc >= acc) {
				return max.value;
			}
			else {
				return XFirst;
			}
		}
	}
class ConsMaybeInt implements LstOfMaybeInt {
	IMaybeInt first;
	LstOfMaybeInt rest;
	ConsMaybeInt(IMaybeInt first, LstOfMaybeInt rest) {
		this.first = first;
		this.rest = rest;
	}
	public IMaybeInt LongestAppear() {
		return this.rest.HelperLongestAppear(new MaxAppear(new Absent(), 0), 0, this.first);
	}
	public IMaybeInt HelperLongestAppear(MaxAppear max, int acc, IMaybeInt XFirst) {
		if (this.first.sameAs(XFirst)) {
			return this.rest.HelperLongestAppear(max, acc + 1, XFirst);
		}
		else if (max.acc >= acc) {
			return this.rest.HelperLongestAppear(max, 0, this.first);
		}
		else {
			return this.rest.HelperLongestAppear(new MaxAppear(XFirst, acc), 0  , this.first);
		}
	}
}
class MaxAppear {
	IMaybeInt value;
	int acc;
	MaxAppear(IMaybeInt value, int acc) {
		this.value = value;
		this.acc = acc;
	}
}
interface ILoTasks {
	ILoTasks HelperTodo(ILoTasks previousTasks);
	boolean contains(Task t);
}
class MtTasks implements ILoTasks{
	MtTasks() {}
	public ILoTasks HelperTodo(ILoTasks previousTasks) {
		return previousTasks;
	}
	public boolean contains(Task t) {
		return false;
	}
}
class ConsTasks implements ILoTasks{
	Task first;
	ILoTasks rest;
	ConsTasks(Task first, ILoTasks rest) {
		this.first = first;
		this.rest = rest;
}
	public ILoTasks HelperTodo(ILoTasks previousTasks) {
		if (previousTasks.contains(this.first)) {
			return this.rest.HelperTodo(previousTasks);
		}
		else {
			return this.rest.HelperTodo(
					new ConsTasks(this.first, previousTasks));
		}
	}
	public boolean contains(Task t) {
		return this.first.sameId(t) && this.rest.contains(t);
	}
	}
class Task {
	int id;
	ILoTasks prereq;
	Task(int id, ILoTasks prereq) {
		this.id = id;
		this.prereq = prereq;
	}
	ILoTasks makeTodo() {
		return this.prereq.HelperTodo(new ConsTasks(this, new MtTasks()));
	}
	boolean sameId(Task task) {
		return this.id == task.id;
	}
	}


class ExamplesMaybeInt {
	LstOfMaybeInt lst = 
			new ConsMaybeInt(new Int(1), new ConsMaybeInt(new Int(1), new ConsMaybeInt(new Int(5), 
			new ConsMaybeInt(new Int(5), new ConsMaybeInt(new Int(5), new ConsMaybeInt (new Int(4),
					new ConsMaybeInt(new Int(3), new ConsMaybeInt(new Int(4), new ConsMaybeInt(new Int(4),
							new ConsMaybeInt(new Int(4), new MTMaybeInt()))))))))));
	boolean testLongestAppear(Tester t) {
		return t.checkExpect(lst.LongestAppear(), new Int(5));
	}
	 Task firstTask = new Task(1, new MtTasks());
	 Task secTask = new Task(2, new ConsTasks(this.fifthTask, new MtTasks()));
	 Task thirdTask = new Task(3, new ConsTasks(firstTask, new MtTasks()));
	 Task fourthTask = new Task(4, new ConsTasks(secTask, new ConsTasks(thirdTask, new MtTasks())));
	 Task fifthTask = new Task(5, new ConsTasks(secTask, new MtTasks()));
	 
	 boolean testMakeTodo(Tester t) {
		return t.checkExpect(firstTask.makeTodo(), new ConsTasks(new Task(1, new MtTasks()),
				new MtTasks())) &&
				t.checkExpect(secTask.makeTodo(), new ConsTasks(new Task(5, new ConsTasks(secTask, new MtTasks()))
						,new ConsTasks(new Task(2, new ConsTasks(this.fifthTask, new MtTasks())),
						new MtTasks())));
	 }
}
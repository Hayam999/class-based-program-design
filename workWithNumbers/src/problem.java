import tester.*;
interface INumbers {
	 boolean satisfyConditions();
	 boolean haveEven();
	 boolean havePositiveAndOdd();
	 boolean Between5And10();
	 boolean isEven(int num);
	 boolean isPositiveAndOdd(int num);
	 boolean isBetween5And10(int num);
	 boolean conditions2();
	 boolean checkFirst(boolean even, boolean postiveAndOdd, boolean between5And10);
	 boolean checkRest(boolean even, boolean postiveAndOdd, boolean between5And10);
}
class MTNumbers implements INumbers{
	MTNumbers() {}
	public boolean satisfyConditions() {
		return false;
	}
	 public boolean haveEven() {
		 return false;
	 }
	 public boolean havePositiveAndOdd() {
		 return false;
	 }
	 public boolean Between5And10() {
		 return false;
	 }
	 public boolean isEven(int num) {
		 return num % 2 == 0;
	 }
	 public boolean isPositiveAndOdd(int num) {
		 return num % 2 == 1 && num > 0;
	 }
	 public boolean isBetween5And10(int num) {
		 return num <= 10 && num >= 5;
	 }
		public boolean conditions2() {
			return false;
		}
		 public boolean checkFirst(boolean even, boolean postiveAndOdd, boolean between5And10) {
				 return false;
		 }
		 public boolean checkRest(boolean even, boolean positiveAndOdd, boolean between5And10) {
			 if (even && positiveAndOdd && between5And10) {
				 return true;
			 }
			 else {
				 return false;
			 }
		 }
}

class ConsNumbers implements INumbers{
	int first;
	INumbers rest;
	ConsNumbers(int first, INumbers rest) {
		this.first = first;
		this.rest = rest;
	}
	public boolean satisfyConditions() {
		return this.haveEven() && this.havePositiveAndOdd() && this.Between5And10();
	}
	 public boolean haveEven() {
		 if (isEven(this.first)) {
			 return true;
		 }
		 else {
			 return this.rest.haveEven();
		 }
	 }
	 public boolean havePositiveAndOdd() {
		 if (isPositiveAndOdd(this.first)) {
			 return true;
		 }
		 else {
			 return this.rest.havePositiveAndOdd();
		 }
	 }
	 public boolean Between5And10() {
		 if (isBetween5And10(this.first)) {
			 return true;
		 }
		 else {
			 return this.rest.Between5And10();
		 }
	 }
	 public boolean isEven(int num) {
		 return num % 2 == 0;
	 }
	 public boolean isPositiveAndOdd(int num) {
		 return num % 2 == 1 && num > 0;
	 }
	 public boolean isBetween5And10(int num) {
		 return num <= 10 && num >= 5;
	 }

		public boolean conditions2() {
			return checkFirst(false, false, false);
		}
		public boolean checkFirst(boolean even, boolean positiveAndOdd, boolean between5And10) {
			if (this.first > 10) {
				return false;
			}
		else if (!even && isEven(this.first)) {
				return this.rest.checkRest(true, positiveAndOdd, between5And10);
			}
			else if (!positiveAndOdd && isPositiveAndOdd(this.first)) {
				return this.rest.checkRest(even, true, between5And10);
			}
			else if (!between5And10 && isBetween5And10(this.first)) {
				return this.rest.checkRest(even, positiveAndOdd, true);
 			}
			else {
				return this.rest.checkRest(even, positiveAndOdd, between5And10);
			}
		}
		public boolean checkRest(boolean even, boolean positiveAndOdd, boolean between5And10) {
			if (even && positiveAndOdd && between5And10) {
				return true;
			}
			else {
				return checkFirst(even, positiveAndOdd, between5And10);
			}
		}
}
class ExamplesNumbers {
	INumbers firstList = new ConsNumbers(5, new ConsNumbers(6, new MTNumbers()));
	INumbers secondList = new ConsNumbers(5,  new MTNumbers());
	INumbers thirdList = new ConsNumbers(5, new ConsNumbers(6,
			new ConsNumbers(6, new MTNumbers())));
	INumbers fourthList = new ConsNumbers(6, new ConsNumbers(5, new ConsNumbers(42,
			new ConsNumbers(6, new MTNumbers()))));
	boolean test(Tester t) {
		return t.checkExpect(this.firstList.satisfyConditions(), true) &&
				t.checkExpect(this.firstList.conditions2(), false) &&
				t.checkExpect(this.secondList.conditions2(), false) &&
				t.checkExpect(this.secondList.satisfyConditions(), false) &&
				t.checkExpect(this.thirdList.conditions2(), true) &&
				t.checkExpect(this.thirdList.satisfyConditions(), true) &&
				t.checkExpect(this.fourthList.conditions2(), false);
	}
}
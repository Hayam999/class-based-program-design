import tester.Tester;

class BagelRecipe {
	double flour;
	double water;
	double yeast;
	double malt;
	double salt;
	double epsilon = 0.1;
	BagelRecipe(double flour, double water, double yeast, double malt, double salt) {
		if (Math.abs(flour - water) < epsilon ) {
			this.water = water;
			this.flour = flour;
		}
		else {
			throw new IllegalArgumentException("flour and water must be equivelant");
		}
		if (Math.abs(yeast - malt) < epsilon ) {
			this.yeast = yeast;
			this.malt = malt;
		}
		else {
			throw new IllegalArgumentException("yeast and malt must be equivelant");
		}
		if ((salt + yeast) - (0.05 * flour) < epsilon) {
			this.salt = salt;
		}
		else {
			throw new IllegalArgumentException("salt + yeast must be 1/20 of the flour");
		}
	}
	BagelRecipe(double flour, double yeast) {
		this(flour, flour, yeast, yeast, ((0.05 * flour) - yeast));
	}
	BagelRecipe(double flour, double yeast, double salt) {
		this(flour * 4.25, flour * 4.25, (yeast / 48) * 5, (yeast / 48) * 5, (salt / 48) * 10);
	}
	
	boolean sameRecipe(BagelRecipe other) {
		return (Math.abs(this.flour - other.flour) < epsilon) &&
				(Math.abs(this.water - other.water) < epsilon) &&
				(Math.abs(this.yeast - other.yeast) < epsilon) &&
				(Math.abs(this.malt - other.malt) < epsilon) &&
				(Math.abs(this.salt- other.salt) < epsilon);
	}
}

class ExamplesBagels {
	BagelRecipe bag1 = new BagelRecipe(10.0, 10.0, 0.2, 0.2, 0.3);
	BagelRecipe bag2 = new BagelRecipe(20.0, 0.5);
	BagelRecipe bag3 = new BagelRecipe(2.0, 0.2, 1.08);
	boolean testSameRecipe(Tester t) {
		return t.checkExpect(bag1.sameRecipe(bag1), true) &&
				t.checkExpect(bag2.sameRecipe(bag1), false);
	}
}
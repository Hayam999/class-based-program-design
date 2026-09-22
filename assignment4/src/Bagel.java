import tester.*;
/**
 * InnerBagelRecipe
 */

class BagelRecipe{
    double flour;
    double water;
    double yeast;
    double salt;
    double malt;
    BagelRecipe(double flour, double water, double yeast,
        double salt, double malt) {
            if (Math.abs(flour - water) > 0.001) {
                throw new IllegalArgumentException("The weight of the flour should be equal to the weight of the water");
            }
            else if (Math.abs(yeast - malt) > 0.001) {
                throw new IllegalArgumentException("the weight of the yeast should be equal to the weight of the malt");

            } else if (Math.abs((salt + yeast) - (flour / 20)) > 0.001) {
                throw new IllegalArgumentException("the weight of the salt + yeast should be 1/20th the weight of the flour");
            }
            else {
                this.flour = flour;
                this.water = water;
                this.yeast = yeast;
                this.salt  = salt;
                this.malt  = malt;
            }
        }

    BagelRecipe(double flour, double yeast) {
            this(flour, flour, yeast, (flour / 20) - yeast, yeast);
    }

    BagelRecipe(double flour, double yeast, double salt) {
            this(flour * 4.25, flour * 4.25, (yeast * 5) / 48, ((flour * 4.25) / 20) - ((yeast * 5) / 48), (yeast * 5) / 48);
    }

    public boolean sameRecipe(BagelRecipe other) {
        // checking other ingredients would be redundunt.
        if (Math.abs(this.flour - other.flour) > 0.001) {
            return false;
        } else if (Math.abs(this.yeast - other.yeast) > 0.001) {
            return false;
        }
        return true;
    }
}





class ExamplesBagel{

    BagelRecipe bagelInOunc1 = new BagelRecipe(48, 48, 2, .4, 2);
    BagelRecipe bagelInOunc2 = new BagelRecipe(40, 6);


    double flourInOunc = 50 * 4.25;
    double saltInOunc = 1.75;
    double saltInSpoons = saltInOunc * 48 / 10;
    double yeastInOunc = (flourInOunc/20) - saltInOunc;
    double yeastInSpoons = (yeastInOunc * 48) / 5;
    BagelRecipe bagelInCups =  new BagelRecipe(50, yeastInSpoons, saltInSpoons);

    boolean testBagel(Tester t) {
        return t.checkExpect(bagelInCups.sameRecipe(bagelInOunc1), false) && 
               t.checkExpect(bagelInOunc1.sameRecipe(bagelInOunc1), true);
    }

    public static void main(String[] args) {
        // ============================================================
        // DEBUG MODE: use this when you want to set a breakpoint and
        // step through a specific method. Tester.runReport is NOT used
        // here, so there's no 60ms watchdog timeout to fight with the
        // debugger -- you can pause for as long as you want.
        //
        // To use it: comment out the "NORMAL MODE" line below, uncomment
        // this block, edit the method call to whatever you're currently
        // debugging, put your breakpoint inside that method, then hit
        // Debug (F5) on this file/config.
        // ------------------------------------------------------------
         ExamplesBagel bagel = new ExamplesBagel();
         boolean result = bagel.bagelInOunc1.sameRecipe(bagel.bagelInOunc1);
         System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesBagel(), false, false);
    }
}
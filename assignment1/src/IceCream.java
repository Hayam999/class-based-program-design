interface IceCream{}

class EmptyServing implements IceCream {
    EmptyServing() {}
}

class Scooped implements IceCream{
    IceCream more;
    String flavor;

    Scooped(IceCream more, String flavor) {
        this.more = more;
        this.flavor = flavor;
    }
}

class ExamplesIceCream{

    IceCream cone = new EmptyServing();
    IceCream mintChip = new Scooped(cone, "mint chip");
    IceCream coffee = new Scooped(mintChip, "coffee");
    IceCream blackRas = new Scooped(coffee, "black raspberry");
    IceCream order1 = new Scooped(blackRas, "caramel swirl");
    
    IceCream chocolate = new Scooped(cone, "chocolate");
    IceCream vanilla = new Scooped(chocolate, "vanilla");
    IceCream order2 = new Scooped(vanilla, "strawberry");
}
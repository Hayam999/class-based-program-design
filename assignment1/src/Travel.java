interface Housing {}
class Hut implements Housing {
    int capacity;
    int population;

    Hut(int capacity, int population) {
        this.capacity = capacity;
        this.population = population;
    }
    
}

class Inn implements Housing{
    String name;
    int capacity;
    int population;
    int stalls;

    Inn (String name, int capacity, int population, int stalls)
    {
        this.name = name;
        this.capacity = capacity;
        this.population = population;
        this.stalls = stalls;
    }
}

class Castle implements Housing {
    String name;
    String familyName;
    int population;
    int carriageCapacity;

    Castle (String name, String familyName, int population, int carriageCapacity) {
        this.name = name;
        this.familyName = familyName;
        this.population = population;
        this.carriageCapacity = carriageCapacity;
    }
}


interface Transportation{}

class Horse implements Transportation {
    String name;
    String color;
    Housing from;
    Housing to;

    Horse(String name, String color, Housing from, Housing to) {
        this.name = name;
        this.color = color;
        this.from = from;
        this.to = to;
    }
}

class Carriage implements Transportation {
    Housing from;
    Housing to;
    int tonnage;

    Carriage(Housing from, Housing to, int tonnage) {
        this.from = from;
        this.to = to;
        this.tonnage = tonnage;
    }
}


class ExamplesTravel {

    Housing hovel = new Hut(5, 1);
    Housing winterfell = new Castle("Winterfell", "Stark", 500, 6);
    Housing crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);

    // the other types are easy to work it out yourself
}
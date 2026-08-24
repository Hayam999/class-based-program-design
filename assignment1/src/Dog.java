import tester.Tester;
class Dog {
    String name;
    String breed;
    int yob;
    String state;
    boolean hypoallergenic;

    Dog(String name, String breed, int yob, String state, boolean hypoallergenic) {
        this.name = name;
        this.breed = breed;
        this.yob = yob;
        this.state = state;
        this.hypoallergenic = hypoallergenic;
    }

}

 class ExamplesDogs{
        Dog huffle = new Dog("Hufflepuff", "Wheaten Terrier", 2012, "TX", true );
        Dog pearl = new Dog("Pearl", "Labrador Retriever", 2016, "MA", false);

           public static void main(String[] args) {
    Tester.runReport(new ExamplesDogs(), false, false);
  }
    }
    
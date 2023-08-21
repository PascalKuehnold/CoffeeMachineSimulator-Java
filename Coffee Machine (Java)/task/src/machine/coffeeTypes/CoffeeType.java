package machine.coffeeTypes;

public abstract class CoffeeType {

    protected String type;
    protected int neededWater;
    protected int neededCoffeeBeans;
    protected int neededMilk;
    protected int price;

    // Constructor
    public CoffeeType(String type, int neededWater, int neededCoffeeBeans, int price) {
        this.type = type;
        this.neededWater = neededWater;
        this.neededCoffeeBeans = neededCoffeeBeans;
        this.price = price;
    }

    public CoffeeType(String type, int neededWater, int neededMilk, int neededCoffeeBeans, int price) {
        this(type, neededWater, neededCoffeeBeans, price); // Call the other constructor using "this"
        this.neededMilk = neededMilk;
    }

    // Getters for the properties
    public String getType() {
        return type;
    }

    public int getNeededWater() {
        return neededWater;
    }

    public int getNeededCoffeeBeans() {
        return neededCoffeeBeans;
    }

    public int getPrice() {
        return price;
    }

    public int getNeededMilk() {
        return neededMilk;
    }
}

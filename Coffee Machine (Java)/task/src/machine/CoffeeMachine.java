package machine;

import machine.coffeeTypes.Cappuccino;
import machine.coffeeTypes.CoffeeType;
import machine.coffeeTypes.Espresso;
import machine.coffeeTypes.Latte;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        coffeeMachine.run();
    }

    private enum Options {
        BUY, FILL, TAKE, REMAINING, EXIT
    }

    private int earnedMoney = 550;

    private int existingWaterAmount = 400;
    private int existingMilkAmount = 540;
    private int existingCoffeeBeans = 120;
    private int existingDisposableCups = 9;

    private final Scanner scanner = new Scanner(System.in);
    boolean isRunning = true;

    private void run() {
        try {
            while (isRunning) {
                printOptions();
                String option = scanner.next();
                Options selectedOption;

                try {
                    selectedOption = Options.valueOf(option.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input, please try again.\n");
                    continue; // Skip the rest of the loop and start over
                }

                switch (selectedOption) {
                    case FILL -> handleFill();
                    case TAKE -> handleTake();
                    case BUY -> handleBuy();
                    case REMAINING -> handleRemaining();
                    case EXIT -> isRunning = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            shutdown();
        }
    }

    private void printOptions() {
        StringBuilder optionsString = new StringBuilder("Choose an option: ");

        Options[] values = Options.values();
        for (int i = 0; i < values.length; i++) {
            optionsString.append("'").append(values[i]).append("'");

            if (i < values.length - 1) {
                optionsString.append(" | ");
            }
        }

        System.out.println(optionsString);
    }

    private void shutdown() {
        System.exit(0);
    }

    private void initResources() {
        existingWaterAmount += getUserInput("How many ml of water do you want to add?:");
        existingMilkAmount += getUserInput("How many ml of milk do you want to add?:");
        existingCoffeeBeans += getUserInput("How many grams of coffee beans do you want to add?:");
        existingDisposableCups += getUserInput("How many disposable cups to you want to add?:");
    }

    private void handleFill() {
        initResources();
    }

    private void handleBuy() {
        String coffeeType = getCoffeeType();
        CoffeeType coffee = getSelectedCoffeeType(coffeeType);

        if (coffee != null && !isEnoughResources(coffee)) {
            System.out.println(printBuyingResponse(coffee));
            return;
        }

        if (coffee != null) {
            System.out.println(printBuyingResponse(coffee));
            handleResourcesStateChange(coffee);
        }
    }

    private CoffeeType getSelectedCoffeeType(String coffeeType) {
        switch (coffeeType.toLowerCase()) {
            case "1", "espresso" -> {
                return new Espresso();
            }
            case "2", "latte" -> {
                return new Latte();
            }
            case "3", "cappuccino" -> {
                return new Cappuccino();
            }
            case "back" -> {
                System.out.println("Back to menu\n");
                return null;
            }
            default -> {
                System.out.println("NOT AVAILABLE\n");
                return null;
            }
        }
    }

    /**
     * Return the current state of the coffee machine
     */
    private void handleRemaining() {
        System.out.println("\n" + this);
    }

    /**
     * Updates the resources according to the use amount of the coffee type
     *
     * @param coffeeType the type which was bought
     */
    private void handleResourcesStateChange(CoffeeType coffeeType) {
        this.earnedMoney += coffeeType.getPrice();
        this.existingWaterAmount -= coffeeType.getNeededWater();
        this.existingCoffeeBeans -= coffeeType.getNeededCoffeeBeans();
        this.existingMilkAmount -= coffeeType.getNeededMilk();
        this.existingDisposableCups--;
    }


    /**
     * Resets the earned money by the machine to 0, if the worker took the money out
     */
    private void handleTake() {
        System.out.printf("I gave you $%d\n\n", earnedMoney);
        earnedMoney = 0;
    }

    /**
     * Ask the user which coffee he wants to buy
     *
     * @return the value of the coffee
     */
    private String getCoffeeType() {
        System.out.println("What type of coffee do you want? 1 - Espresso, 2 - Latte, 3 - Cappuccino, back - Back to menu");
        return scanner.next();
    }

    /**
     * Ask the user an amount for the given question on prompt
     *
     * @param prompt depends on what mode the user is on
     * @return the amount of the input
     */
    private int getUserInput(String prompt) throws InputMismatchException {
        System.out.println(prompt);
        return scanner.nextInt();
    }

    private String printBuyingResponse(CoffeeType coffeeType) {
        if (!isEnoughWater(coffeeType)) {
            return "Sorry, not enough water!";
        }
        if (!isEnoughMilk(coffeeType)) {
            return "Sorry, not enough milk!";
        }
        if (!isEnoughCoffeeBeans(coffeeType)) {
            return "Sorry, not enough coffee beans!";
        }
        if (!isEnoughDisposableCups()) {
            return "Sorry, i don't have any cups left!";
        }

        return "I have enough resources, making you a coffee!";

    }

    private boolean isEnoughResources(CoffeeType coffeeType) {
        return isEnoughWater(coffeeType) && isEnoughMilk(coffeeType) && isEnoughCoffeeBeans(coffeeType) && isEnoughDisposableCups();
    }

    private boolean isEnoughWater(CoffeeType coffeeType) {
        return coffeeType.getNeededWater() <= existingWaterAmount;
    }

    private boolean isEnoughMilk(CoffeeType coffeeType) {
        return coffeeType.getNeededMilk() <= existingMilkAmount;
    }

    private boolean isEnoughCoffeeBeans(CoffeeType coffeeType) {
        return coffeeType.getNeededCoffeeBeans() <= existingCoffeeBeans;
    }

    private boolean isEnoughDisposableCups() {
        return existingDisposableCups >= 1;
    }


    @Override
    public String toString() {
        return "The coffee machine has:\n"
                + existingWaterAmount + " ml of water\n"
                + existingMilkAmount + " ml of milk\n"
                + existingCoffeeBeans + " g of coffee beans\n"
                + existingDisposableCups + " disposable cups\n"
                + "$" + earnedMoney + " of money\n";
    }
}


//    private String calculatePossibleCoffeeAmount(int coffeeCupsAmount) {
//        if (coffeeCupsAmount > existingDisposableCups) {
//            coffeeCupsAmount = existingDisposableCups;
//        }
//
//        int waterNeeded = waterPerCup * coffeeCupsAmount;
//        int milkNeeded = milkPerCup * coffeeCupsAmount;
//        int coffeeBeansNeeded = coffeeBeansPerCup * coffeeCupsAmount;
//
//        int remainingWater = existingWaterAmount - waterNeeded;
//        int remainingMilk = existingMilkAmount - milkNeeded;
//        int remainingCoffeeBeans = existingCoffeeBeans - coffeeBeansNeeded;
//
//        String output;
//
//        if (remainingWater >= 0 && remainingMilk >= 0 && remainingCoffeeBeans >= 0) {
//            output = "Yes, I can make that amount of coffee";
//
//            if (remainingWater >= waterPerCup && remainingMilk >= milkPerCup && remainingCoffeeBeans >= coffeeBeansPerCup) {
//                int additionalCups = calculateCups(remainingWater, remainingMilk, remainingCoffeeBeans);
//
//                output = String.format("Yes, I can make that amount of coffee (and even %d more than that) ", additionalCups);
//            }
//        } else {
//            int maxCups = calculateCups(existingWaterAmount, existingMilkAmount, existingCoffeeBeans);
//
//            output = String.format("No, I can make only %d cup(s) of coffee ", maxCups);
//            existingDisposableCups -= maxCups;
//        }
//        return output;
//    }


//    private int calculateCups(int water, int milk, int coffeeBeans) {
//        int cups;
//
//        int maxCupsWithWater = water / waterPerCup;
//        int maxCupsWithMilk = milk / milkPerCup;
//        int maxCupsWithCoffeeBeans = coffeeBeans / coffeeBeansPerCup;
//
//        cups = Math.min(Math.min(maxCupsWithWater, maxCupsWithMilk), maxCupsWithCoffeeBeans);
//        return cups;
//    }

/*
  Calculates how many cups are possible with the current amount of resources in the machine
 */
//    private void handleCupAmount() {
//        int coffeeCupsAmount = getUserInput("How many cups of coffee you will need:");
//        String result = calculatePossibleCoffeeAmount(coffeeCupsAmount);
//        System.out.println(result);
//    }


//    /**
//     * Sets the initial required resources based on the coffee type
//     *
//     * @param coffeeType the type of coffee chosen by user
//     */
//    private void handleNeededResources(CoffeeType coffeeType) {
//        this.coffeeBeansPerCup = coffeeType.neededCoffeeBeans;
//        this.waterPerCup = coffeeType.neededWater;
//        this.milkPerCup = coffeeType.neededMilk;
//    }

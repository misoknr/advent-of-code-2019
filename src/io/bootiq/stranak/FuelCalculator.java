package io.bootiq.stranak;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;
import java.util.stream.Stream;

public class FuelCalculator {

    public static int calculateFuelForMass(int mass) {
        return (int) (Math.floor(mass / 3) - 2);
    }

    public static int calculateTotalFuel(int mass) {
        int fuel = calculateFuelForMass(mass);
        int additionalFuelTotal = 0;
        int additionalFuel = fuel;

        do {
            additionalFuel = calculateFuelForMass(additionalFuel);

            if (additionalFuel < 0) {
                additionalFuel = 0;
            }

            additionalFuelTotal += additionalFuel;
        } while (additionalFuel > 0);

        return fuel + additionalFuelTotal;
    }

    public Stream<Integer> readInputAsStream() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/module_mass.txt"));
        return bufferedReader.lines().map(line -> Integer.valueOf(line));
    }

    public int calculateRequiredFuel() {
        Optional<Integer> result = Optional.empty();

        try {
            result = readInputAsStream().map(mass -> calculateTotalFuel(mass)).reduce((left, right) -> left + right);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (result.isPresent()) {
            return result.get();
        }

        return 0;
    }

    public static void main(String[] args) {
        FuelCalculator fuelCalculator = new FuelCalculator();

        System.out.println(String.format("Required fuel: %d", fuelCalculator.calculateRequiredFuel()));
    }
}

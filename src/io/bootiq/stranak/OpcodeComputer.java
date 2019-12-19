package io.bootiq.stranak;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Arrays;

public class OpcodeComputer {

    public Integer[] readInput() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/opcode_input.txt"));
        var optionalString = bufferedReader.lines().reduce((s1, s2) -> s1.concat(s2));

        if (optionalString.isPresent()) {
            return Arrays.stream(optionalString.get().split(",")).map(s -> Integer.valueOf(s)).toArray(Integer[]::new);
        }

        return null;
    }

    private int processOpcode(Integer[] input, int position) {
        int operand = input[position];

        switch (operand) {
            case 1:
                input[input[position + 3]] = input[input[position + 1]] + input[input[position + 2]];
                return position + 4;
            case 2:
                input[input[position + 3]] = input[input[position + 1]] * input[input[position + 2]];
                return position + 4;
            case 99:
            default:
                return -1;
        }
    }

    public void processOpcodeInput(Integer[] input) {
        int position = 0;

        do {
            position = processOpcode(input, position);
        } while (position >= 0);
    }

    public int restoreErrorState(Integer[] input) throws FileNotFoundException {
        input[1] = 12;
        input[2] = 2;

        processOpcodeInput(input);

        return input[0];
    }

    public static void main(String[] args) throws FileNotFoundException {
        OpcodeComputer opcodeComputer = new OpcodeComputer();
        var input = opcodeComputer.readInput();

//        Integer[] testInput = new Integer[]{1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50};
//        opcodeComputer.processOpcodeInput(testInput);
//
//        System.out.println("Opcode after processing");
//        Arrays.stream(testInput).forEach(i -> System.out.print(i + ", "));

        System.out.println("Value at zero position: " + opcodeComputer.restoreErrorState(input));
    }

}

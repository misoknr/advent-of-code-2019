package io.bootiq.stranak;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class OpcodeComputer {

    public Integer[] readMemory() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/opcode_input.txt"));
        var optionalString = bufferedReader.lines().reduce((s1, s2) -> s1.concat(s2));

        if (optionalString.isPresent()) {
            return Arrays.stream(optionalString.get().split(",")).map(s -> Integer.valueOf(s)).toArray(Integer[]::new);
        }

        return null;
    }

    private int executeInstruction(Integer[] memory, int memoryPointer) {
        int instruction = memory[memoryPointer];

        switch (instruction) {
            case 1:
                memory[memory[memoryPointer + 3]] = memory[memory[memoryPointer + 1]] + memory[memory[memoryPointer + 2]];
                return memoryPointer + 4;
            case 2:
                memory[memory[memoryPointer + 3]] = memory[memory[memoryPointer + 1]] * memory[memory[memoryPointer + 2]];
                return memoryPointer + 4;
            case 99:
            default:
                return -1;
        }
    }

    public void processMemory(Integer[] memory, Integer noun, Integer verb) {
        int position = 0;

        if (noun != null) {
            memory[1] = noun;
        }

        if (verb != null) {
            memory[2] = verb;
        }

        do {
            position = executeInstruction(memory, position);
        } while (position >= 0);
    }

    public int[] findNounAndVerbForOutput(Integer[] memory, int targetOutput) {
        int[] foundWords = null;

        for (int noun = 0; noun < 100; noun++) {
            if (foundWords != null) {
                break;
            }

            for (int verb = 0; verb < 100; verb++) {
                var memoryCopy = memory.clone();
                processMemory(memoryCopy, noun, verb);
                int output = memoryCopy[0];

                if (output == targetOutput) {
                    foundWords = new int[]{noun, verb};
                    break;
                }
            }
        }

        return foundWords;
    }

    public static void main(String[] args) throws FileNotFoundException {
        OpcodeComputer opcodeComputer = new OpcodeComputer();
        Integer[] memory = opcodeComputer.readMemory();
        Integer[] memoryCopy = memory.clone();

//        opcodeComputer.processMemory(memoryCopy, 12, 2);
//        System.out.println("Value at memory zero position: " + memoryCopy[0]);

        int targetOutput = 19690720;
        int[] words = opcodeComputer.findNounAndVerbForOutput(memory, targetOutput);

        if (words == null || words.length < 2) {
            System.out.println(String.format("Noun and verb that produces output '%d' not found", targetOutput));
        } else {
            System.out.println(String.format("Found noun and verb for output %d: [%d, %d]", targetOutput, words[0], words[1]));
            System.out.println("100 * NOUN + VERB = " + (100 * words[0] + words[1]));
        }
    }

}

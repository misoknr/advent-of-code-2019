package io.bootiq.stranak;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpcodeComputer {

    private List<Integer> output = new ArrayList<>();

    public Integer[] readMemory(String inputFile) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/" + inputFile));
        var optionalString = bufferedReader.lines().reduce((s1, s2) -> s1.concat(s2));

        if (optionalString.isPresent()) {
            return Arrays.stream(optionalString.get().split(",")).map(s -> Integer.valueOf(s)).toArray(Integer[]::new);
        }

        return null;
    }

    private int executeInstruction(Integer[] memory, int memoryPointer) throws IOException {
        String operation = String.valueOf(memory[memoryPointer]);
        int operationLength = operation.length();
        int instruction = Integer.valueOf(operation.substring(operationLength > 2 ? operation.length() - 2 : 0));
        int[] parameterModes = new int[]{0, 0, 0};

        if (operation.length() > 2) {
            String modes = operation.substring(0, operation.length() - 2);
            int modePosition = 0;

            for (int i = modes.length() - 1; i >= 0; i--) {
                parameterModes[modePosition] = Integer.parseInt(modes.substring(i, i + 1));
                modePosition++;
            }
        }

        int param1;
        int param2;
        int writePointer;

        switch (instruction) {
            case 1:
                param1 = parameterModes[0] == 0 ? memory[memory[memoryPointer + 1]] : memory[memoryPointer + 1];
                param2 = parameterModes[1] == 0 ? memory[memory[memoryPointer + 2]] : memory[memoryPointer + 2];
                writePointer = memory[memoryPointer + 3];
                memory[writePointer] = param1 + param2;

                return memoryPointer + 4;
            case 2:
                param1 = parameterModes[0] == 0 ? memory[memory[memoryPointer + 1]] : memory[memoryPointer + 1];
                param2 = parameterModes[1] == 0 ? memory[memory[memoryPointer + 2]] : memory[memoryPointer + 2];
                writePointer = memory[memoryPointer + 3];
                memory[writePointer] = param1 * param2;

                return memoryPointer + 4;
            case 3:
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int input = Integer.parseInt(reader.readLine());
                param1 = memory[memoryPointer + 1];
                memory[param1] = input;

                return memoryPointer + 2;
            case 4:
                param1 = memory[memoryPointer + 1];
                this.output.add(memory[param1]);

                return memoryPointer + 2;
            case 99:
            default:
                return -1;
        }
    }

    public void processMemory(Integer[] memory, Integer noun, Integer verb) throws IOException {
        this.output.clear();
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

    public int[] findNounAndVerbForOutput(Integer[] memory, int targetOutput) throws IOException {
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

    public static void main(String[] args) throws IOException {
        OpcodeComputer opcodeComputer = new OpcodeComputer();
        Integer[] memory = opcodeComputer.readMemory("opcode2_input.txt");
//        Integer[] memory = new Integer[]{3, 0, 4, 0, 99};

//        Integer[] memoryCopy = memory.clone();
//        opcodeComputer.processMemory(memoryCopy, 12, 2);
//        System.out.println("Value at memory zero position: " + memoryCopy[0]);

//        int targetOutput = 19690720;
//        int[] words = opcodeComputer.findNounAndVerbForOutput(memory, targetOutput);
//
//        if (words == null || words.length < 2) {
//            System.out.println(String.format("Noun and verb that produces output '%d' not found", targetOutput));
//        } else {
//            System.out.println(String.format("Found noun and verb for output %d: [%d, %d]", targetOutput, words[0], words[1]));
//            System.out.println("100 * NOUN + VERB = " + (100 * words[0] + words[1]));
//        }

        opcodeComputer.processMemory(memory, null, null);

        for (int i = 0; i < memory.length; i++) {
            System.out.print(memory[i] + ", ");
        }

        System.out.println("Output: ");

        for (Integer o : opcodeComputer.output) {
            System.out.print(o + ",");
        }
    }

}

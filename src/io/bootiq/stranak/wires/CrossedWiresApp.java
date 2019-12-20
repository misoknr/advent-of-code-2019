package io.bootiq.stranak.wires;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class CrossedWiresApp {

    public List<String> loadWirePaths() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input/crossed_wires.txt"));
        return bufferedReader.lines().collect(Collectors.toList());
    }

    public Wire processWirePath(String path) {
        Wire wire = new Wire();
        int[] currentPosition = new int[]{0, 0};

        for (String move : path.split(",")) {
            char direction = move.charAt(0);
            int distance = Integer.parseInt(move.substring(1));
            int[] newPosition = currentPosition.clone();

            switch (direction) {
                case 'U':
                    newPosition[1] += distance;
                    wire.addVerticalVector(currentPosition.clone(), newPosition.clone());
                    break;
                case 'D':
                    newPosition[1] -= distance;
                    wire.addVerticalVector(newPosition.clone(), currentPosition.clone());
                    break;
                case 'R':
                    newPosition[0] += distance;
                    wire.addHorizontalVector(currentPosition.clone(), newPosition.clone());
                    break;
                case 'L':
                    newPosition[0] -= distance;
                    wire.addHorizontalVector(newPosition.clone(), currentPosition.clone());
                    break;
                default:
                    break;
            }

            currentPosition = newPosition;
        }

        return wire;
    }

    private List<int[]> findVectorIntersections(List<int[][]> horizontalVectors, List<int[][]> verticalVectors) {
        List<int[]> intersections = new ArrayList<>();

        for (int[][] horizontal : horizontalVectors) {
            for (int[][] vertical : verticalVectors) {
                int horizontalVectorY = horizontal[0][1];
                int horizontalVertex1X = horizontal[0][0];
                int horizontalVertex2X = horizontal[1][0];
                int verticalVectorX = vertical[0][0];
                int verticalVertex1Y = vertical[0][1];
                int verticalVertex2Y = vertical[1][1];

                if (verticalVectorX > horizontalVertex1X && verticalVectorX <= horizontalVertex2X && horizontalVectorY > verticalVertex1Y && horizontalVectorY <= verticalVertex2Y) {
                    if (verticalVectorX != 0 && horizontalVectorY != 0) {
                        intersections.add(new int[]{verticalVectorX, horizontalVectorY});
                    }
                }
            }
        }

        return intersections;
    }

    public List<int[]> findWireIntersections(Wire w1, Wire w2) {
        List<int[]> intersections = new ArrayList<>();
        var w1HorizontalVectors = w1.getHorizontalVectors();
        var w1VerticalVectors = w1.getVerticalVectors();
        var w2HorizontalVectors = w2.getHorizontalVectors();
        var w2VerticalVectors = w2.getVerticalVectors();

        intersections.addAll(findVectorIntersections(w1HorizontalVectors, w2VerticalVectors));
        intersections.addAll(findVectorIntersections(w2HorizontalVectors, w1VerticalVectors));

        return intersections;
    }

    public static void main(String[] args) throws FileNotFoundException {
        CrossedWiresApp app = new CrossedWiresApp();

//        List<String> wireInput = new ArrayList<>();
//        wireInput.add("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51");
//        wireInput.add("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7");

        List<String> wireInput = app.loadWirePaths();
        List<Wire> wires = wireInput.stream().map(wirePath -> app.processWirePath(wirePath)).collect(Collectors.toList());

//        wires.stream().forEach(System.out::println);

        List<int[]> intersections = app.findWireIntersections(wires.get(0), wires.get(1));

        System.out.println("Intersections:");
        intersections.stream().forEach(coordinates -> System.out.println(String.format("[%d, %d]", coordinates[0], coordinates[1])));

        int shortestPath = intersections.stream().map(coordinates -> Math.abs(coordinates[0]) + Math.abs(coordinates[1])).min(Integer::compareTo).get();
        System.out.println(String.format("Nearest intersection distance: %d", shortestPath));
    }
}

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
            boolean vectorHorizontal = false;
            boolean unknownOperation = false;

            switch (direction) {
                case 'U':
                    newPosition[1] += distance;
                    break;
                case 'D':
                    newPosition[1] -= distance;
                    break;
                case 'R':
                    newPosition[0] += distance;
                    vectorHorizontal = true;
                    break;
                case 'L':
                    newPosition[0] -= distance;
                    vectorHorizontal = true;
                    break;
                default:
                    unknownOperation = true;
                    break;
            }

            if (unknownOperation) {
                continue;
            }

            wire.addVector(vectorHorizontal, currentPosition.clone(), newPosition.clone());
            currentPosition = newPosition;
        }

        return wire;
    }

    private List<int[]> findVectorIntersections(List<Wire.Vector> horizontalVectors, List<Wire.Vector> verticalVectors) {
        List<int[]> intersections = new ArrayList<>();

        for (Wire.Vector horizontal : horizontalVectors) {
            for (Wire.Vector vertical : verticalVectors) {
                int horizontalVectorY = horizontal.getStartVertex()[1];
                int verticalVectorX = vertical.getStartVertex()[0];

                List<int[]> horizontalVertices = new ArrayList<>();
                horizontalVertices.add(horizontal.getStartVertex());
                horizontalVertices.add(horizontal.getEndVertex());
                horizontalVertices.sort(Comparator.comparingInt(o -> o[0]));

                List<int[]> verticalVertices = new ArrayList<>();
                verticalVertices.add(vertical.getStartVertex());
                verticalVertices.add(vertical.getEndVertex());
                verticalVertices.sort(Comparator.comparingInt(o -> o[1]));

                if (verticalVectorX >= horizontalVertices.get(0)[0] && verticalVectorX <= horizontalVertices.get(1)[0] && horizontalVectorY > verticalVertices.get(0)[1] && horizontalVectorY <= verticalVertices.get(1)[1]) {
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

    public int[] calculateIntersectionDistances(Wire wire, List<int[]> intersections) {
        int[] distances = new int[intersections.size()];
        int distanceTravelled = 0;
        List<Integer> crossedIntersectionIndices = new ArrayList<>();

        for (Wire.Vector vector : wire.getVectors()) {
            for (int i = 0; i < intersections.size(); i++) {
                if (crossedIntersectionIndices.contains(i)) {
                    continue;
                }

                int distanceToIntersection = getDistanceOfVertexOnVector(intersections.get(i), vector);

                if (distanceToIntersection != -1) {
                    distances[i] = distanceTravelled + distanceToIntersection;
                    crossedIntersectionIndices.add(i);
                }
            }

            distanceTravelled += vector.getVectorLength();
        }

        return distances;
    }

    private int getDistanceOfVertexOnVector(int[] vertex, Wire.Vector vector) {
        int[] vectorStart = vector.getStartVertex();
        int[] vectorEnd = vector.getEndVertex();

        if (vector.isHorizontal() && vector.getStartVertex()[1] == vertex[1]) {
            if ((vectorStart[0] < vectorEnd[0] && vertex[0] >= vectorStart[0] && vertex[0] <= vectorEnd[0])
                    || (vectorStart[0] > vectorEnd[0] && vertex[0] <= vectorStart[0] && vertex[0] >= vectorEnd[0])) {
                return Math.abs(vectorStart[0] - vertex[0]);
            }
        }

        if (!vector.isHorizontal() && vector.getStartVertex()[0] == vertex[0]) {
            if ((vectorStart[1] < vectorEnd[1] && vertex[1] >= vectorStart[1] && vertex[1] <= vectorEnd[1])
                    || (vectorStart[1] > vectorEnd[1] && vertex[1] <= vectorStart[1] && vertex[1] >= vectorEnd[1])) {
                return Math.abs(vectorStart[1] - vertex[1]);
            }
        }

        return -1;
    }

    public int calculateLeastIntersectionSteps(List<int[]> intersections, List<Wire> wires) {
        int[] wireAIntersectionDistances = calculateIntersectionDistances(wires.get(0), intersections);
        int[] wireBIntersectionDistances = calculateIntersectionDistances(wires.get(1), intersections);
        Integer lowestDistance = null;

        for (int i = 0; i < intersections.size(); i++) {
            int distanceSum = wireAIntersectionDistances[i] + wireBIntersectionDistances[i];
            if (lowestDistance == null || lowestDistance > distanceSum) {
                lowestDistance = distanceSum;
            }
        }

        return lowestDistance;
    }

    public static void main(String[] args) throws FileNotFoundException {
        CrossedWiresApp app = new CrossedWiresApp();

//        List<String> wireInput = new ArrayList<>();
//        wireInput.add("R75,D30,R83,U83,L12,D49,R71,U7,L72");
//        wireInput.add("U62,R66,U55,R34,D71,R55,D58,R83");

        List<String> wireInput = app.loadWirePaths();
        List<Wire> wires = wireInput.stream().map(wirePath -> app.processWirePath(wirePath)).collect(Collectors.toList());

//        wires.stream().forEach(System.out::println);

        List<int[]> intersections = app.findWireIntersections(wires.get(0), wires.get(1));

        System.out.println("Intersections:");
        intersections.stream().forEach(coordinates -> System.out.println(String.format("[%d, %d]", coordinates[0], coordinates[1])));

        int shortestPath = intersections.stream().map(coordinates -> Math.abs(coordinates[0]) + Math.abs(coordinates[1])).min(Integer::compareTo).get();
        System.out.println(String.format("Nearest intersection distance: %d", shortestPath));

        int lowestIntersectionStepsSum = app.calculateLeastIntersectionSteps(intersections, wires);

        System.out.println(String.format("Lowest number of combined steps to intersection: %d", lowestIntersectionStepsSum));
    }
}

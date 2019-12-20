package io.bootiq.stranak.wires;

import java.util.ArrayList;
import java.util.List;

public class Wire {

    private List<int[][]> horizontalVectors;
    private List<int[][]> verticalVectors;

    public Wire() {
        this.horizontalVectors = new ArrayList<>();
        this.verticalVectors = new ArrayList<>();
    }

    public void addHorizontalVector(int v1[], int v2[]) {
        this.horizontalVectors.add(new int[][]{v1, v2});
    }

    public void addVerticalVector(int v1[], int v2[]) {
        this.verticalVectors.add(new int[][]{v1, v2});
    }

    public List<int[][]> getHorizontalVectors() {
        return horizontalVectors;
    }

    public List<int[][]> getVerticalVectors() {
        return verticalVectors;
    }

    @Override
    public String toString() {
        return "Wire{" +
                "horizontalVectors=" + horizontalVectors.stream().map(vector -> String.format("[%d, %d][%d, %d]", vector[0][0], vector[0][1], vector[1][0], vector[1][1])).reduce((s1, s2) -> String.format("%s, %s", s1, s2)).get() +
                ", verticalVectors=" + verticalVectors.stream().map(vector -> String.format("[%d, %d][%d, %d]", vector[0][0], vector[0][1], vector[1][0], vector[1][1])).reduce((s1, s2) -> String.format("%s, %s", s1, s2)).get() +
                '}';
    }

}

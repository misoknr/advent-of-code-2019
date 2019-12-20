package io.bootiq.stranak.wires;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Wire {

    private List<Vector> vectors = new ArrayList<>();

    public void addVector(boolean horizontal, int start[], int end[]) {
        this.vectors.add(new Vector(horizontal, start, end));
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public List<Vector> getHorizontalVectors() {
        return vectors.stream().filter(vector -> vector.isHorizontal()).collect(Collectors.toList());
    }

    public List<Vector> getVerticalVectors() {
        return vectors.stream().filter(vector -> !vector.isHorizontal()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Wire{" +
                "vectors=" + getHorizontalVectors().stream().map(vector -> vector + ", ") +
                '}';
    }

    public class Vector {

        private boolean horizontal;
        private int[] startVertex;
        private int[] endVertex;

        public Vector(boolean horizontal, int[] startVertex, int[] endVertex) {
            this.horizontal = horizontal;
            this.startVertex = startVertex;
            this.endVertex = endVertex;
        }

        public boolean isHorizontal() {
            return horizontal;
        }

        public int[] getStartVertex() {
            return startVertex;
        }

        public int[] getEndVertex() {
            return endVertex;
        }

        public int getVectorLength() {
            return this.isHorizontal() ? Math.abs(this.startVertex[0] - this.endVertex[0]) : Math.abs(this.startVertex[1] - this.endVertex[1]);
        }

        @Override
        public String toString() {
            return "{" +
                    String.format("start=[%d, %d]", startVertex[0], startVertex[1]) +
                    String.format(", end=[%d, %d]", endVertex[0], endVertex[1]) +
                    '}';
        }
    }

}

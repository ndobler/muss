package com.nd.pgm.operator;

import java.util.Queue;

import com.nd.pgm.Pixel;

public class ReconstructFlatZone extends AbstractOperator {

    private int[][] imageData = null;
    private Queue<Pixel> queue = null;
    private int labelFlatZone;

    /**
     * @param imageData
     * @param queue
     * @param labelFlatZone
     */
    public ReconstructFlatZone(int[][] imageData, Queue<Pixel> queue, int labelFlatZone) {
        this.imageData = imageData;
        this.queue = queue;
        this.labelFlatZone = labelFlatZone;
    }

    @Override
    public int compute(int pixel1, int pixel2, int p1y, int p1x, int p2y, int p2x) {
        if (pixel1 == pixel2 && imageData[p2y][p2x] != labelFlatZone) {
            imageData[p2y][p2x] = labelFlatZone;
            queue.add(new Pixel(p2y, p2x));
        }
        return pixel1;
    }
}

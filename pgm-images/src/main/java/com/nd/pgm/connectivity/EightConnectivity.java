package com.nd.pgm.connectivity;

import com.nd.pgm.operator.IOperator;

public class EightConnectivity implements IConnectivity {

    @Override
    public int compute(int[][] imageData, int i, int j, int size, IOperator operator) {
        int outPixel = imageData[i][j];
        for (int ii = i - size; ii <= i + size; ii++) {
            for (int jj = j - size; jj <= j + size; jj++) {
                try {
                    outPixel = operator.compute(outPixel, imageData[ii][jj]);
                } catch (IndexOutOfBoundsException ex) {
                }
            }
        }
        return outPixel;
    }

}

package com.nd.pgm.connectivity;

import com.nd.pgm.operator.IOperator;

public class EightConnectivity implements IConnectivity {

    @Override
    public int compute(int[][] imageData, int i, int j, int size, IOperator operator) {
        int outPixel = imageData[i][j];
        int height = imageData.length;
        int width = imageData[0].length;
        for (int ii = (i - size) < 0 ? 0 : (i - size); ii <= ((i + size) > height - 1 ? (height - 1) : (i + size)); ii++) {
            for (int jj = (j - size) < 0 ? 0 : (j - size); jj <= ((j + size) > width - 1 ? (width - 1) : (j + size)); jj++) {
                try {
                    outPixel = operator.compute(outPixel, imageData[ii][jj]);
                } catch (IndexOutOfBoundsException ex) {
                    assert false : "No deberia saltar " + ex;
                }
            }
        }
        return outPixel;
    }

}

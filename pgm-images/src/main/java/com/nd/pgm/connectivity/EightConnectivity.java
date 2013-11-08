package com.nd.pgm.connectivity;

import com.nd.pgm.operator.IOperator;

public class EightConnectivity implements IConnectivity {

    @Override
    public int compute(int[][] imageData, int i, int j, int vsize, int hsize, IOperator operator) {
        assert vsize % 2 == 1 : "vsize must be odd";
        assert hsize % 2 == 1 : "vsize must be odd";

        int outPixel = imageData[i][j];
        int height = imageData.length;
        int width = imageData[0].length;

        int hRatio = vsize / 2;
        int vRatio = hsize / 2;
        for (int ii = (i - vRatio) < 0 ? 0 : (i - vRatio); ii <= ((i + vRatio) > height - 1 ? (height - 1) : (i + vRatio)); ii++) {
            for (int jj = (j - hRatio) < 0 ? 0 : (j - hRatio); jj <= ((j + hRatio) > width - 1 ? (width - 1) : (j + hRatio)); jj++) {
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

package com.nd.pgm.connectivity;

import com.nd.pgm.operator.IOperator;

public class EightConnectivity implements IConnectivity {

    @Override
    public int compute(int[][] imageData, int i, int j, int vSize, int hSize, IOperator operator) {
        assert vSize % 2 == 1 : "vsize must be odd";
        assert hSize % 2 == 1 : "vsize must be odd";

        int outPixel = imageData[i][j];
        int height = imageData.length;
        int width = imageData[0].length;

        int hRatio = vSize / 2;
        int vRatio = hSize / 2; // se podria hacer con un condicional que acepte pares?

        int vStart = i - vRatio < 0 ? 0 : (i - vRatio);
        int vEnd = i + vRatio > height - 1 ? (height - 1) : (i + vRatio);
        for (int ii = vStart; ii <= vEnd; ii++) {
            int hStart = j - hRatio < 0 ? 0 : (j - hRatio);
            int hEnd = j + hRatio > width - 1 ? (width - 1) : (j + hRatio);
            for (int jj = hStart; jj <= hEnd; jj++) {
                try {
                    outPixel = computeOperator(operator, outPixel, imageData[ii][jj], i, j, ii, jj);
                } catch (IndexOutOfBoundsException ex) {
                    assert false : "No deberia saltar " + ex;
                }
            }
        }
        return outPixel;
    }

    protected int computeOperator(IOperator operator, int pixel1, int pixel2, int p1y, int p1x, int p2y, int p2x) {
        return operator.compute(pixel1, pixel2, p1y, p1x, p2y, p2x);
    }

}

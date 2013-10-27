package com.nd.pgm.connectivity;

import com.nd.pgm.operator.IOperator;

public class EightConnectivity implements IConnectivity {

    @Override
    public int compute(int[][] imageData, int i, int j, int size, IOperator operator) {
        int outPixel = imageData[i][j];
        // se podria hacer que ii y jj iteren revisando que nunca se arranque antes que 0 y finalice despues del limite superior
        for (int ii = i - size; ii <= i + size; ii++) {
            for (int jj = j - size; jj <= j + size; jj++) {
                try {
                    outPixel = operator.compute(outPixel, imageData[ii][jj]);
                } catch (IndexOutOfBoundsException ex) {
                    //esto es ineficiente, ver comentario anterior
                }
            }
        }
        return outPixel;
    }

}

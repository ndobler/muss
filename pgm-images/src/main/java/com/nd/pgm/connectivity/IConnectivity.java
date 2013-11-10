package com.nd.pgm.connectivity;

import com.nd.pgm.operator.IOperator;

/**
 * Se impementarán clases para ofrecer funcionalidad de comparación 8connectivity o 6connectivity. De esta manera los
 * algoritmos de PGMImage permanecen independientes del tipo de conectividad deseada para aplicar una operacion
 * 
 * @author Nicolás Dobler
 * 
 */
public interface IConnectivity {

    public enum Connectivity {
        EIGHTCONNECTIVITY, FOURCONNECTIVITY
    }

    /**
     * Calculates an operator over pixel coordinates i,j on imageDate using a structuring element size size.
     * 
     * @param imageData the matrix with imageData
     * @param i vertical position where to calculate the operation
     * @param j horizontal position where to calculate the operation
     * @param vsize vertical size of the structuring element
     * @param hsize horizontal size of the structuring element
     * @param operator operation to compute
     * @return value of the output pixel
     */
    public int compute(int[][] imageData, int i, int j, int vsize, int hsize, IOperator operator);

}

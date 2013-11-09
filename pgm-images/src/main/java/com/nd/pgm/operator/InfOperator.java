package com.nd.pgm.operator;

/**
 * Computes inf operation over two single input pixels
 * 
 * @author Nicolás Dobler
 */
public class InfOperator extends AbstractOperator {

    /**
     * Computes inf operation over two single input pixels
     */
    @Override
    public int compute(int pixel1, int pixel2) {
        return pixel1 < pixel2 ? pixel1 : pixel2;
    }

}

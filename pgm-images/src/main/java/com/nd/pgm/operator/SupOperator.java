package com.nd.pgm.operator;

/**
 * Computes sup operation over two single input pixels
 * 
 * @author Nicolás Dobler
 */
public class SupOperator extends AbstractOperator {
    
    /**
     * Computes sup operation over two single input pixels
     */
    @Override
    public int compute(int pixel1, int pixel2) {
        executionCount++;
        return pixel1 > pixel2 ? pixel1 : pixel2;
    }

}

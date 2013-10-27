package com.nd.pgm.operator;

/**
 * Computes the thresholding operation
 * 
 * @author Nicolás Dobler
 * 
 */
public class ThresholdingOperator extends AbstractOperator {

    /**
     * Threshold value to use
     */
    private int threshold;

    /**
     * Output value to use if the input pixel excedes the threshold value
     */
    private int outputValue;

    /**
     * Creates an instance of a thresholding operation
     * 
     * @param threshold the threshold value to use in the operations
     */
    public ThresholdingOperator(int threshold, int outputValue) {
        this.threshold = threshold;
        this.outputValue = outputValue;
    }

    /**
     * Computes the thresholding operation over one pixel
     */
    @Override
    public int compute(int pixel) {
        return pixel >= threshold ? outputValue : 0;

    }

}

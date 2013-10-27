package com.nd.pgm.operator;

/**
 * Operator to normalize an input pixel
 * 
 * @author Nicolás Dobler
 */
public class NormalizationOperator extends AbstractOperator {

    /**
     * Image maximum value
     */
    private int imageMaxValue;

    /**
     * Normalization threshold
     */
    private int fullRangeValue;

    /**
     * Creates an instance of normalization operator using input arguments
     * 
     * @param imageMaxValue
     * @param fullRangeValue
     */
    public NormalizationOperator(int imageMaxValue, int fullRangeValue) {
        this.imageMaxValue = imageMaxValue;
        this.fullRangeValue = fullRangeValue;
    }

    /**
     * Computes the normalization operation over a single pixel.
     */
    @Override
    public int compute(int pixel) {
        return pixel * fullRangeValue / imageMaxValue;
    }

}

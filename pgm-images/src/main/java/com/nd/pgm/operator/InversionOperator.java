package com.nd.pgm.operator;

/**
 * Operation to calculate the inversion of an image
 * 
 * @author Nicolás Dobler
 */
public class InversionOperator extends AbstractOperator {

    /**
     * Value of max pixel
     */
    private final int maxValue;

    /**
     * Creates an instance of the operator to invert an image with pixels with a higher value of maxValue
     */
    public InversionOperator(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Invert the value of a pixel
     */
    @Override
    public int compute(int pixel) {
        return -pixel + maxValue;

    }

}

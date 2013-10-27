package com.nd.pgm.operator;

/**
 * Interface to implement operations over one or two pixels
 * 
 * @author Nicolás Dobler
 * 
 */
public interface IOperator {

    /**
     * Compute the value of an output pixel using two input values.
     * 
     * @param pixel1 first value
     * @param pixel2 second value
     * @return output pixel value
     */
    public int compute(int pixel1, int pixel2);

    /**
     * Compute the value of an output pixel given a single input
     * 
     * @param pixel the input pixel
     * @return the value of the output pixel
     */
    public int compute(int pixel);

}

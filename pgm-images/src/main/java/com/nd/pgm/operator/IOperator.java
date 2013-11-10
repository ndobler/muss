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
     * @param p1y pixel 1 y coordinate
     * @param p1x pixel 1 x coordinate
     * @param p2y pixel 2 y coordinate
     * @param p2x pixel 2 x coordinate
     * @return output pixel value
     */
    public int compute(int pixel1, int pixel2, int p1y, int p1x, int p2y, int p2x);

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

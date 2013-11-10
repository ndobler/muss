package com.nd.pgm.operator;

/**
 * Helper class to simplify implementation of operation classes as most of the operations only need to implement one of
 * the two methods defined in the interface. In the future this interface should be separated in two interfaces
 * 
 * @author Nicolás Dobler
 */
abstract class AbstractOperator implements IOperator {

    @Override
    public int compute(int pixel1, int pixel2, int p1y, int p1x, int p2y, int p2x) {
        return this.compute(pixel1, pixel2);
    }

    @Override
    public int compute(int pixel1, int pixel2) {
        assert false : "Should not get here";
        return 0;
    }

    @Override
    public int compute(int pixel) {
        assert false : "Should not get here";
        return 0;
    }

}

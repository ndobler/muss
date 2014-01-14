package com.nd;

import java.io.IOException;

import com.nd.pgm.PGMImage;
import com.nd.pgm.PGMImageException;

/**
 * Exercise 5.1 As discussed in class, proof that the alternated filter opening-closing (or closing-opening) is
 * idempotent using the properties of openings and closings.
 * 
 * 
 * @author Nicolás Dobler
 * 
 */
public class Exercise5 {

    public static void main(String[] args) {
        try {
            exercise5();
        } catch (IOException | PGMImageException e) {
            e.printStackTrace();
        }
    }

    private static void exercise5() throws PGMImageException, IOException {
        PGMImage img = new PGMImage("src/test/resources/immed_gray_inv.pgm");

        int size = 1;
        PGMImage test1 = img.imOpeningClosing(size);
        PGMImage test2 = test1.imOpeningClosing(size);
        System.out.println("Is opening/closing size " + size + " idempotent? " + test1.equals(test2));

        size = 10;
        test1 = img.imOpeningClosing(size);
        test2 = test1.imOpeningClosing(size);
        System.out.println("Is opening/closing size " + size + " idempotent? " + test1.equals(test2));

        size = 1;
        test1 = img.imClosingOpening(size);
        test2 = test1.imClosingOpening(size);
        System.out.println("Is opening/closing size " + size + " idempotent? " + test1.equals(test2));

        size = 10;
        test1 = img.imClosingOpening(size);
        test2 = test1.imClosingOpening(size);
        System.out.println("Is opening/closing size " + size + " idempotent? " + test1.equals(test2));

    }

}

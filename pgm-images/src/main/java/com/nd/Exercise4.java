package com.nd;

import java.io.IOException;

import com.nd.pgm.PGMImage;
import com.nd.pgm.PGMImageException;

/**
 * @author Nicolás Dobler
 */
public class Exercise4 {

    public static void main(String[] args) {
        try {
            System.out.println("Exercise 4.1: Openings and closings\n");
            exercise41();
            System.out.println("\nExercise 4.1: Alternated filtering\n");
            exercise42();
            System.out.println("\nExercise 4.1: Noise removal\n");
            exercise43();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PGMImageException e) {
            e.printStackTrace();
        }

    }

    /**
     * * Exercise 4.1 Implement the following functions to compute the morphological opening and closing with
     * structuring element:
     * 
     * imOpening (imageIn, size, connectivity, imageOut) imClosing (imageIn, size, connectivity, imageOut)
     * 
     * Note: assume 8-connectivity, i.e., the structuring element is a 3x3 square when the size is 1.
     * 
     * Note: the following images are attached: immed_gray_inv.pgm immed_gray_inv_20051123_ope1.pgm (opening of size 1,
     * 8-connectivity) immed_gray_inv_20051123_ope2.pgm (opening of size 2, 8-connectivity)
     * immed_gray_inv_20051123_clo1.pgm (closing of size 1, 8-connectivity) immed_gray_inv_20051123_clo2.pgm (closing of
     * size 2, 8-connectivity)
     * 
     * @throws IOException
     * @throws PGMImageException
     */
    private static void exercise41() throws IOException, PGMImageException {
        PGMImage img = new PGMImage("src/test/resources/immed_gray_inv.pgm");

        PGMImage testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_ope1.pgm");
        PGMImage outputImage = img.imOpening(1);
        outputImage.saveImage("target/ope1.pgm");
        System.out.println("Testing opening of size 1: " + outputImage.equals(testImg));

        testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_ope2.pgm");
        outputImage = img.imOpening(2);
        outputImage.saveImage("target/ope2.pgm");
        System.out.println("Testing opening of size 2: " + outputImage.equals(testImg));

        testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_clo1.pgm");
        outputImage = img.imClosing(1);
        outputImage.saveImage("target/clo1.pgm");
        System.out.println("Testing closing of size 2: " + outputImage.equals(testImg));

        testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_clo2.pgm");
        outputImage = img.imClosing(2);
        outputImage.saveImage("target/clo2.pgm");
        System.out.println("Testing closing of size 2: " + outputImage.equals(testImg));

    }

    /**
     * * Exercise 4.2 Implement the following functions to compute the alternated filters opening-closing and
     * closing-opening with structuring element.
     * 
     * imOpeningClosing (imageIn, size, connectivity, imageOut) --> opening (closing (I))
     * 
     * imClosingOpening (imageIn, size, connectivity, imageOut) --> closing (opening (I))
     * 
     * Note: assume 8-connectivity, i.e., the structuring element is a 3x3 square when the size is 1.
     * 
     * Note: the following images are attached:
     * 
     * immed_gray_inv.pgm
     * 
     * immed_gray_inv_20051123_clo2ope2.pgm (closing (opening (I)) with size 2, 8-connectivity)
     * immed_gray_inv_20051123_clo4ope4.pgm (closing (opening (I)) with size 4, 8-connectivity)
     * immed_gray_inv_20051123_ope2clo2.pgm (opening (closing (I)) with size 2, 8-connectivity)
     * immed_gray_inv_20051123_ope4clo4.pgm (opening (closing (I)) with size 4, 8-connectivity)
     * 
     * @throws IOException
     * @throws PGMImageException
     */
    private static void exercise42() throws IOException, PGMImageException {
        PGMImage img = new PGMImage("src/test/resources/immed_gray_inv.pgm");

        PGMImage testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_clo2ope2.pgm");
        PGMImage outputImage = img.imClosingOpening(2);
        outputImage.saveImage("target/clo2ope2.pgm");
        System.out.println("Testing clo ope 2: " + outputImage.equals(testImg));

        testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_clo4ope4.pgm");
        outputImage = img.imClosingOpening(4);
        outputImage.saveImage("target/clo4ope4.pgm");
        System.out.println("Testing clo ope 4: " + outputImage.equals(testImg));

        testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_ope2clo2.pgm");
        outputImage = img.imOpeningClosing(2);
        outputImage.saveImage("target/clo2ope2.pgm");
        System.out.println("Testing ope clo 2: " + outputImage.equals(testImg));

        testImg = new PGMImage("src/test/resources/immed_gray_inv_20051123_ope4clo4.pgm");
        outputImage = img.imOpeningClosing(4);
        outputImage.saveImage("target/clo4ope4.pgm");
        System.out.println("Testing ope clo 4: " + outputImage.equals(testImg));

    }

    /**
     * Exercise 4.3 Let I be the input image in file isn_256.pgm, which has a binary impulsive noise added
     * ("salt-and-pepper" noise). Let B be a structuring element square of size 3.
     * 
     * Compute: - opening_B (I) - closing_B (I) - closing_B (opening_B (I)) - opening_B (closing_B (I))
     * 
     * Indicate which are the two best filters to eliminate the noise in I.
     * 
     * Note: image isn_256.pgm is attached.
     */
    private static void exercise43() throws IOException, PGMImageException {
        PGMImage img = new PGMImage("src/test/resources/isn_256.pgm");

        img.imOpening(1).saveImage("target/bestFilter-opening3.pgm");
        img.imClosing(1).saveImage("target/bestFilter-closing3.pgm");
        img.imOpeningClosing(1).saveImage("target/bestFilter-opeclo3.pgm");
        img.imClosingOpening(1).saveImage("target/bestFilter-cloope3.pgm");

    }
}

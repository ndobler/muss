package com.nd;

import java.io.IOException;

import com.nd.pgm.PGMImage;
import com.nd.pgm.PGMImageException;

/**
 * Exercise 2.1 Implement the following function to compare two input images:
 * 
 * imAreEqual (imageIn1, imageIn2):this function returns true or false depending on whether the two input images
 * imageIn1 and imageIn2 are equal or not (the sizes of the two images are supposed to be the same)
 * 
 * Note: two images are equal if and only if the intensity value of every pixel (x,y) of imageIn1 is equal to the
 * intensity value of the same pixel (x,y) of imageIn2.
 * 
 * ----------------------------------------------------------------
 * 
 * Exercise 2.2 Implement the following functions to compare two input images:
 * 
 * imIsGreater (imageIn1, imageIn2): this function returns true if every pixel in imageIn has an intensity value greater
 * than or equal to its intensity value in imageIn2, and it returns false otherwise
 * 
 * imIsLessThan (imageIn1, imageIn2): this function returns true if every pixel in imageIn has an intensity value
 * smaller than or equal to its intensity value in imageIn2, and it returns false otherwise
 * ----------------------------------------------------------------
 * 
 * Exercise 2.3 Implement the following functions to compute the sup and inf of two input images:
 * 
 * imSup (imageIn1, imageIn2, imageOut): sup of images imageIn1 and imageIn2
 * 
 * imInf (imageIn1, imageIn2, imageOut): inf of images imageIn1 and imageIn2
 * ----------------------------------------------------------------
 * 
 * Exercise 2.4 Implement the following thresholding function:
 * 
 * imThresh (imageIn, value, imageOut) : the most typical thresholding or binarization function: a pixel p will have a
 * value of 255 in imageOut if its value in imageIn is greater or equal than that of value; otherwise, p will have a
 * value of 0
 * 
 * The image 'cam_74_threshold100.pgm' is the result of thresholding 'cam_74.pgm' at value 100.
 * ----------------------------------------------------------------
 * 
 * Exercise 2.5 Implement the linear normalization procedure that we discussed in class:
 * 
 * imNormalization (imageIn, imageOut) : linearly mapping the range in image1 to the complete range [0, 255] in imageOut
 * 
 * Apply imNormalization to the following gradient image in order to stretch its dynamic range:
 * micro24_20060309_grad.pgm
 * 
 * @author Nicolás Dobler
 * 
 */
public class Exercise2 {

    public static void main(String[] args) {
        try {
            equalTest();

            greaterThanLessThanTest();
            supAndInfTest();
            thresholdTest();
            normalizationTest();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (PGMImageException e) {
            e.printStackTrace();
        }

    }

    private static void normalizationTest() throws IOException, PGMImageException {
        PGMImage img = new PGMImage("src/test/resources/micro24_20060309_grad.pgm");
        PGMImage normalizedImage = img.getNormalizedImage();
        normalizedImage.saveImage("target/micro24_20060309_grad.pgm");
    }

    public static void thresholdTest() throws IOException {
        // threshold test
        PGMImage originalImage = new PGMImage("src/test/resources/cam_74.pgm");
        PGMImage outputImage = originalImage.imThresh(originalImage, 100);
        PGMImage testImage = new PGMImage("src/test/resources/cam_74_threshold100.pgm");
        System.out.println("Threshold test " + testImage.equals(outputImage));
    }

    public static void supAndInfTest() throws IOException, PGMImageException {
        PGMImage f1 = new PGMImage("src/test/resources/immed_gray_inv_20051123_clo1.pgm");
        PGMImage f2 = new PGMImage("src/test/resources/immed_gray_inv_20051123_ope1.pgm");

        // sup test
        PGMImage testImage = f1.imSup(f2);
        testImage.saveImage("target/suptest.pgm");

        // inf test
        testImage = f1.imInf(f2);
        testImage.saveImage("target/inftest.pgm");
    }

    private static void greaterThanLessThanTest() throws IOException, PGMImageException {
        PGMImage f1 = new PGMImage("src/test/resources/immed_gray_inv_20051123_clo1.pgm");
        PGMImage f2 = new PGMImage("src/test/resources/immed_gray_inv_20051123_ope1.pgm");
        PGMImage testImage = new PGMImage("src/test/resources/immed_gray_inv.pgm");

        // greater than test
        System.out.println("Greater than test: " + f1.isGreaterThan(testImage));

        // less than test
        System.out.println("Less than test: " + f2.isLessThan(testImage));
    }

    public static void equalTest() throws IOException, PGMImageException {
        String outputImage = "target/gran01_64.pgm";

        PGMImage f1 = new PGMImage("src/test/resources/gran01_64.pgm");
        f1.saveImage(outputImage);
        PGMImage f2 = new PGMImage(outputImage);

        // equal test
        System.out.println("Equal test: " + f1.equals(f2));

        // segunda imagen
        outputImage = "target/immed_gray_inv.pgm";

        f1 = new PGMImage("src/test/resources/immed_gray_inv.pgm");
        f1.saveImage(outputImage);
        f2 = new PGMImage(outputImage);

        // equal test
        System.out.println("Equal test: " + f1.equals(f2));

    }

}

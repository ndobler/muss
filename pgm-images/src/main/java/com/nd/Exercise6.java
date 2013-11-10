package com.nd;

import java.io.IOException;

import com.nd.pgm.PGMImage;
import com.nd.pgm.PGMImageException;
import com.nd.pgm.Pixel;
import com.nd.pgm.connectivity.IConnectivity.Connectivity;

/**
 * 
 * @author Nicolás Dobler
 */
public class Exercise6 {

    public static void main(String[] args) {
        try {
            exercise61();
        } catch (IOException | PGMImageException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exercise 6.1 Implement the following function that extracts the flat zone (piecewise-constant region) of a pixel:
     * 
     * imFlatZone (imageIn, (pix_x, pix_y), connectivity, label_flatzone, imageOut)
     * 
     * the flat zone of pixel (pix_x, pix_y) in imageIn will appear with value label_flatzone in imageOut, and the rest
     * of the image with value 0
     * 
     * Note: 8-connectivity can be assumed. Note: assume label_flatzone is value 255.
     * 
     * Note: to check imFlatZone: -immed_gray_inv_20051218_frgr4.pgm (input image)
     * -immed_gray_inv_20051218_frgr4_flatzone57_36.pgm (flat zone of pixel (column = 57, row = 36))
     * 
     * -gran01_64.pgm (input image) -gran01_64_flatzone0_0.pgm (flat zone of pixel (column = 0, row = 0))
     * 
     * @throws IOException
     * @throws PGMImageException
     */
    private static void exercise61() throws IOException, PGMImageException {
        PGMImage img = new PGMImage("src/test/resources/immed_gray_inv_20051218_frgr4.pgm");
        PGMImage testImg = new PGMImage("src/test/resources/immed_gray_inv_20051218_frgr4_flatzone57_36.pgm");
        PGMImage outputImage = img.imFlatZone(new Pixel(36, 57), Connectivity.EIGHTCONNECTIVITY, 255);
        outputImage.saveImage("target/fz1.pgm");
        System.out.println("Testing flatzone for immed_gray_inv_20051218_frgr4 : " + outputImage.equals(testImg));

        img = new PGMImage("src/test/resources/gran01_64.pgm");
        testImg = new PGMImage("src/test/resources/gran01_64_flatzone0_0.pgm");
        outputImage = img.imFlatZone(new Pixel(0, 0), Connectivity.EIGHTCONNECTIVITY, 255);
        outputImage.saveImage("target/fz2.pgm");
        System.out.println("Testing flatzone for gran01_64: " + outputImage.equals(testImg));

    }

}

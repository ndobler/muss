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
            exercise62();
            exercise63();
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

    /**
     * 
     Exercise 6.2 Implement the following function to compute the number of flat zones of an input image:
     * 
     * imFlatZonesNumber (imageIn, conectividad) result: number of flat zones
     * 
     * Note: internally, an auxiliary image of sufficient bit depth (16 bits or, better, 32 bits) should be used
     * 
     * Note: 8-connectivity can be assumed.
     * 
     * To check imFlatZonesNumber:
     * 
     * (a) immed_gray_inv_20051218_thresh127.pgm has 55 flat zones with 8-connectivity (with 4-connectivity, the number
     * of flat zones is also 55).
     * 
     * (b) the gray-level image immed_gray_inv.pgm has 12488 flat zones with 8-connectivity (with 4-connectivity, 16909
     * flat zones).
     * @throws IOException 
     */
    public static void exercise62() throws IOException {
        PGMImage img1 = new PGMImage("src/test/resources/immed_gray_inv_20051218_thresh127.pgm");
        PGMImage img2 = new PGMImage("src/test/resources/immed_gray_inv.pgm");
        
        System.out.println("La cantidad de flatzones en immed_gray_inv_20051218_thresh127 con 8connectivity es " + img1.imFlatZonesNumber(Connectivity.EIGHTCONNECTIVITY) );
        System.out.println("La cantidad de flatzones en immed_gray_inv_20051218_thresh127 con 4connectivity es " + img1.imFlatZonesNumber(Connectivity.FOURCONNECTIVITY) );
        System.out.println("La cantidad de flatzones en immed_gray_inv con 8connectivity es " + img2.imFlatZonesNumber(Connectivity.EIGHTCONNECTIVITY) );
        System.out.println("La cantidad de flatzones en immed_gray_inv con 4connectivity es " + img2.imFlatZonesNumber(Connectivity.FOURCONNECTIVITY) );
    }

    /**
     * Exercise 6.3 Implement the following functions to compute the regional maxima and minima of an input image:
     * 
     * imMaxima (imageIn, conectividad, imageOut) --> the regional maxima will appear with value 255 in imageOut, and
     * the rest of the image with value 0.
     * 
     * (Note: regional maximum: flat zone whose neighboring regions all have lower intensity values.)
     * 
     * imMinima (imageIn, conectividad, imageOut) --> the regional minima will appear with value 255 in imageOut, and
     * the rest of the image with value 0.
     * 
     * Note: 8-connectivity can be assumed.
     * 
     * To check these functions: -immed_gray_inv_20051218_frgr4.pgm (input image)
     * 
     * -immed_gray_inv_20051218_frgr4_max.pgm (regional maxima, with 8-connectivity)
     * 
     * -immed_gray_inv_20051218_frgr4_min.pgm (regional minima, with 8-connectivity)
     */
    public static void exercise63() {
    }
}

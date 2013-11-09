package com.nd;

import java.io.IOException;
import java.text.DecimalFormat;

import com.nd.pgm.PGMImage;
import com.nd.pgm.PGMImageException;
import com.nd.pgm.connectivity.EightConnectivity;
import com.nd.pgm.connectivity.IConnectivity;
import com.nd.pgm.operator.IOperator;

/**
 * Exercise 3.1 Implement the following functions to compute an erosion and a dilation using a 3x3 square structuring
 * element.
 * 
 * imErosionSquare3x3 (imageIn, imageOut)
 * 
 * imDilationSquare3x3 (imageIn, imageOut)
 * 
 * Note: The following images are attached to check the functions: immed_gray_inv.pgm (input image)
 * immed_gray_inv_20051123_ero1.pgm (erosion using a 3x3 square structuring element) immed_gray_inv_20051123_dil1.pgm
 * (dilation using a 3x3 square structuring element)
 * 
 * ----------------------------------------------------------------
 * 
 * Exercise 3.2 Implement the following functions to compute the morphological erosion and dilation with structuring
 * element:
 * 
 * imErosion (imageIn, size, imageOut)
 * 
 * imDilation (imageIn, size, imageOut)
 * 
 * Note: assume 8-connectivity, i.e., the structuring element is a 3x3 square when the size is 1, and, in general, a
 * (2i+1)x(2i+1) square when the size is i. Use the property of computing a dilation of size larger than 1 in terms of
 * elementary dilations of size 1 (and similarly for erosions: compute an erosion of size larger than 1 in terms of
 * elementary erosions of size 1).
 * 
 * Note: the following images are attached: immed_gray_inv.pgm (input image) immed_gray_inv_20051123_ero1.pgm (erosion
 * of size 1, 8-connectivity) immed_gray_inv_20051123_ero2.pgm (erosion of size 2, 8-connectivity)
 * immed_gray_inv_20051123_dil1.pgm (dilation of size 1, 8-connectivity) immed_gray_inv_20051123_dil2.pgm (dilation of
 * size 2, 8-connectivity)
 * 
 * ---------------------------------------------------------------
 * 
 * Exercise 3.3 a) Implement the image inversion operation.
 * 
 * imInversion (imageIn, imageOut)
 * 
 * inversion of values for example, if the range is [0, 255], the value x is mapped to -x + 255
 * 
 * Images immed_gray_inv.pgm and immed_gray.pgm are inverse of each other.
 * 
 * b) Use it in the duality expressions between the erosion and dilation to check that: ero_B (I) = inv (dil_B (inv
 * (I))) dil_B (I) = inv (ero_B (inv (I))) where B is the 3x3 square structuring element, and I is the image
 * immed_gray_inv.pgm
 * 
 * ---------------------------------------------------------------
 * 
 * Exercise 3.4 In this exercise we are going to compare the number of operations in two alternatives for computing a
 * morphological dilation with structuring element.
 * 
 * Let B be the MxM square structuring element. Let C be the 1xM 1-D horizontal structuring element. x...xXx...x (Note:
 * the number of pixels is M.) Let D be the Mx1 1-D vertical structuring element. x . . . x X x . . . x (Note: the
 * number of pixels is M.)
 * 
 * 'X' denotes the origin of coordinates or center of the structuring element.
 * 
 * It can be observed that B = dilate_C (D) = dilate_D (C).
 * 
 * Estimate the number or 'max' operations that must be computed in order to process a NxN square input image using the
 * following alternatives: dilate_B (I)) dilate_C(dilate_D (I)))
 * 
 * Assume that the sizes of B, C and D are, respectively: MxM, 1xM y Mx1. Border effects should not be considered for
 * simplicity, i.e., all image pixels should be treated in the same manner.
 * 
 * 
 * @author Nicolás Dobler
 */
public class Exercise3 {

    static class NumberOfOperationsCalculator extends EightConnectivity {

        private long operationsCount = 0;
        private DecimalFormat formatter = new DecimalFormat("###,###.##");

        @Override
        protected int computeOperator(IOperator operator, int pixel1, int pixel2) {
            operationsCount++;
            return 1;
        }

        public String getOperationsCount() {
            long res = operationsCount;
            operationsCount = 0;
            return formatter.format(res);
        }

    }

    public static void main(String[] args) {
        try {
            System.out.println("3x3 test\n");
            erosionAndDilation3x3Test();
            System.out.println("\n\nnxn test\n");
            erosionAndDilationNMTest();
            System.out.println("\nInversion test\n");
            inversionTest();
            System.out.println("\n\nCompare performance of erosion and dilation using a nxn structuring element or by using n operations with an se size 1\n");
            performanceComparisonTest();
            System.out.println("\nNumber of operations comparison\n");
            exercise34();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PGMImageException e) {
            e.printStackTrace();
        }

    }

    private static void erosionAndDilation3x3Test() throws IOException, PGMImageException {
        // read input
        PGMImage img = new PGMImage("src/test/resources/immed_gray_inv.pgm");

        // test erosion
        PGMImage outErosion = img.imErosionFB(1);
        PGMImage testErosion = new PGMImage("src/test/resources/immed_gray_inv_20051123_ero1.pgm");
        System.out.println("Resultado erosion size 1 " + outErosion.equals(testErosion));
        testErosion.saveImage("target/ero1.pgm");

        // test dilation
        PGMImage outDilation = img.imDilationFB(1);
        PGMImage testDilation = new PGMImage("src/test/resources/immed_gray_inv_20051123_dil1.pgm");
        System.out.println("Resultado dilation size 1 " + outDilation.equals(testDilation));
        testDilation.saveImage("target/dil1.pgm");
    }

    private static void erosionAndDilationNMTest() throws IOException, PGMImageException {
        // read input
        PGMImage img = new PGMImage("src/test/resources/immed_gray_inv.pgm");

        // test erosion

        PGMImage testErosion = new PGMImage("src/test/resources/immed_gray_inv_20051123_ero2.pgm");
        long time = System.currentTimeMillis();
        PGMImage outErosion = img.imErosionFB(2);
        long duration = System.currentTimeMillis() - time;
        System.out.println("Resultado erosion size 2 " + outErosion.equals(testErosion) + " in " + duration);

        time = System.currentTimeMillis();
        PGMImage outErosionOpt = img.imErosion(2);
        duration = System.currentTimeMillis() - time;
        System.out.println("Resultado erosion size 2 opt " + outErosionOpt.equals(testErosion) + " in " + duration);
        outErosionOpt.saveImage("target/ero2.pgm");

        // test dilation
        PGMImage testDilation = new PGMImage("src/test/resources/immed_gray_inv_20051123_dil2.pgm");

        time = System.currentTimeMillis();
        PGMImage outDilation = img.imDilationFB(2);
        duration = System.currentTimeMillis() - time;
        System.out.println("Resultado dilation size 2 " + outDilation.equals(testDilation) + " in " + duration);

        time = System.currentTimeMillis();
        PGMImage outDilationOpt = img.imDilation(2);
        duration = System.currentTimeMillis() - time;
        System.out.println("Resultado dilation size 2 opt " + outDilationOpt.equals(testDilation) + " in " + duration);

        outDilationOpt.saveImage("target/dil2.pgm");
    }

    private static void performanceComparisonTest() throws IOException, PGMImageException {
        // read input
        PGMImage img = new PGMImage("src/test/resources/immed_gray_inv.pgm");

        int size = 15;
        long time = System.currentTimeMillis();
        PGMImage outErosion = img.imErosionFB(size);
        long duration = System.currentTimeMillis() - time;
        System.out.println("erosion size " + size + " in " + duration);

        time = System.currentTimeMillis();
        PGMImage outErosionOpt = img.imErosion(size);
        duration = System.currentTimeMillis() - time;
        System.out.println("erosion size " + size + " opt in " + duration);
        outErosion.saveImage("target/ero" + size + ".pgm");
        outErosionOpt.saveImage("target/ero" + size + "opt.pgm");
        System.out.println("compare " + outErosion.equals(outErosionOpt));

        time = System.currentTimeMillis();
        PGMImage outDilation = img.imDilationFB(size);
        duration = System.currentTimeMillis() - time;
        System.out.println("dilation size " + size + " in " + duration);

        time = System.currentTimeMillis();
        PGMImage outDilationOpt = img.imDilation(size);
        duration = System.currentTimeMillis() - time;
        System.out.println("dilation size " + size + " opt in " + duration);

        outDilation.saveImage("target/dil" + size + ".pgm");
        outDilationOpt.saveImage("target/dil" + size + "opt.pgm");
        System.out.println("compare " + outDilation.equals(outDilationOpt));

    }

    /**
     * Exercise 3.4 In this exercise we are going to compare the number of operations in two alternatives for computing
     * a morphological dilation with structuring element.
     * 
     * Let B be the MxM square structuring element. Let C be the 1xM 1-D horizontal structuring element. x...xXx...x
     * (Note: the number of pixels is M.) Let D be the Mx1 1-D vertical structuring element. x . . . x X x . . . x
     * (Note: the number of pixels is M.)
     * 
     * 'X' denotes the origin of coordinates or center of the structuring element.
     * 
     * It can be observed that B = dilate_C (D) = dilate_D (C).
     * 
     * Estimate the number or 'max' operations that must be computed in order to process a NxN square input image using
     * the following alternatives: dilate_B (I)) dilate_C(dilate_D (I)))
     * 
     * Assume that the sizes of B, C and D are, respectively: MxM, 1xM y Mx1. Border effects should not be considered
     * for simplicity, i.e., all image pixels should be treated in the same manner.
     * 
     * @throws PGMImageException
     */
    private static void exercise34() throws PGMImageException {
        computeOperations(256, 1);
        computeOperations(256, 2);
        computeOperations(1024, 2);
        computeOperations(1024, 15);
        computeOperations(1024 * 5, 16);

    }

    private static void computeOperations(int imageSize, int seSize) throws PGMImageException {
        DecimalFormat formatter = new DecimalFormat("###,###.##");

        int seDimensions = 2 * seSize + 1;
        System.out.println("\nResultados para imagen de " + imageSize + "x" + imageSize + " y structuring element size " + seDimensions + "x" + seDimensions);
        NumberOfOperationsCalculator visitor = new NumberOfOperationsCalculator();

        eroDilSimulator(imageSize, 2 * seSize + 1, 2 * seSize + 1, visitor);
        System.out.println("El algoritmo completo necesitó " + visitor.getOperationsCount() + " operaciones");
        long miCalculo = imageSize * imageSize * seDimensions * seDimensions;
        System.out.println("El calculo sin extremos es de " + formatter.format(miCalculo));

        erosionDilationRecursiveNxM(imageSize, seSize, visitor);
        System.out.println("El algoritmo recursivo necesitó " + visitor.getOperationsCount() + " operaciones");
        miCalculo = 0;
        int i = seSize;
        while (i-- > 0) {
            miCalculo += imageSize * imageSize * 3 * 3;
        }
        System.out.println("El calculo sin extremos es de " + formatter.format(miCalculo));

        eroDilSimulator(imageSize, seDimensions, 1, visitor);
        eroDilSimulator(imageSize, 1, seDimensions, visitor);
        System.out.println("El algoritmo completo con dilaciones 1x" + seDimensions + " y " + seDimensions + "x1 necesitó " + visitor.getOperationsCount()
                + " operaciones");
        miCalculo = imageSize * imageSize * seDimensions * 2;
        System.out.println("El calculo sin extremos es de " + formatter.format(miCalculo));

        // hay algo mal en alguno de los que sigue a continuación, sea el cálculo o la prueba
        erosionDilationRecursive1MN1(imageSize, seDimensions, visitor);
        System.out.println("El algoritmo recursivo con " + seSize + " dilaciones 1x3 y 3x1 necesitó " + visitor.getOperationsCount() + " operaciones");
        miCalculo = 0;
        i = seSize;
        while (i-- > 0) {
            miCalculo += imageSize * imageSize * 3 * 2;
        }
        System.out.println("El calculo sin extremos es de " + formatter.format(miCalculo));
    }

    private static void erosionDilationRecursiveNxM(int imageSize, int size, IConnectivity visitor) throws PGMImageException {
        while (size-- > 0) {
            eroDilSimulator(imageSize, 3, 3, visitor);
        }
    }

    private static void erosionDilationRecursive1MN1(int imageSize, int size, IConnectivity visitor) throws PGMImageException {
        while (size-- > 0) {
            eroDilSimulator(imageSize, 1, 3, visitor);
            eroDilSimulator(imageSize, 3, 1, visitor);
        }
    }

    private static void eroDilSimulator(int imageSize, int sEWidth, int sEHeight, IConnectivity visitor) {
        int[][] id = new int[imageSize][imageSize];
        for (int i = 0; i < imageSize; i++) {
            for (int j = 0; j < imageSize; j++) {
                visitor.compute(id, i, j, sEHeight, sEWidth, null);
            }
        }
    }

    private static void inversionTest() throws IOException, PGMImageException {

        // read input and test image
        PGMImage originalImg = new PGMImage("src/test/resources/immed_gray.pgm");
        PGMImage testImg = new PGMImage("src/test/resources/immed_gray_inv.pgm");
        // compare if input image inverted is equal to test image
        System.out.println("Inversion operation test: " + originalImg.imInversion().equals(testImg));

        // ero_B (I) = inv (dil_B (inv(I)))
        PGMImage ero1 = originalImg.imErosion(1);
        PGMImage ero2 = originalImg.imInversion().imDilation(1).imInversion();
        System.out.println("Test ero_B (I) = inv (dil_B (inv(I))) --> " + ero1.equals(ero2));

        // dil_B (I) = inv (ero_B (inv (I)))
        PGMImage dil1 = originalImg.imDilation(1);
        PGMImage dil2 = originalImg.imInversion().imErosion(1).imInversion();
        System.out.println("Test dil_B (I) = inv (ero_B (inv (I))) --> " + dil1.equals(dil2));

    }
}

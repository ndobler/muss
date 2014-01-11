package com.nd.pgm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.nd.pgm.connectivity.EightConnectivity;
import com.nd.pgm.connectivity.FourConnectivity;
import com.nd.pgm.connectivity.IConnectivity;
import com.nd.pgm.connectivity.IConnectivity.Connectivity;
import com.nd.pgm.operator.IOperator;
import com.nd.pgm.operator.InfOperator;
import com.nd.pgm.operator.InversionOperator;
import com.nd.pgm.operator.NormalizationOperator;
import com.nd.pgm.operator.ReconstructFlatZone;
import com.nd.pgm.operator.SupOperator;
import com.nd.pgm.operator.ThresholdingOperator;

/**
 * Represents a PGM image.
 * 
 * @author Nicolás Dobler
 * 
 */
public class PGMImage {

    /**
     * Contains the magic number
     */
    private String magicNumber;

    /**
     * Contains the image comment
     */
    private String comment;

    /**
     * Contains the highest density value. It's not the actual value in this image.
     */
    private int highestDensityValue;

    /**
     * Contains the image data
     */
    private int[][] imageData;

    protected PGMImage() {
    }

    /**
     * Constructs an image with the parameters.
     */
    public PGMImage(String magicNumber, String comment, int highestDensityValue, int[][] imageData) {
        setImageData(magicNumber, comment, highestDensityValue, imageData);
    }

    /**
     * Creates new empty image with all the metadata passed as arguments
     */
    public PGMImage(String magicNumber, String comment, int height, int width, int highestDensityValue) {
        this(magicNumber, comment, highestDensityValue, new int[height][width]);
    }

    /**
     * Constructs new empty image with the same characteristics as input image.
     * 
     * @param imageIn the sample image
     */
    public PGMImage(PGMImage imageIn) {
        this(imageIn.magicNumber, imageIn.comment, imageIn.getHeight(), imageIn.getWidth(), imageIn.getHighestDensityValue());
    }

    /**
     * Constructs new empty image with the same characteristics as input image with another comment
     * 
     * @param imageIn the sample image
     */
    public PGMImage(PGMImage imageIn, String comment) {
        this(imageIn.magicNumber, comment, imageIn.getHeight(), imageIn.getWidth(), imageIn.getHighestDensityValue());
    }

    /**
     * Constructs image from filename
     * 
     * @param filename file with image to read
     * @throws IOException
     */
    public PGMImage(String filename) throws IOException {
        new PGMImageReader().readPGMFile(filename, this);
    }

    /**
     * Helper method to set image data. Used by PGMImageReader
     */
    protected void setImageData(String magicNumber, String comment, int highestDensityValue, int[][] imageData) {
        this.magicNumber = magicNumber;
        setComment(comment);
        this.highestDensityValue = highestDensityValue;
        this.imageData = imageData;
    }

    public String getMagicNumber() {
        return magicNumber;
    }

    public String getComment() {
        return comment;
    }

    /**
     * Sets and comment to this image and ensure it begins with #
     * 
     * @param comment the comment string
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : (comment.startsWith("#") ? comment : "# " + comment);
    }

    /**
     * Appends information to the comment. If it doesn't exist it creates one.
     * 
     * @param newComment the comment string
     */
    public void appendComment(String newComment) {
        assert newComment != null;
        if (this.comment == null)
            setComment(newComment);
        else
            this.comment = this.comment + ". " + newComment;
    }

    public int getHeight() {
        return imageData != null ? imageData.length : 0;
    }

    public int getWidth() {
        return imageData != null && imageData[0] != null ? imageData[0].length : 0;
    }

    public int getHighestDensityValue() {
        return highestDensityValue;
    }

    private int[][] getImageData() {
        return imageData;
    }

    /**
     * Helper method for writing files
     * 
     * @return a byte[][] with the image data
     * @throws PGMImageException if highestDensityValue > 256
     */
    protected byte[] getBinaryImageData() {
        byte[] id = new byte[getHeight() * getWidth()];
        for (int i = 0; i < this.imageData.length; i++) {
            int offset = i * imageData[0].length;
            for (int j = 0; j < this.imageData[i].length; j++) {
                id[offset + j] = (byte) imageData[i][j];
            }
        }
        return id;
    }

    /**
     * Saves image to file. If the file already exist it is overwritten.
     * 
     * @param fileName the filename to write
     * @throws IOException if there's a problem writing the file
     * @throws PGMImageException if highestDensityValue > 256
     */
    public void saveImage(String fileName) throws IOException, PGMImageException {
        new PGMImageWriter().writePGMFile(this, fileName);
    }

    /**
     * Compare if an image passed as an argument have equal size
     * 
     * @param img2 the image to compare
     * @return true if sizes are equal, false otherwise
     */
    private boolean equalSize(PGMImage img2) {
        return this.getHeight() == img2.getHeight() && this.getWidth() == img2.getWidth();
    }

    /**
     * Says if another image is comparable to this
     * 
     * @param anotherImage
     * @return true if both images are P5, have the same size and highest density value
     */
    private boolean isComparableTo(PGMImage anotherImage) {
        boolean comparable = true;
        comparable = comparable && this.getMagicNumber().equals("P5") && anotherImage.getMagicNumber().equals("P5");
        comparable = comparable && equalSize(anotherImage);
        comparable = comparable && anotherImage.getHighestDensityValue() == this.getHighestDensityValue();
        return comparable;
    }

    /**
     * Calculates the maximum value of the image.
     * 
     * Exercise 1.1
     * 
     * @return the greatest value of this image
     */
    public int getMaxValue() {
        int max = 0;
        for (int i = 0; i < this.imageData.length; i++) {
            for (int j = 0; j < this.imageData[i].length; j++) {
                max = this.imageData[i][j] > max ? this.imageData[i][j] : max;
            }
        }
        return max;
    }

    /**
     * Calculates the smallest value of the image.
     * 
     * Exercise 1.1
     * 
     * @return the smallest value of this image
     */
    public int getMinValue() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < imageData.length; i++) {
            for (int j = 0; j < imageData[i].length; j++) {
                min = imageData[i][j] < min ? imageData[i][j] : min;
            }
        }
        return min;
    }

    /**
     * Compares two images. Two images are equal if and only if the intensity value of every pixel (x,y) of imageIn1 is
     * equal to the intensity value of the same pixel (x,y) of imageIn2
     * 
     * Exercise 2.1
     * 
     * @return true if images are equal, false otherwise
     */
    @Override
    public boolean equals(Object pgmImage) {
        try {
            PGMImage pgmi = (PGMImage) pgmImage;
            int[][] id = pgmi.getImageData();
            if (isComparableTo(pgmi)) {
                for (int i = 0; i < this.imageData.length; i++) {
                    for (int j = 0; j < this.imageData[i].length; j++) {
                        if (imageData[i][j] != id[i][j])
                            return false;
                    }
                }
                return true;
            } else
                return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Compare two input images:
     * 
     * Exercise 2.2
     * 
     * @param img2 the image to compare
     * @throws PGMImageException if images are not of the same size
     * @returns true if every pixel in this has an intensity value greater than or equal to its intensity value in img2,
     *          and it returns false otherwise
     * 
     */
    public boolean isGreaterThan(PGMImage img2) throws PGMImageException {
        if (!isComparableTo(img2))
            throw new PGMImageException("Images are not the same size");
        int[][] imageData2 = img2.getImageData();
        for (int i = 0; i < imageData2.length; i++) {
            for (int j = 0; j < imageData2[i].length; j++) {
                if (this.imageData[i][j] < imageData2[i][j])
                    return false;
            }
        }
        return true;
    }

    /**
     * Compare two input images:
     * 
     * Exercise 2.2
     * 
     * @param img2 the image to compare
     * @throws PGMImageException if images are not of the same size
     * @returns true if every pixel in this image has an intensity value smaller than or equal to its intensity value in
     *          img2, and it returns false otherwise
     */
    public boolean isLessThan(PGMImage img2) throws PGMImageException {
        if (!isComparableTo(img2))
            throw new PGMImageException("Images are not the same size");
        int[][] imageData2 = img2.getImageData();
        for (int i = 0; i < imageData2.length; i++) {
            for (int j = 0; j < imageData2[i].length; j++) {
                if (this.imageData[i][j] > imageData2[i][j])
                    return false;
            }
        }
        return true;
    }

    /**
     * Compute the sup operation of the image with another image passed as an argument
     * 
     * Exercise 2.3
     * 
     * @param anotherImage another image to use in the sup operation
     * @return output supped image
     * @exception PGMImageException if images have different sizes or maximum depth value
     */
    public PGMImage imSup(PGMImage anotherImage) throws PGMImageException {
        if (!isComparableTo(anotherImage)) {
            throw new PGMImageException("Images are of different size or have different maximum depth value");
        }
        PGMImage outputImage = this.computeOperation(new SupOperator(), anotherImage);
        outputImage.appendComment("#SUP of two images");
        return outputImage;
    }

    /**
     * Compute the inf operation of two images.
     * 
     * Exercise 2.3
     * 
     * @param anotherImage to use in the inf operation
     * @return output inf image
     * @exception PGMImageException if images have different sizes or maximum depth value
     */
    public PGMImage imInf(PGMImage anotherImage) throws PGMImageException {
        if (!isComparableTo(anotherImage)) {
            throw new PGMImageException("Images are of different size or have different maximum depth value");
        }
        PGMImage outputImage = this.computeOperation(new InfOperator(), anotherImage);
        outputImage.appendComment("#INF of two images");
        return outputImage;
    }

    /**
     * The most typical thresholding or binarization function: a pixel p will have a value of 255 in imageOut if its
     * value in imageIn is greater or equal than that of value; otherwise, p will have a value of 0
     * 
     * Exercise 2.4
     * 
     * @param imageIn image to threshold
     * @param threshold threshold value
     * @return thresholded image
     */
    public PGMImage imThresh(PGMImage imageIn, int threshold) {
        PGMImage oi = this.computeOperation(new ThresholdingOperator(threshold, this.highestDensityValue));
        oi.appendComment("Thresholded to " + threshold);
        return oi;
    }

    /**
     * Normalize image to the full range of 0..highestDensityValue
     * 
     * Ercercise 2.5
     * 
     * @return the normalized image
     */
    public PGMImage getNormalizedImage() {
        int thisImageMaxValue = this.getMaxValue();
        PGMImage oi = this.computeOperation(new NormalizationOperator(thisImageMaxValue, this.highestDensityValue - 1));
        oi.appendComment("Normalized");
        return oi;

    }

    @Override
    public String toString() {
        return String.format("PGM File %1s Height: %2d Width %3d Highest Density %4d", this.magicNumber, this.getHeight(), this.getWidth(),
                this.highestDensityValue);
    }

    /**
     * Compute an erosion of an image using a structuring element
     * 
     * Exercise 3.1 and 3.2.
     * 
     * @param size structuring element size
     * @return the image with the operation applied
     * @throws PGMImageException
     */
    public PGMImage imErosionFB(int size) {
        int sEDimensions = 2 * size + 1;
        PGMImage oi = computeOperationWithStructuringElement(sEDimensions, sEDimensions, new EightConnectivity(), new InfOperator());
        oi.appendComment("Eroded with " + size + " sized structuring element");
        return oi;
    }

    /**
     * Compute a dilation of an image using a structuring element
     * 
     * Exercise 3.1 and 3.2.
     * 
     * @param size structuring element size
     * @return the image with the operation applied
     * @throws PGMImageException
     */
    public PGMImage imDilationFB(int size) {
        int sEDimensions = 2 * size + 1;
        PGMImage oi = computeOperationWithStructuringElement(sEDimensions, sEDimensions, new EightConnectivity(), new SupOperator());
        oi.appendComment("Dilated with " + size + " sized structuring element");
        return oi;
    }

    /**
     * Use a recursive algorith to compute an erosion of an image using a structuring element
     * 
     * Exercise 3.1 and 3.2.
     * 
     * @param size structuring element size
     * @return the image with the operation applied
     * @throws PGMImageException
     */
    public PGMImage imErosion(int size) throws PGMImageException {
        PGMImage oi = erosionDilation1MN1(size, new EightConnectivity(), new InfOperator());
        oi.appendComment("Eroded with size " + size + " structuring element");
        return oi;
    }

    /**
     * Use a recursive algorith to compute a dilation of an image using a structuring element
     * 
     * Exercise 3.1 and 3.2.
     * 
     * @param size structuring element size
     * @return the image with the operation applied
     * @throws PGMImageException
     */
    public PGMImage imDilation(int size) throws PGMImageException {
        PGMImage oi = erosionDilation1MN1(size, new EightConnectivity(), new SupOperator());
        oi.appendComment("Dilated with size " + size + " structuring element");
        return oi;
    }

    /**
     * Recursion method to calculate erotions and dilations with structuring elements
     * 
     * Exercise 3.1 and 3.2.
     * 
     * @param size structuring element size
     * @param visitor connectivity helper
     * @param operator operation to compute
     * @return the image with the operation applied
     * @throws PGMImageException
     */
    @SuppressWarnings("unused")
    private PGMImage erosionDilationRecursive(int size, IConnectivity visitor, IOperator operator) throws PGMImageException {
        assert size > 0 : "Can't apply less than size 1 operation";

        if (size == 1) {
            return computeOperationWithStructuringElement(3, 3, visitor, operator);
        } else {
            return computeOperationWithStructuringElement(3, 3, visitor, operator).erosionDilationRecursive(size - 1, visitor, operator);
        }
    }

    /**
     * Calculates erosions and dilations with NxM structuring elements dividing the operation in two 1xM and Nx1
     * operations.
     * 
     * Exercise 3.4
     * 
     * @param size structuring element size
     * @param visitor connectivity helper
     * @param operator operation to compute
     * @return the image with the operation applied
     * @throws PGMImageException
     */
    private PGMImage erosionDilation1MN1(int size, IConnectivity visitor, IOperator operator) throws PGMImageException {
        assert size > 0 : "Can't apply less than size 1 operation";

        int seSize = 2 * size + 1;
        return computeOperationWithStructuringElement(1, seSize, visitor, operator).computeOperationWithStructuringElement(seSize, 1, visitor, operator);
    }

    /**
     * Transverse an image to compute operations with structuring element
     * 
     * Exercise 3.1 and 3.2.
     * 
     * @param sEHeight height of structuring element
     * @param sEWidth width of structuring element
     * @param connectivityVisitor helper class to compute the connectivity
     * @param operator operation to compute
     * @return the image with the operation applied
     */
    private PGMImage computeOperationWithStructuringElement(int sEHeight, int sEWidth, IConnectivity connectivityVisitor, IOperator operator) {
        PGMImage outputImage = new PGMImage(this);
        int[][] od = outputImage.getImageData();
        for (int i = 0; i < this.imageData.length; i++) {
            for (int j = 0; j < this.imageData[i].length; j++) {
                od[i][j] = connectivityVisitor.compute(this.imageData, i, j, sEHeight, sEWidth, operator);
            }
        }
        return outputImage;
    }

    /**
     * Transverse an image to compute operations to each pixel.
     * 
     * (se podria haber utilizado el metodo anterior utilizando un structuringelement "size zero"?)
     * 
     * @param operator operation to compute
     * @return the image with the operation applied
     */
    private PGMImage computeOperation(IOperator operator) {
        PGMImage outputImage = new PGMImage(this);
        int[][] od = outputImage.getImageData();
        for (int i = 0; i < this.imageData.length; i++) {
            for (int j = 0; j < this.imageData[i].length; j++) {
                od[i][j] = operator.compute(this.imageData[i][j]);
            }
        }
        return outputImage;
    }

    /**
     * Transverse this image and another image passed as an argument to compute an operation
     * 
     * (se podria haber utilizado el metodo anterior utilizando un structuringelement "size zero"?)
     * 
     * @param operator operation to compute
     * @param img2 another image
     * @return the image with the operation applied
     */
    private PGMImage computeOperation(IOperator operator, PGMImage img2) {
        PGMImage outputImage = new PGMImage(this);
        int[][] outputImageData = outputImage.getImageData();
        int[][] imageData2 = img2.getImageData();
        for (int i = 0; i < outputImageData.length; i++) {
            for (int j = 0; j < outputImageData[i].length; j++) {
                outputImageData[i][j] = operator.compute(imageData[i][j], imageData2[i][j]);
            }
        }
        return outputImage;
    }

    /**
     * Invert an image.
     * 
     * Exercise 3.3a
     * 
     * @return the inverted image
     */
    public PGMImage imInversion() {
        PGMImage oi = this.computeOperation(new InversionOperator(this.highestDensityValue));
        oi.appendComment("Inverted");
        return oi;
    }

    /**
     * Returns a filtered image with an opening of this image of size size.
     * 
     * @param size size of the filter
     * @return the filtered image
     */
    public PGMImage imOpening(int size) throws PGMImageException {
        return imInversion().imDilation(size).imErosion(size).imInversion();
    }

    /**
     * Returns a filtered image with a closing of this image of size size.
     * 
     * @param size size of the filter
     * @return the filtered image
     */
    public PGMImage imClosing(int size) throws PGMImageException {
        return imInversion().imErosion(size).imDilation(size).imInversion();
    }

    public PGMImage imOpeningClosing(int size) throws PGMImageException {
        return this.imClosing(size).imOpening(size);
    }

    public PGMImage imClosingOpening(int size) throws PGMImageException {
        return this.imOpening(size).imClosing(size);
    }

    public PGMImage imFlatZone(Pixel p, Connectivity connectivity, int labelFlatZone) {
        PGMImage outputImage = new PGMImage(this);
        int[][] od = outputImage.getImageData();
        od[p.getY()][p.getX()] = labelFlatZone;
        IConnectivity ic = connectivity == Connectivity.EIGHTCONNECTIVITY ? new EightConnectivity() : new FourConnectivity();
        Queue<Pixel> q = new LinkedList<>();
        q.add(p);
        IOperator operator = new ReconstructFlatZone(od, q, labelFlatZone);
        while (q.peek() != null) {
            Pixel currentPixel = q.poll();
            ic.compute(this.getImageData(), currentPixel.getY(), currentPixel.getX(), 3, 3, operator);
        }
        return outputImage;
    }

    public int[][] imFlatZones(Connectivity connectivity) {
        PGMImage outputImage = new PGMImage(this);
        int[][] od = outputImage.getImageData();
        IConnectivity ic = connectivity == Connectivity.EIGHTCONNECTIVITY ? new EightConnectivity() : new FourConnectivity();
        Queue<Pixel> q = new LinkedList<>();
        int currentFlatZone = 0;

        for (int i = 0; i < od.length; i++) {
            for (int j = 0; j < od[i].length; j++) {
                if (od[i][j] == 0) {
                    q.add(new Pixel(i, j));
                    IOperator operator = new ReconstructFlatZone(od, q, ++currentFlatZone);
                    while (q.peek() != null) {
                        Pixel currentPixel = q.poll();
                        ic.compute(this.getImageData(), currentPixel.getY(), currentPixel.getX(), 3, 3, operator);
                    }

                }
            }
        }

        return od;
    }

    public int imFlatZonesNumber(Connectivity connectivity) {
        int[][] fz = imFlatZones(connectivity);

        int max = 0;
        for (int i = 0; i < fz.length; i++) {
            for (int j = 0; j < fz[i].length; j++) {
                max = max > fz[i][j] ? max : fz[i][j];
            }
        }
        return max;
    }

}
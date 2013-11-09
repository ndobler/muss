package com.nd.pgm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Helper class to read images form files
 * 
 * @author Nicolás Dobler
 * 
 */
class PGMImageReader {

    protected PGMImageReader() {
    }

    /**
     * Reads an image from file
     * 
     * @param filename the filename to read
     * @param image an empty image to read the data to
     * @throws IOException if there's a problem reading the image
     */
    public void readPGMFile(String filename, PGMImage image) throws IOException {
        InputStream fis = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(filename));
            String magicNumber = this.readLine(fis);
            if (!"P5".equalsIgnoreCase(magicNumber))
                throw new IOException("Image is not P5");

            // move(fis);
            fis.mark(4000);
            String comment = readLine(fis);
            // si el primer caracter no es # entonces vuelvo a releer la linea ya que no hay comentario y lo que lei era
            // el tamaño
            if (!comment.startsWith("#")) {
                fis.reset();
                comment = null;
            }
            int width = readNumber(fis);
            int height = readNumber(fis);
            int highestDensityValue = readNumber(fis);
            if (highestDensityValue >= 65536)
                throw new IOException("Highest density value must be less than 65536");

            int[][] imageData = readImageData(fis, height, width);
            image.setImageData(magicNumber, comment, highestDensityValue, imageData);
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                }
        }

    }

    /**
     * Reads the image data
     * 
     * @param fis input stream to read from
     * @param height image height
     * @param width image width
     * @return data matrix with the image read
     * @throws IOException if there's a problem reading the input stream
     */
    private int[][] readImageData(InputStream fis, int height, int width) throws IOException {
        int[][] id = new int[height][width];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                id[h][w] = fis.read();
            }
        }
        return id;
    }

    /**
     * Read a string line. Finishes reading when it reaches a \n or \r character
     * 
     * @param fis input stream to read from
     * @return the line as a string
     * @throws IOException if there's a problem reading the input stream
     */
    private String readLine(InputStream fis) throws IOException {
        int c;
        ByteBuffer bb = ByteBuffer.allocate(4000);
        while ((c = fis.read()) != -1) {
            // creo un string a partir del byte leido
            String charString = new String(new char[] { (char) c });
            // comparon con delimitadores de linea para determinar si terminé de leer la linea
            if ("\n".equals(charString) || "\r".equals(charString) || " ".equals(charString))
                break;
            else
                bb.put((byte) c);
        }
        String s = new String(bb.array()).trim();
        return s;
    }

    /**
     * Read a number from the input stream
     * 
     * @param fis the input stream to read from
     * @return the number
     * @throws IOException if there's a problem reading the input stream
     */
    private int readNumber(InputStream fis) throws IOException {
        int c;
        byte[] widthByte = new byte[10];
        int i = 0;
        while (!Character.isWhitespace(c = fis.read())) {
            widthByte[i++] = (byte) c;
        }
        String s = new String(widthByte).trim();
        return Integer.parseInt(s);
    }

}
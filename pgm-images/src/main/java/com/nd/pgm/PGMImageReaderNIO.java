package com.nd.pgm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Lee una imagen usando las API java NIO. NO FUNCIONA
 * 
 * @author Nicolás Dobler
 * 
 */
public class PGMImageReaderNIO {

    public PGMImageReaderNIO() {
    }

    public void readPGMFile(String fileName, PGMImage image) throws IOException {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            String magicNumber = null, comment = null;
            int width, height, highestDensityValue;

            magicNumber = s.nextLine();
            if (!"P5".equalsIgnoreCase(magicNumber))
                throw new IOException("File is not a P5 PGM");

            if (!s.hasNextInt()) {
                comment = s.nextLine().trim();
            }

            width = s.nextInt();
            height = s.nextInt();
            highestDensityValue = s.nextInt();
            if (highestDensityValue > 256)
                throw new IOException("Highest Density Value > 256 not supported");

            int[][] id = new int[height][width];
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    id[h][w] = s.nextByte();
                }
            }

            image.setImageData(magicNumber, comment, highestDensityValue, id);
        }
    }
}
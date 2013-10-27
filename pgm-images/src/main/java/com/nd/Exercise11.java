package com.nd;

import java.io.IOException;

import com.nd.pgm.PGMImage;

/**
 * Exercise 1.1
 * 
 * @author Nicolás Dobler
 * 
 */
public class Exercise11 {

	/** it returns the greatest value of imageIn */
	private static int imMaxValue(PGMImage file) {
		return file.getMaxValue();
	}

	/** it returns the smallest value of imageIn */
	private static int imMinValue(PGMImage file) {
        int min = file.getMinValue();
		return min;
	}

	public static void main(String[] args) {
		try {
			String fileName = "src/test/resources/gran01_64.pgm";
			PGMImage f = new PGMImage(fileName);
			System.out.println(f);
			System.out.println(fileName + " min is " + imMinValue(f));
			System.out.println(fileName + " max is " + imMaxValue(f));

			fileName = "src/test/resources/cam_74.pgm";
			f = new PGMImage(fileName);
			System.out.println(f);
			System.out.println(fileName + " min is " + imMinValue(f));
			System.out.println(fileName + " max is " + imMaxValue(f));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

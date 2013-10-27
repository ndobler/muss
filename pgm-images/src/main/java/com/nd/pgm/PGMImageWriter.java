package com.nd.pgm;

import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Helper class to write images to files
 * 
 * @author Nicolás Dobler
 * 
 */
class PGMImageWriter {

    protected PGMImageWriter() {
    }

    /**
     * Saves an image to a file. Works with JDK 7+
     * 
     * @param image the image to save
     * @param file where to save it
     * @throws IOException if there's an error writing the image
     */
    public void writePGMFile(PGMImage image, String file) throws IOException {
        Path p = FileSystems.getDefault().getPath(file);
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE))) {
            byte[] header = getHeader(image);
            out.write(header);
            byte[] id = image.getBinaryImageData();
            out.write(id);
        }
    }

    /**
     * Saves an image to file. Works with JDK versions older than 7
     * 
     * @param image the image to save
     * @param file where to save it
     * @throws IOException if there's an error writing the image
     */
    public void writePGMFileLegacy(PGMImage image, String file) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] header = getHeader(image);
            fos.write(header);
            byte[] id = image.getBinaryImageData();
            fos.write(id);
            fos.close();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException ex) {
                }
        }

    }

    /**
     * Helper method to construct the image header
     * 
     * @param image input image
     * @return the ascii header as a byte array
     */
    private byte[] getHeader(PGMImage image) {
        // works with mac and windows. I don't know why system.getPreferece("line.separator") doesn't work.
        final String eoldelimiter = "\n";
        String header;
        if (image.getComment() != null) {
            assert image.getComment().startsWith("#");
            header = image.getMagicNumber() + eoldelimiter + image.getComment() + eoldelimiter + image.getWidth() + " " + image.getHeight() + eoldelimiter
                    + image.getHighestDensityValue() + eoldelimiter;
        } else {
            header = image.getMagicNumber() + eoldelimiter + image.getWidth() + " " + image.getHeight() + eoldelimiter + image.getHighestDensityValue()
                    + eoldelimiter;
        }
        Charset cs = Charset.forName("US-ASCII");
        return header.getBytes(cs);
    }

}
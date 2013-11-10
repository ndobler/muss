package com.nd.pgm;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class PGMImageReaderTest {

    @Test
    public void testReadPGMFile() {
        Path source = Paths.get("src/test/resources");
        Path des = Paths.get("target/readtest");
        try {
            Files.createDirectories(des);
        } catch (IOException e) {
            fail("No puedo crear el directorio de prueba");
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source, "*.pgm")) {
            for (Path file : stream) {
                try {
                    System.out.println("Reading " + file);
                    PGMImage image = new PGMImage(file.toString());
                    Path destFile = Paths.get("target/readtest/", file.getFileName().toString());
                    image.saveImage(destFile.toString());
                    PGMImage testImage = new PGMImage(destFile.toString());
                    org.junit.Assert.assertEquals(image, testImage);
                } catch (IOException e) {
                    System.err.println(e);
                    fail("error de lectura");
                }
            }
        } catch (IOException | DirectoryIteratorException | PGMImageException x) {
            System.err.println(x);
            fail("error de lectura");
        }
    }

}

package com.nd.pgm;

public class Pixel {

    private int y;
    private int x;

    public Pixel(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Pixel anotherPixel = (Pixel) obj;
        return anotherPixel.getX() == x && anotherPixel.getY() == y;
    }

    @Override
    public String toString() {
        return "[" + y + "," + x + "]";
    }

}

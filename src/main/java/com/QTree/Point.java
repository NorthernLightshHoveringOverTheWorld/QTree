package com.QTree;


public class Point<T>  {
    public final double x;
    public final double y;
    public final T value;

    public Point(double x, double y, T value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Point{(%.3f,%.3f) -> %s}", x, y, String.valueOf(value));
    }
}


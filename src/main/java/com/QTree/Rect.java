package com.QTree;

import java.io.Serializable;
public class Rect implements Serializable {
    private static final long serialVersionUID = 1L;
    public final double x; // центр
    public final double y;
    public final double halfWidth;
    public final double halfHeight;

    public Rect(double centerX, double centerY, double halfWidth, double halfHeight) {
        if (halfWidth <= 0 || halfHeight <= 0) throw new IllegalArgumentException("half sizes must be > 0");
        this.x = centerX;
        this.y = centerY;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    public boolean contains(double px, double py) {
        return px >= (x - halfWidth) && px <= (x + halfWidth) && py >= (y - halfHeight) && py <= (y + halfHeight);
    }

    public boolean intersects(Rect other) {
        return !(other.x - other.halfWidth > this.x + this.halfWidth ||
                other.x + other.halfWidth < this.x - this.halfWidth ||
                other.y - other.halfHeight > this.y + this.halfHeight ||
                other.y + other.halfHeight < this.y - this.halfHeight);
    }

    @Override
    public String toString() {
        return String.format("Rect{center=(%.3f,%.3f), hw=%.3f, hh=%.3f}", x, y, halfWidth, halfHeight);
    }
}


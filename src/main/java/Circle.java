import java.util.ArrayList;
import java.util.List;

/**
 * Класс для круга — область поиска по радиусу
 */
public class Circle {
    public final double x;
    public final double y;
    public final double radius;

    public Circle(double x, double y, double radius) {
        if (radius < 0) throw new IllegalArgumentException("Radius must be non-negative");
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean contains(Point<?> p) {
        double dx = p.x - x;
        double dy = p.y - y;
        return dx * dx + dy * dy <= radius * radius;
    }
}


import java.io.Serializable;

/** Обертка для точки с координатами и значением */
public class Point<T> implements Serializable {
    private static final long serialVersionUID = 1L;
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


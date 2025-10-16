import java.QTree.Circle;
import java.util.List;



public class Main {
    public static void main(String[] args) {
        QuadTree<String> qt = new QuadTree<>(new Rect(0, 0, 50, 50), 4);


        System.out.println("=== Вставка точек ===");
        qt.insert("A", -10, -10);
        qt.insert("B", 10, -10);
        qt.insert("C", -10, 10);
        qt.insert("D", 10, 10);
        qt.insert("E", 0, 0);


        List<Point<String>> allPoints = qt.queryRange(new Rect(0, 0, 50, 50));
        System.out.println("Точки в дереве после вставки:");
        for (Point<String> p : allPoints) {
            System.out.println(p);
        }


        System.out.println("\n=== Поиск точек в диапазоне [-15,-15] до [5,5] ===");
        Rect range = new Rect(-5, -5, 10, 10);
        List<Point<String>> found = qt.queryRange(range);
        for (Point<String> p : found) {
            System.out.println("Найдена точка: " + p);
        }

        System.out.println("\n=== Поиск точек внутри круга радиусом 12 вокруг (0,0) ===");
        Circle circle = new Circle(0, 0, 12);
        List<Point<String>> inCircle = qt.queryCircle(circle);
        for (Point<String> p : inCircle) {
            System.out.println("Внутри круга: " + p);
        }


        System.out.println("\n=== Поиск 3 ближайших точек к (0,0) ===");
        List<Point<String>> nearest = qt.nearest(0, 0, 3);
        for (Point<String> p : nearest) {
            System.out.println("Ближайшая точка: " + p);
        }


        System.out.println("\n=== Удаление точки (0,0) ===");
        qt.remove("E", 0, 0);
        allPoints = qt.queryRange(new Rect(0, 0, 50, 50));
        System.out.println("Точки в дереве после удаления:");
        for (Point<String> p : allPoints) {
            System.out.println(p);
        }


        System.out.println("\n=== Очистка дерева ===");
        qt.clear();
        allPoints = qt.queryRange(new Rect(0, 0, 50, 50));
        System.out.println("Точки после очистки дерева: " + allPoints.size());
    }
}

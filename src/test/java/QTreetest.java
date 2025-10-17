
import com.QTree.*;
import org.junit.jupiter.api.*;
import java.util.List;

 class QuadTreeTest {
    private QuadTree<String> qt;
     @BeforeEach
     void setUp() {
         qt = new QuadTree<>(new Rect(0, 0, 50, 50), 4);
         qt.insert("A", -10, -10);
         qt.insert("B", 10, -10);
         qt.insert("C", -10, 10);
         qt.insert("D", 10, 10);
         qt.insert("E", 0, 0);
     }




// ---------- Nearest ----------


    @Test
    void testNearestPoints() {
        List<Point<String>> nearest = qt.nearest(0, 0, 3);
        Assertions.assertEquals(3, nearest.size(), "Должны быть найдены 3 ближайшие точки");
        Assertions.assertEquals("E", nearest.get(0).value, "Первая (ближайшая) должна быть E");
    }


    @Test
    void testNearestSingle() {
        List<Point<String>> nearest = qt.nearest(0, 0, 1);
        Assertions.assertEquals(1, nearest.size());
        Assertions.assertEquals("E", nearest.get(0).value);
    }


    @Test
    void testNearestAll() {
        List<Point<String>> nearest = qt.nearest(0, 0, 10);
        Assertions.assertEquals(5, nearest.size(), "Запрос большего количества должен вернуть максимум 5 точек");
    }


// ---------- Rect ----------


    @Test
    void testRectContains() {
        Rect r = new Rect(0, 0, 10, 10);
        Assertions.assertTrue(r.contains(5, 5), "Точка (5,5) должна быть внутри прямоугольника");
        Assertions.assertFalse(r.contains(15, 5), "Точка (15,5) должна быть вне прямоугольника");
    }


    @Test
    void testRectIntersects() {
        Rect r1 = new Rect(0, 0, 10, 10);
        Rect r2 = new Rect(5, 0, 10, 10);
        Rect r3 = new Rect(30, 0, 10, 10);


        Assertions.assertTrue(r1.intersects(r2), "r1 и r2 должны пересекаться");
        Assertions.assertFalse(r1.intersects(r3), "r1 и r3 не должны пересекаться");
    }


// ---------- Circle ----------


    @Test
    void testCircleContains() {
        Circle c = new Circle(0, 0, 10);
        Assertions.assertTrue(c.contains(new Point<>(5, 5, null)),
                "Точка (5,5) должна быть внутри круга радиуса 10");
        Assertions.assertFalse(c.contains(new Point<>(10, 10, null)),
                "Точка (10,10) должна быть вне круга радиуса 10");
    }

    @Test
    void testCircleBorderCase() {
        Circle c = new Circle(0, 0, 10);
        Assertions.assertTrue(c.contains(new Point<>(10, 0, null)),
                "Точка на границе круга считается внутри");
    }

    @Test
    void testCircleNoPoints() {
        Circle c = new Circle(100, 100, 5);
        List<Point<String>> found = qt.queryCircle(c);
        Assertions.assertTrue(found.isEmpty(), "Круг вне границ не должен возвращать точки");
    }

    @Test
    void testCircleContainsNegativeCoordinates() {
        Circle c = new Circle(0, 0, 15);
        Assertions.assertTrue(c.contains(new Point<>(-10, -10, null)),
                "Отрицательные координаты должны корректно обрабатываться");
    }

     @Test
     void testQueryCircleEdgeCases() {

         Circle largeCircle = new Circle(0, 0, 100);
         List<Point<String>> all = qt.queryCircle(largeCircle);
         Assertions.assertEquals(5, all.size(), "Большой круг должен находить все точки");
     }
}


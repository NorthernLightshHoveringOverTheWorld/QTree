import org.junit.jupiter.api.*;
import java.util.List;
import com.QTree.*;
class QuadTreeTest {

    QuadTree<String> qt;

    @BeforeEach
    void setUp() {
        qt = new QuadTree<>(new Rect(0, 0, 50, 50), 4);
        qt.insert("A", -10, -10);
        qt.insert("B", 10, -10);
        qt.insert("C", -10, 10);
        qt.insert("D", 10, 10);
        qt.insert("E", 0, 0);
    }

    @Test
    void testInsertAndSize() {
        Assertions.assertEquals(5, qt.size(), "Размер дерева должен быть 5");
    }

    @Test
    void testRemove() {
        qt.remove("A", -10, -10);
        Assertions.assertEquals(4, qt.size(), "После удаления размер должен быть 4");
    }

    @Test
    void testClear() {
        qt.clear();
        Assertions.assertEquals(0, qt.size(), "После clear дерево должно быть пустым");
    }

    @Test
    void testQueryRange() {
        Rect range = new Rect(0, 0, 15, 15);
        List<Point<String>> found = qt.queryRange(range);
        Assertions.assertEquals(5, found.size(), "В радиусе 15 должно быть найдено 5 точек");
    }

    @Test
    void testNearest() {
        List<Point<String>> nearest = qt.nearest(0, 0, 3);
        Assertions.assertEquals(3, nearest.size());
        Assertions.assertEquals("E", nearest.get(0).value, "Ближайшая точка должна быть E");
    }

    @Test
    void testCircleQuery() {
        Circle circle = new Circle(0, 0, 12);
        List<Point<String>> found = qt.queryCircle(circle);
        Assertions.assertEquals(1, found.size(), "В радиусе 12 должна быть найдена 1 точка (E)");
    }
}


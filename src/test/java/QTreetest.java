
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

     @Test
     void testInsertAndSize() {
         Assertions.assertEquals(5, qt.size(), "Размер дерева должен быть 5 после вставок");
         boolean result = qt.insert("F", 60, 60);
         Assertions.assertFalse(result, "Точка вне границ не должна вставляться");
         qt.insert("G", 20, 20);
         Assertions.assertEquals(6, qt.size(), "Размер должен увеличиться после добавления новой точки");
     }


     @Test
     void testDuplicateInsert() {
         boolean inserted = qt.insert("E", 0, 0);
         Assertions.assertTrue(inserted, "Можно вставить дубликат, если координаты те же");
         Assertions.assertTrue(qt.size() >= 5, "Размер не должен уменьшиться после вставки дубликата");
     }


     @Test
     void testRemoveExistingPoint() {
         boolean removed = qt.remove("A", -10, -10);
         Assertions.assertTrue(removed, "Точка A должна быть удалена");
         Assertions.assertEquals(4, qt.size(), "После удаления A должно остаться 4 точки");
     }


     @Test
     void testRemoveNonExistingPoint() {
         boolean removed = qt.remove("Z", 100, 100);
         Assertions.assertFalse(removed, "Удаление несуществующей точки должно вернуть false");
     }


     @Test
     void testClear() {
         qt.clear();
         Assertions.assertEquals(0, qt.size(), "После clear дерево должно быть пустым");
         Assertions.assertTrue(qt.queryRange(new Rect(0, 0, 50, 50)).isEmpty(), "После clear дерево пустое");
     }


     @Test
     void testSizeOnEmptyTree() {
         QuadTree<String> empty = new QuadTree<>(new Rect(0, 0, 10, 10), 2);
         Assertions.assertEquals(0, empty.size(), "Пустое дерево должно иметь размер 0");
     }



     @Test
     void testQueryRangeInside() {
         Rect range = new Rect(0, 0, 15, 15);
         List<Point<String>> found = qt.queryRange(range);
         Assertions.assertEquals(5, found.size(), "В пределах 15x15 должно быть найдено 5 точек");
     }


     @Test
     void testQueryRangeOutside() {
         Rect range = new Rect(100, 100, 10, 10);
         List<Point<String>> found = qt.queryRange(range);
         Assertions.assertTrue(found.isEmpty(), "Вне области не должно быть найдено точек");
     }


     @Test
     void testQueryRangePartialOverlap() {
         Rect range = new Rect(10, 10, 20, 20);
         List<Point<String>> found = qt.queryRange(range);
         Assertions.assertTrue(found.stream().anyMatch(p -> p.value.equals("D")), "Точка D должна быть найдена");
     }
}


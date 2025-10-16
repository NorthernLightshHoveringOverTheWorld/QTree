import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class QuadTree<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Node root;
    private int size = 0;

    public QuadTree(Rect boundary, int nodeCapacity) {
        Objects.requireNonNull(boundary, "boundary");
        if (nodeCapacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.root = new Node(boundary, nodeCapacity);
    }

    public int size() { return size; }

    public boolean insert(T item, double x, double y) {
        Objects.requireNonNull(item, "item");
        if (!root.boundary.contains(x, y)) return false;
        boolean inserted = root.insert(new Point<>(x, y, item));
        if (inserted) size++;
        return inserted;
    }

    public boolean remove(T item, double x, double y) {
        Objects.requireNonNull(item);
        boolean removed = root.remove(item, x, y);
        if (removed) size--;
        return removed;
    }

    public void clear() {
        root.clear();
        size = 0;
    }

    public List<Point<T>> queryRange(Rect range) {
        Objects.requireNonNull(range);
        List<Point<T>> found = new ArrayList<>();
        root.queryRange(range, found);
        return found;
    }

    public List<Point<T>> queryCircle(Circle circle) {
        List<Point<T>> result = new ArrayList<>();
        queryCircleNode(root, circle, result);
        return result;
    }


    private void queryCircleNode(Node node, Circle circle, List<Point<T>> result) {
        if (!node.boundary.intersects(new Rect(circle.x, circle.y, circle.radius, circle.radius))) return;
        for (Point<T> p : node.points) {
            if (circle.contains(p)) result.add(p);
        }
        if (!node.divided) return;
        queryCircleNode(node.nw, circle, result);
        queryCircleNode(node.ne, circle, result);
        queryCircleNode(node.sw, circle, result);
        queryCircleNode(node.se, circle, result);
    }

    public List<Point<T>> nearest(double x, double y, int k) {
        if (k <= 0) throw new IllegalArgumentException("k must be > 0");
        double maxDim = Math.max(root.boundary.halfWidth, root.boundary.halfHeight);
        double boxHalf = Math.max(1e-6, Math.min(maxDim, maxDim / 8.0));
        List<Point<T>> candidates = new ArrayList<>();
        while (true) {
            Rect r = new Rect(x, y, boxHalf, boxHalf);
            candidates = queryRange(r);
            if (candidates.size() >= k || boxHalf >= maxDim) break;
            boxHalf *= 2.0;
        }
        candidates.sort((a, b) -> Double.compare(dist2(a.x, a.y, x, y), dist2(b.x, b.y, x, y)));
        return candidates.size() > k ? new ArrayList<>(candidates.subList(0, k)) : candidates;
    }

    private static double dist2(double ax, double ay, double bx, double by) {
        double dx = ax - bx, dy = ay - by; return dx * dx + dy * dy;
    }

    private class Node implements Serializable {
        final Rect boundary;
        final int capacity;
        final List<Point<T>> points;
        boolean divided = false;
        Node nw, ne, sw, se;

        Node(Rect boundary, int capacity) {
            this.boundary = boundary;
            this.capacity = capacity;
            this.points = new ArrayList<>(capacity);
        }

        boolean insert(Point<T> p) {
            if (!boundary.contains(p.x, p.y)) return false;
            if (points.size() < capacity && !divided) {
                points.add(p);
                return true;
            }
            if (!divided) subdivide();
            return nw.insert(p) || ne.insert(p) || sw.insert(p) || se.insert(p);
        }

        boolean remove(T item, double x, double y) {
            if (!boundary.contains(x, y)) return false;
            for (int i = 0; i < points.size(); i++) {
                Point<T> p = points.get(i);
                if (p.x == x && p.y == y && Objects.equals(p.value, item)) {
                    points.remove(i);
                    return true;
                }
            }
            if (!divided) return false;
            return nw.remove(item, x, y) || ne.remove(item, x, y) || sw.remove(item, x, y) || se.remove(item, x, y);
        }

        void subdivide() {
            double x = boundary.x, y = boundary.y;
            double hw = boundary.halfWidth / 2.0, hh = boundary.halfHeight / 2.0;
            nw = new Node(new Rect(x - hw, y - hh, hw, hh), capacity);
            ne = new Node(new Rect(x + hw, y - hh, hw, hh), capacity);
            sw = new Node(new Rect(x - hw, y + hh, hw, hh), capacity);
            se = new Node(new Rect(x + hw, y + hh, hw, hh), capacity);
            List<Point<T>> old = new ArrayList<>(points);
            points.clear();
            for (Point<T> p : old) insert(p);
            divided = true;
        }

        void queryRange(Rect range, List<Point<T>> found) {
            if (!boundary.intersects(range)) return;
            for (Point<T> p : points) if (range.contains(p.x, p.y)) found.add(p);
            if (!divided) return;
            nw.queryRange(range, found);
            ne.queryRange(range, found);
            sw.queryRange(range, found);
            se.queryRange(range, found);
        }

        void clear() {
            points.clear();
            if (divided) {
                nw.clear(); ne.clear(); sw.clear(); se.clear();
                nw = ne = sw = se = null;
                divided = false;
            }
        }
    }
}


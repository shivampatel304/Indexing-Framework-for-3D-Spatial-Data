import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class KDTree {
    private final int K = 3;
    private Node root;

    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }


    public void insert(Vector3D point) {
        root = insert(root, point, 0);
    }

    private Node insert(Node node, Vector3D point, int depth) {
        if (node == null) {
            return new Node(point);
        }

        int axis = depth % K;
        if (axis == 0) {
            if (point.getX() < node.point.getX()) {
                node.left = insert(node.left, point, depth + 1);
            } else {
                node.right = insert(node.right, point, depth + 1);
            }
        } else if (axis == 1) {
            if (point.getY() < node.point.getY()) {
                node.left = insert(node.left, point, depth + 1);
            } else {
                node.right = insert(node.right, point, depth + 1);
            }
        } else {
            if (point.getZ() < node.point.getZ()) {
                node.left = insert(node.left, point, depth + 1);
            } else {
                node.right = insert(node.right, point, depth + 1);
            }
        }

        return node;
    }

    public List<Vector3D> rangeQuery(Vector3D min, Vector3D max) {
        List<Vector3D> result = new ArrayList<>();
        rangeQuery(root, min, max, result, 0);
        return result;
    }

    private void rangeQuery(Node node, Vector3D min, Vector3D max, List<Vector3D> result, int depth) {
        if (node == null) {
            return;
        }

        Vector3D point = node.point;
        if (point.getX() >= min.getX() && point.getX() <= max.getX()
                && point.getY() >= min.getY() && point.getY() <= max.getY()
                && point.getZ() >= min.getZ() && point.getZ() <= max.getZ()) {
            result.add(point);
        }

        int axis = depth % K;
        double splitValue = 0;
        double minValue = 0;
        double maxValue = 0;

        if (axis == 0) {
            splitValue = point.getX();
            minValue = min.getX();
            maxValue = max.getX();
        } else if (axis == 1) {
            splitValue = point.getY();
            minValue = min.getY();
            maxValue = max.getY();
        } else {
            splitValue = point.getZ();
            minValue = min.getZ();
            maxValue = max.getZ();
        }

        if (minValue < splitValue) {
            rangeQuery(node.left, min, max, result, depth + 1);
        }
        if (maxValue >= splitValue) {
            rangeQuery(node.right, min, max, result, depth + 1);
        }
    }

    public Vector3D nearestNeighbor(Vector3D target) {
        return nearestNeighbor(root, target, null, 0).point;
    }

    private Node nearestNeighbor(Node node, Vector3D target, Node best, int depth) {
        if (node == null) {
            return best;
        }

        double nodeDistance = node.point.distance(target);
        double bestDistance = best != null ? best.point.distance(target) : Double.MAX_VALUE;

        if (nodeDistance < bestDistance) {
            best = node;
            bestDistance = nodeDistance;
        }

        int axis = depth % K;
        double targetValue = 0;
        double splitValue = 0;

        if (axis == 0) {
            targetValue = target.getX();
            splitValue = node.point.getX();
        } else if (axis == 1) {
            targetValue = target.getY();
            splitValue = node.point.getY();
        } else {
            targetValue = target.getZ();
            splitValue = node.point.getZ();
        }

        Node nextNode = null;
        Node otherNode = null;

        if (targetValue < splitValue) {
            nextNode = node.left;
            otherNode = node.right;
        } else {
            nextNode = node.right;
            otherNode = node.left;
        }

        best = nearestNeighbor(nextNode, target, best, depth + 1);

        // Check if there could be a closer point on the other side of the splitting plane
        if (Math.abs(splitValue - targetValue) < bestDistance) {
            best = nearestNeighbor(otherNode, target, best, depth + 1);
        }

        return best;
    }

    public List<Vector3D> findAllCloserPoints(Vector3D target, double maxDistance) {
        List<Vector3D> result = new ArrayList<>();
        findAllCloserPoints(root, target, result, maxDistance, 0);
        return result;
    }

    private void findAllCloserPoints(Node node, Vector3D target, List<Vector3D> result, double maxDistance, int depth) {
        if (node == null) {
            return;
        }

        double distance = node.point.distance(target);

        if (distance < maxDistance) {
            result.add(node.point);
        }

        int axis = depth % K;
        double targetValue = 0;
        double splitValue = 0;

        if (axis == 0) {
            targetValue = target.getX();
            splitValue = node.point.getX();
        } else if (axis == 1) {
            targetValue = target.getY();
            splitValue = node.point.getY();
        } else {
            targetValue = target.getZ();
            splitValue = node.point.getZ();
        }

        Node nextNode = null;
        Node otherNode = null;

        if (targetValue < splitValue) {
            nextNode = node.left;
            otherNode = node.right;
        } else {
            nextNode = node.right;
            otherNode = node.left;
        }

        findAllCloserPoints(nextNode, target, result, maxDistance, depth + 1);

        // Check if there could be a closer point on the other side of the splitting plane
        if (Math.abs(splitValue - targetValue) < maxDistance) {
            findAllCloserPoints(otherNode, target, result, maxDistance, depth + 1);
        }
    }


    public List<Vector3D> findClosestKPoints(Vector3D target, int k) {
        PriorityQueue<HeapNode> heap = new PriorityQueue<>(new HeapNodeComparator());
        findClosestKPoints(root, target, heap, k, 0);
        List<Vector3D> result = new ArrayList<>();
        for (HeapNode node : heap) {
            result.add(node.point);
        }
        return result;
    }

    private void findClosestKPoints(Node node, Vector3D target, PriorityQueue<HeapNode> heap, int k, int depth) {
        if (node == null) {
            return;
        }

        double distance = node.point.distance(target);
        if (heap.size() == 0 || heap.peek().distance == distance) {
            heap.offer(new HeapNode(node.point, distance));
        } else if (heap.peek().distance<distance){
            heap.clear();
            heap.offer(new HeapNode(target, distance));
        }


        int axis = depth % K;
        double targetValue = 0;
        double splitValue = 0;

        if (axis == 0) {
            targetValue = target.getX();
            splitValue = node.point.getX();
        } else if (axis == 1) {
            targetValue = target.getY();
            splitValue = node.point.getY();
        } else {
            targetValue = target.getZ();
            splitValue = node.point.getZ();
        }

        Node nextNode = null;
        Node otherNode = null;

        if (targetValue < splitValue) {
            nextNode = node.left;
            otherNode = node.right;
        } else {
            nextNode = node.right;
            otherNode = node.left;
        }

        findClosestKPoints(nextNode, target, heap, k, depth + 1);

        // Check if there could be a closer point on the other side of the splitting plane
        if (Math.abs(splitValue - targetValue) < heap.peek().distance) {
            findClosestKPoints(otherNode, target, heap, k, depth + 1);
        }
    }

    public int countNodes() {
        return countNodes(root);
    }

    private int countNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    private static class Node {
        Vector3D point;
        Node left;
        Node right;

        public Node(Vector3D point) {
            this.point = point;
        }
    }

    private static class HeapNode {
        Vector3D point;
        double distance;

        public HeapNode(Vector3D point, double distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    private static class HeapNodeComparator implements Comparator<HeapNode> {
        public int compare(HeapNode a, HeapNode b) {
            if (a.distance < b.distance) {
                return -1;
            } else if (a.distance > b.distance) {
                return 1;
            } else {
                return 0;
            }
        }
    }


}

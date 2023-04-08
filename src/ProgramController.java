import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProgramController {
    public static void main(String[] args) {
        String filename = "coordinates_large.csv";
        KDTree tree = new KDTree();

        // Read point coordinates from CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                Vector3D point = new Vector3D(x, y, z);
                tree.insert(point);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            return;
        }

        // Query 1: find all points in a cube defined by the query parameters
        Vector3D min = new Vector3D(100, 200, 300);
        Vector3D max = new Vector3D(500, 600, 700);
        long startTime = System.currentTimeMillis();
        List<Vector3D> result1 = tree.rangeQuery(min, max);
        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime); // convert to seconds
        System.out.println("Query 1 result: " + result1.size() + " points, Time: " + elapsedTime + " ms");

        // Query 2: find the nearest neighbors of a given point
        Vector3D target = new Vector3D(250, 350, 450);
        startTime = System.currentTimeMillis();
        double maxDistance = 100; // Adjust this value as needed
        List<Vector3D> result2 = tree.findAllCloserPoints(target, maxDistance);
        endTime = System.currentTimeMillis();
        elapsedTime = (endTime - startTime); // convert to seconds
        System.out.println("Query 2 result: " + result2.size() + " points, Time: " + elapsedTime + " ms");

        // Report the total number of nodes in the KDTree
        int nodeCount = tree.countNodes();
        System.out.println("Total number of nodes in KDTree: " + nodeCount);

        // Report the height of the KDTree
        int treeHeight = tree.height();
        System.out.println("Height of KDTree: " + treeHeight);

        // Report the memory usage of the KDTree
        long nodeSize = 40; // Approximate memory usage in bytes for each node (Vector3D: 24 bytes, Node: 16 bytes)
        long memoryUsage = nodeCount * nodeSize;
        System.out.println("Memory usage of KDTree: " + memoryUsage + " bytes");


        // Save Query 1 results to file
        try (PrintWriter out = new PrintWriter("Q1-Output.txt")) {
            for (Vector3D point : result1) {
                out.println(point.getX() + "," + point.getY() + "," + point.getZ());
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}

package ex1.src;

import java.io.*;
import java.util.*;

/**
 * This class represents an Undirected (positive) Weighted Graph Theory algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 */

public class WGraph_Algo implements weighted_graph_algorithms {
    weighted_graph g;

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        this.g = g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return g;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public weighted_graph copy() {
        WGraph_DS gc = new WGraph_DS();
        try {
            for (node_info n : g.getV()) { //Add all nodes
                gc.addNode(n.getKey());
            }
            for (node_info n : g.getV()) { //Add all edges
                for (node_info ni : g.getV(n.getKey()))
                    gc.connect(n.getKey(), ni.getKey(), g.getEdge(n.getKey(), ni.getKey()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        gc.mc = g.getMC();
        return gc;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     * @return
     */
    @Override
    public boolean isConnected() {
        for (node_info n : g.getV()) //Initialize all nodes to be unvisited
            n.setTag(0);
        boolean first = true;
        for (node_info n : g.getV()) { //Run bfs only one time (on the first node)
            if (!first) break;
            first = false;
            bfs(n.getKey());
        }
        for (node_info n : g.getV()) { //Check if there is unvisited node
            if (n.getTag() == 0)
                return false;
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        if (!hasPath(src, dest)) //There is no path
            return -1;
        HashMap<Integer, Double> weight = new HashMap<>();
        for (node_info n : g.getV()) { //Initialize all nodes to be unvisited and empty path list
            n.setTag(0);
            n.setInfo(""); //Save the nodes path from src to current node
            weight.put(n.getKey(), Double.POSITIVE_INFINITY);
        }
        weight.put(src, 0.0);
        int min;
        double min_w = 0;
        while (!weight.isEmpty()) {
            min = findMinNode(weight);
            min_w = weight.get(min);
            if (min == dest)
                return min_w;
            weight.remove(min);
            for (node_info n : g.getV(min)) {
                if (n.getTag() != 1 && min_w + g.getEdge(min, n.getKey()) < weight.get(n.getKey())) {
                    weight.put(n.getKey(), min_w + g.getEdge(min, n.getKey()));
                    n.setInfo(min + "");
                }
            }
            g.getNode(min).setTag(1);
        }
        return -1;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        Stack<node_info> path = new Stack<>();
        double dist = shortestPathDist(src, dest);
        if (dist != -1) { //There is a path
            node_info v = g.getNode(dest);
            path.push(v);
            while (!v.getInfo().isEmpty()) {
                int prev_node = Integer.parseInt(v.getInfo());
                path.push(g.getNode(prev_node));
                v.setInfo("");
                v = g.getNode(prev_node);
            }
            List<node_info> reversed_path = new ArrayList<>();
            while (!path.isEmpty())
                reversed_path.add(path.pop());
            //System.out.println(reversed_path.toString());
            return reversed_path;
        }
        return null;
    }

    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(g);
            out.close();
            f.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(f);
            weighted_graph gc = (weighted_graph) in.readObject();
            in.close();
            f.close();
            init(gc);
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            return false;
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
            return false;
        }
        return true;
    }

    /**
     * Bfs utility function- does breadth traversal across the graph and marks the vertices it visited.
     * @param key the id of the starting node.
     */
    private void bfs(int key) {
        Queue<Integer> queue = new LinkedList<>();
        int element; //Save the current key node dequeue from the queue
        g.getNode(key).setTag(1); //The node is visited
        queue.add(key);
        while (!queue.isEmpty()) {
            element = queue.remove();
            for (node_info i : g.getV(element)) { //The neighbors of the current node
                if (i.getTag() == 0) { //Have not visited
                    queue.add(i.getKey());
                    i.setTag(1);
                }
            }
        }
    }

    /**
     * The function shows whether there is a path between two vertices.
     * @param src  - Source node
     * @param dest - Destination node
     * @return true- if there is a path , otherwise, false
     */
    private boolean hasPath(int src, int dest) {
        for (node_info n : g.getV())
            n.setTag(0);
        LinkedList<Integer> queue = new LinkedList<>();
        g.getNode(src).setTag(1);
        queue.add(src);
        //bfs Algorithm
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (node_info i : g.getV(u)) { //The neighbors of the current node
                if (i.getTag() == 0) {
                    queue.add(i.getKey());
                    //Save a list of nodes we went through till this node from the source
                    i.setInfo(i.getInfo() + g.getNode(u).getInfo() + u + " ");
                    i.setTag(1);
                    if (i.getKey() == dest)
                        return true;
                }
            }
        }
        return false;
    }

    private int findMinNode(HashMap<Integer, Double> w) {
        int minKey = -1;
        double minValue = Double.MAX_VALUE;
        for (Integer key : w.keySet()) {
            double value = w.get(key);
            if (value < minValue) {
                minValue = value;
                minKey = key;
            }
        }
        return minKey;
    }
}

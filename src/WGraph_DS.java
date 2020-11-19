package ex1.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents an undirectional weighted graph.
 * It should support a large number of nodes (over 10^6, with average degree of 10).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 */
public class WGraph_DS implements weighted_graph, java.io.Serializable {
    private HashMap<Integer, node_info> nodes;
    private HashMap<Integer, HashMap<Integer, Double>> edges;
    int countEdges;
    int mc;

    /**
     * default constructor
     */
    public WGraph_DS() {
        nodes = new HashMap<Integer, node_info>();
        edges = new HashMap<Integer, HashMap<Integer, Double>>();
        countEdges = 0;
        mc = 0;
    }

    /**
     * copy constructor
     */
    public WGraph_DS(WGraph_DS g) {
        this.mc = g.getMC();
        this.countEdges = g.edgeSize();
        this.nodes = g.getNodes();
        this.edges = g.getEdges();
    }

    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2))
            return false;
        if (node1 == node2)
            return false;
        if (!edges.containsKey(node1) || !edges.containsKey(node2)) //If the vertex has no edges
            return false;
        else return (edges.get(node1).containsKey(node2));
    }

    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) //If there is a edge between the vertices
            return edges.get(node1).get(node2); //Return the weight
        return -1;
    }

    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     * @param key
     */
    @Override
    public void addNode(int key) {
        try {
            if (nodes.containsKey(key)) //If the vertex already exists
                throw new RuntimeException("ERR: The node is already exist in the graph");
            if (key < 0) //If the key is negative throw exception
                throw new RuntimeException("ERR: The key is negative");
            nodes.put(key, new NodeInfo(key));
            mc++;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        try {
            if (w <= 0)
                throw new RuntimeException("ERR: The weight should be larger than 0");
            if (node1 == node2)
                throw new RuntimeException("ERR: The nodes should be different");
            if (nodes.containsKey(node1) && nodes.containsKey(node2)) {
                if (!hasEdge(node1, node2))
                     countEdges++;
                if (!edges.containsKey(node1)) //If node1 has no edges
                    edges.put(node1, new HashMap<Integer, Double>());
                edges.get(node1).put(node2, w);

                if (!edges.containsKey(node2))  //If node2 has no edges
                    edges.put(node2, new HashMap<Integer, Double>());
                edges.get(node2).put(node1, w);
            }
            mc++;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }

    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (edges.containsKey(node_id)) { //The node is exist
            ArrayList<node_info> neighbors = new ArrayList<node_info>();
            for (int i : edges.get(node_id).keySet()) //Runs on the list of keys in the HashMap
                neighbors.add(getNode(i));
            return neighbors;
        }
        return new ArrayList<node_info>();
    }

    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        if (nodes.containsKey(key)) { //The node is exist
            if (edges.containsKey(key)) { //If there is an edge with this node
                for (Integer node : nodes.keySet()) { //Runs on all the nodes
                    //If there is a edge between the key and the current vertex
                    if (node != key && edges.containsKey(node) && edges.get(node).containsKey(key)) {
                        edges.get(node).remove(key);
                        countEdges--;
                        mc++;
                    }
                    if (node == key && edges.containsKey(key)) {
                        edges.remove(key); //Delete the vertex from the list of edges
                    }
                }
            }
            mc++;
            return nodes.remove(key); //Remove from the nodes hashmap.
        }
        return null;
    }

    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (edges.containsKey(node1) && edges.get(node1).containsKey(node2)) { //The edge is exist
            edges.get(node1).remove(node2);
            edges.get(node2).remove(node1);
            mc++;
            countEdges--;
        }
    }

    /**
     * return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return countEdges;
    }

    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     * Nodes getter.
     * @return the hashmap that contains all the nodes.
     */
    public HashMap<Integer, node_info> getNodes() {
        return this.nodes;
    }

    /**
     * Edges getter.
     * @return the hashmap that contains all the edges.
     */
    public HashMap<Integer, HashMap<Integer, Double>> getEdges() {
        return this.edges;
    }

    /**
     * Check the equality of this graph and the given object
     * @param ob-
     * @return
     */
    @Override
    public boolean equals(Object ob) {
        if (this == ob)
            return true;
        if (ob instanceof weighted_graph) {
            weighted_graph w_gr = (weighted_graph) ob;
            if (nodeSize() != w_gr.nodeSize() || edgeSize() != w_gr.edgeSize())
                return false;
            for (node_info n : getV()) {
                if (w_gr.getNode(n.getKey()) == null)
                    return false;
                if (this.getV(n.getKey()).size() != w_gr.getV(n.getKey()).size()) //Check amount of edges
                    return false;
            }
            return true;
        }
        return false;
    }

    private static class NodeInfo implements node_info, java.io.Serializable {
        static int keyCounter;
        int key;
        private String info;
        private double tag;

        /**
         * constructor
         */
        public NodeInfo(int tag, String info) {
            this.key = keyCounter++;
            this.info = info;
            this.tag = tag;
        }

        /**
         * copy constructor
         */
        public NodeInfo(node_info n) {
            this.setKey(n.getKey());
            info = n.getInfo();
            tag = n.getTag();

        }

        /**
         * default constructor
         */
        public NodeInfo() {
            this.key = keyCounter++;
            this.info = "";
            this.tag = 0;
        }

        /**
         * constructor
         */
        public NodeInfo(int key) {
            this.key = key;
            this.info = info;
            this.tag = tag;
        }

        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         * @return
         */
        @Override
        public int getKey() {
            return key;
        }

        /**
         * return the remark (meta data) associated with this node.
         * @return
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            info = s;
        }

        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return
         */
        @Override
        public double getTag() {
            return tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            tag = t;
        }

        /**
         * Allow setting the node_data's key
         * @param key
         */
        private void setKey(int key) {
            this.key = key;
        }

        /**
         * toString()
         * @return the node's key
         */
        public String toString() {
            return key + "";
        }
    }
}





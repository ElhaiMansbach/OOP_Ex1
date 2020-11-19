package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

    WGraph_DS g;

    @BeforeEach
    void setGraph() {
        g = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g.addNode(i);
        }
        g.connect(0, 5, 2);
        g.connect(0, 6, 1);
        g.connect(1, 5, 5);
        g.connect(1, 7, 3);
        g.connect(2, 6, 4);
        g.connect(2, 9, 2);
        g.connect(2, 3, 1);
        g.connect(3, 8, 2);
        g.connect(3, 4, 5);
        g.connect(4, 7, 3);
    }

    @Test
    void getNode() {
        assertNull(g.getNode(15)); //Checks a key that not exist
        for (int i = 0; i <10; i++) //Checks a keys that exists
            assertNotNull(g.getNode(i));
    }

    @Test
    void hasEdge() {
        assertFalse(g.hasEdge(3, 9)); //No edge between 3 and 9
        g.connect(3, 9, 4); //Connect edge between 3 and 9
        assertTrue(g.hasEdge(3, 9)); //There is a edge between 3 and 9
    }

    @Test
    void getEdge() {
        assertEquals(g.getEdge(1, 7), 3);
        assertEquals(g.getEdge(1, 1), -1);
        assertEquals(g.getEdge(1, 0), -1);
        //Shows that there is the same weight between the same edge in reverse order
        assertEquals(g.getEdge(2, 6), g.getEdge(6, 2));
    }

    @Test
    void addNode() {
        g.addNode(0); //Try adding a node which already exists
        g.addNode(2); //Try adding a node which already exists
        g.addNode(4); //Try adding a node which already exists
        int size = g.nodeSize();
        g.addNode(10);
        g.addNode(11);
        g.addNode(12);
        assertNotNull(g.getNode(12));
        assertEquals(size + 3, g.nodeSize());
        g.addNode(-1); //Try to add negative key
    }

    @Test
    void connect() {
        int numOfEdges = g.edgeSize();
        g.connect(0, 1, -1); //Try to add negative weight
        g.connect(5, 5, 1); //Try to connect the same node
        g.connect(5, 15, 1); // The destination node is not exist
        g.connect(15, 5, 1); //The source node is not exist
        g.connect(20, 30, 1); //Both nodes are not exist
        g.connect(0, 5, 2); // The edge is exist
        assertEquals(g.getEdge(0, 5), 2);
        g.connect(0, 5, 6); //Update the weight
        assertEquals(g.getEdge(0, 5), 6);
        if (numOfEdges != g.edgeSize())
            fail("ERR: Shouldn't add these edges");
        g.connect(4, 9, 1);
        assertEquals(g.getEdge(4, 9), 1);
        assertEquals(g.getEdge(9, 4), 1); //Check both directions
        if (numOfEdges + 1 != g.edgeSize())
            fail("ERR: Should add 1 edge");
    }

    @Test
    void getV() {
        assertNotNull(g.getV());
        assertEquals(g.getV().size(), 10);
        assertTrue(g.getV().contains(g.getNode(0))); //The node is exist
        assertFalse(g.getV().contains(g.getNode(40))); //The node is not exist
        Collection<node_info> ex = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ex.add(g.getNode(i));
        }
        Collection<node_info> ac = g.getV();
        assertEquals(ex.toString(), ac.toString());
    }

    @Test
    void testGetV() {
        Collection<node_info> ni2 = new ArrayList<>();
        ni2.add(g.getNode(3));
        ni2.add(g.getNode(6));
        ni2.add(g.getNode(9));
        Collection<node_info> ac2 = g.getV(2);
        if (!ni2.toString().equals(ac2.toString()))
            fail("The neighbors lists should be the same");
        assertEquals(g.getV(9).size(), 1);
        g.removeEdge(9, 2);
        assertEquals(g.getV(9).size(), 0); //No neighbors
        g.connect(9, 8, 1);
        assertEquals(g.getV(9).size(), 1); //Add neighbor
    }

    @Test
    void removeNode() {
        assertNull( g.removeNode(15)); //Try to remove a node that doesn't exist
        node_info n5=g.getNode(5);
        assertEquals( g.removeNode(5),n5);
        assertEquals(g.getV().size(), 9); //Checks the size of the nodes after remove one Node
        assertEquals(g.getEdge(5, 0), -1);
        assertEquals(g.getEdge(1, 5), -1);
        assertEquals(g.edgeSize(), 8); //Checks the size of the edges after remove a node
        g.addNode(20);
        g.connect(20, 2, 4);
        assertEquals(g.getV().size(), 10); //Checks the size of the nodes after add one node
        assertEquals(g.edgeSize(), 9); //Checks the size of the edges after add one edge
        g.removeNode(20);
        assertEquals(g.getV().size(), 9);
        assertEquals(g.edgeSize(), 8);
    }

    @Test
    void removeEdge() {
        int edgeCount = g.edgeSize();
        g.removeEdge(3, 4); //Remove an edge between 3 and 4
        assertEquals(g.edgeSize(), edgeCount-1); //Checks the size of the edges after remove one edge
        assertEquals(g.getEdge(3, 4), -1);
        assertEquals(g.getEdge(4, 3), -1);
    }

    @Test
    void nodeSize() {
        assertEquals(g.nodeSize(), 10);
        g.addNode(11);
        assertEquals(g.nodeSize(), 11);
        g.removeNode(11);
        assertEquals(g.nodeSize(), 10);
    }

    @Test
    void edgeSize() {
        assertEquals(g.edgeSize(), 10);
        int edgeCount = g.edgeSize();
        g.addNode(11);
        g.connect(11, 1, 1);
        g.connect(11, 2, 1);
        g.connect(11, 3, 1);
        assertEquals(g.edgeSize(), edgeCount+3);
        g.removeEdge(11, 2);
        assertEquals(g.edgeSize(), edgeCount+2);
        edgeCount = g.edgeSize();
        int ni1=g.getV(1).size(); //Amount of neighbors of node 1
        g.removeNode(1);
        assertEquals(g.edgeSize(), edgeCount-ni1);

    }

    @Test
    void getMC() {
        int mc=g.getMC();
        assertEquals(mc, 20); //Added 10 nodes and 10 edges
        g.addNode(55);
        g.connect(55, 5, 1);
        assertEquals(g.getMC(), mc+2);
        g.removeNode(55);
        assertEquals(g.getMC(), mc+4); //Removes 1 node and 1 edge
    }

    @Test
    void getNodes() {
        assertEquals(g.getNodes().size(), 10);
        g.addNode(22);
        assertEquals(g.getNodes().size(), 11);
        g.removeNode(22);
        assertEquals(g.getNodes().size(), 10);
        HashMap<Integer,node_info> nodes = g.getNodes();
        for(int i=0; i<10; i++){ //There are 10 nodes in the graph - with keys 0 to 9
            assertTrue(nodes.containsKey(i));
            assertEquals(nodes.get(i), g.getNode(i));
        }
    }

    @Test
    void getEdges() {
        HashMap<Integer, HashMap<Integer, Double>> edges=g.getEdges();
        assertEquals(edges.size(), 10); //Amount of nodes with edges.
        if(edges.get(0).size()!=2)
            fail ("Node 0 has 2 edges");
        g.removeEdge(0, 5);
        edges=g.getEdges();
        if(edges.get(0).size()!=1)
            fail ("Node 0 has 1 edge");
    }

    @Test
    void testEquals() {
        WGraph_DS g2 = new WGraph_DS(g);
        assertEquals(g2, g);
        g.removeNode(1);
        assertNotEquals(g2,g);
        g.addNode(1);
        g2.connect(1,5,1); //Update an edge weight
        assertNotEquals(g2,g);
    }

    @Test
    void set1000000() {
        g = new WGraph_DS();
        for (int i = 1; i <= 1000000; i++) { //Builds a graph with million nodes
            g.addNode(i);
        }
        for (int i = 1; i < 999990; i++) {
            g.connect(i, i + 1, 1);
            g.connect(i, i + 2, 2);
            g.connect(i, i + 3, 2);
            g.connect(i, i + 4, 2);
            g.connect(i, i + 5, 2);
            g.connect(i, i + 6, 2);
            g.connect(i, i + 7, 2);
            g.connect(i, i + 8, 2);
            g.connect(i, i + 9, 2);
            g.connect(i, i + 10, 2);
        }
        for (int i = 1; i <= 10; i++)
            g.connect(1, i + 20, 1);
    }
}
package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
    weighted_graph_algorithms ga = new WGraph_Algo();
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
        ga.init(g);
    }


    @Test
    void init() {
        ga.init(g);
        assertNotNull(ga.getGraph());

    }

    @Test
    void getGraph() {
        assertEquals(ga.getGraph(), g);
        g.removeNode(0);
        assertEquals(ga.getGraph().getV(), g.getV()); //The graph in ga should  be changed
    }

    @Test
    void copy() {
        weighted_graph g1 = ga.copy();
        assertEquals(g,g1);
        assertEquals(g1.getMC(), g.getMC());
        assertEquals(g1.edgeSize(), g.edgeSize()); //Check nodes size
        assertEquals(g1.nodeSize(), g.nodeSize()); //Check edges size
        for(int i=0; i<10; i++)
            assertEquals(g1.getV(i).size(),g.getV(i).size());
    }

    @Test
    void isConnected() {
        assertTrue(ga.isConnected());
        g.removeEdge(2,9);
        assertFalse(ga.isConnected());
        g.removeNode(9);
        assertTrue(ga.isConnected());
        g.addNode(11);
        assertFalse(ga.isConnected()); //There is an isolated node
        g.connect(11,1,2);
        assertTrue(ga.isConnected());
    }

    @Test
    void shortestPathDist() {
        g.connect(4 ,9,9);
        assertEquals(ga.shortestPathDist(3,9),3);
        assertEquals(ga.shortestPathDist(9,3),3);
        g.addNode(15);
        assertEquals(ga.shortestPathDist(9,15), -1); //There is no path
        assertEquals(ga.shortestPathDist(7,9),11);
    }

    @Test
    void shortestPath() {
        g.addNode(20);
        assertNull(ga.shortestPath(20,9)); //There is no path
        g.connect(4 ,9,9);
        List<node_info> path=ga.shortestPath(3,9);
        if(!path.toString().equals("[3, 2, 9]"))
            fail("There is shorter path");
        g.connect(0,1,3);
        path=ga.shortestPath(1,5);
        if(!path.toString().equals("[1, 5]") && !path.toString().equals("[1, 0, 5]")) //Two paths
            fail("There is shorter path");
    }

    @Test
    void save_load() {
        ga.save("test_save");
        weighted_graph_algorithms ga2 = new WGraph_Algo();
        ga2.load("test_save");
        assertEquals(ga.getGraph(),ga2.getGraph());
    }

}
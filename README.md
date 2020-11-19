# OOP_Ex1
## Ex_1 - Undirected & Weighted Graphs
*Author: Elhai Mansbach* <br/>
In this project I implemented algorithms for developing a data structure into an undirectional weighted graph. <br/>
After the data structure implementation, I implemented a bunch of algorithms on the graph including the ability to duplicate the graph, check connectivity, check edge's weight and calculate the shortest path between source and destination nodes.

**In the project I have 2 main classes (and 1 inner class):**

## Data Structures:

### WGraph_DS:<br/>
*The weighted_graph interface is implemented in WGraph_DS class:*
This class represents an undirectional weighted graph.
It supports a large number of nodes (over 10^6, with average degree of 10).
The nodes and the edges are implemented in a data structure – HashMap.
There are functions for adding / removing nodes and edges, getting the node info by his key, obtaining lists of nodes and the node's neighbors list, checking if two nodes are connected, checking if there is an edge between two nodes, checking the weight of edge between to nodes, obtaining the amount of nodes/edges there are in the graph, obtaining the amount of actions done on the graph (saved as MC) and checking whether two graphs are equals.
This class also contains as an inner class the NodeInfo class:
### NodeInfo:<br/>
*The node_info interface is implemented in NodeInfo class:*
This class represents the set of operations applicable on a node in an undirectional weighted graph.
Each node has a unique key and there are 2 more node fields (info and tag) that were used only during the algorithms.


## Algorithms:

### WGraph_Algo:<br/>
*The weighted_graph_algorithms interface is implemented in WGraph_Algo class:*
The Graph_Algo object contains a graph to activate the algorithms on.

**This class represents the Graph Theory algorithms including:**
1.	**Init** - Init this set of algorithms on a given graph
2.	**Copy** - Deep copy of the graph.
3.	**isConnected** - Checks whether there is a valid path from EVREY node to each other node.
4.	**shortestPathDist** - Calculates the weight of the shortest path between 2 given nodes using Dijsktra algorithm.
5.	**shortestPath** - Finds the shortest path (as an ordered list of nodes) between 2 given nodes in the graph.
6.	**Save** - Saves the graph into a file.
7.	**Load** - Loads the graph from the file.

**I have also implemented some auxiliary functions used to implement the above function:**

 **Bfs:**<br/>
I used this function in isConnected function, to check if we visited all the nodes, otherwise the graph is not connected.
I used the bfs utility function that does breadth traversal across the graph and marks the nodes it visited.
I created a queue (data structure) and another filed "element" (which save the current node dequeue from the queue). The tag of a node is 0 if we have not visited the node yet, otherwise 1. I start traversal from the "key" node (which the function gets), set the tag of the node to 1, and inserted him into the queue. Then I made a while loop that works as long as the queue is not empty, in the loop I ran over the neighbors of the current "element"  and checked if the tag of each neighbor is equal to 0 (i.e. we had not yet visited it), otherwise we move to the next neighbor. If equal to 0 then the node was inserted to the queue and the tag was updated.

**hasPath:**<br/>
I used the hasPath function as an auxiliary function for shortestPathDist.
The function checks whether there is a path between two nodes and returns true or false.
The hasPath function is very similar to the bfs function, while this function gets two variables (keys) of the start and destination nodes, keeps in the info variable every node we went through until we reached the destination node (if we arrived).
If we have reached the destination node, the function returns true, otherwise returns false.

**findMinNode:**<br/>
I used findMinNode function as an auxiliary function for shortestPathDist.
The function receives a list of nodes (HashMap) and finds from them the node with the lowest weight.

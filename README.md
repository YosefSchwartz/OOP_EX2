# Weighted graphs  
***Authors:** Eden Shkuri & Yosef Schwartz* 

In this project we implemented a Pokemon game whitch contains agents and Pokemons, and the purpose of the game is that the agens
will collect the highest score by eating Pokemon when each has a different value.
The game was implemented by a directed weighted graph with graph's algorithms.

##### The game based of 2 parts -
api - contains all the graph classes, e.g. edge data, node data, directed weighted graph, graph algorithms and so.  
gameClient - contains all the game planning and running, Pokemon and agent classes, GUI and more.

# api-

### Data Structures:  
#### NodeData:  
The NodeData class implements the node_data interface with an innew class- geoLocation implements the geo_location interface.  
This class represents a vertex in a directed weighted graph.  
Each node has a unique id number (key), info and tag (that were used only during the algorithms).  
The geoLocation represents a 3D point for the location of each node.  

#### edgeData:  
The edgeData class implements the edge_data interface with an inner class- edgeLocation implements the edge_location interface.    
This class represents a edge between 2 nodes in directed weighted graph.  
Each edge contains src, dest, weight and edge location.
The edgeLocatin represents a edge with a point on it with a ratio of the location of this point.  

#### DWGraph_DS:
The DWGraph_DS implements the weighted_graph interface:  
This class represents a undirectional weighted graph.  
The nodes and edges are implemented in a "HashMap" data structure.  
There are functions for adding / removing nodes and edges, getting a collection of the nodes in the graph / collection of neighbors for each  
node. additionally, you can get the amount of nodes/edges that are in the graph and also get the amount of actions done on the graph (MC).  

### Algorithms:
The DWGraph_Algo class implements the weighted_graph_algorithms:  
The DWGraph_Algo class contains a graph to operate the algorithms on.  
This class represents the Graph algorithms including:  
1.	**init-** Initializes the graph a given graph.  
2.	**getGraph-** Return the underlying graph the class works on.  
3.	**copy-** Deep copy of the graph.  
4.	**isConnected-** Checks whether the graph is connected.  
5.	**shortestPathDist-** Calculates the shortest path distance between 2 given nodes.  
6.	**shortestPath-** Finds the shortest path (by list of nodes) between 2 given nodes in the graph.  
7.	**save-** Saves the graph into a JSON file.  
8.	**load-** load a graph from a JSON file to the graph that class works on.  
**isConnected, shortestPath and shortestPathDist using Dijkstra's algorithm method.**

# gameClient- 

#### Agent:  
This class repesents a agent in the game, contains a src and dest node of the agent, his value, id, speed, location and a list of pokemons he is going to eat.  
All the details are updating with any changes.

#### Pokemon:  
This class repesents a Pokemon in the game, contains a src and dest node of the pokemon, his value, location, edge location and a agent that will eat him.  
All the details are updating with any changes.

#### GameData:  
This class keeps all the list of the Pokemons and agents, the graph of this level and the info of the game - time to end, the points that have been collected until now and the level number.

#### Ex2:  
This class is responsible to build the game and run it with using the GUI.  
The game is automatic - Our algorithm decide to each agent where to go and when.

### GUI
based on LoginFrame - the login part with id and game number and the GameFrame - the class that paint the game running based on the GameData details.  

### Tests:  
We have created 2 JUNIT test (using JUNIT 5 version) :  
DWGraph_DSTest and DWGraph_AlgoTest.  

#### An example for a graph this project work on:  


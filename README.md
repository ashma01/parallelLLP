# Parallel LLP Algorithms

This repository contains a Java program that implements various parallel algorithms using the Lattice Linear Predicate (LLP) framework. The program provides the following algorithms with user-friendly input options:

1. **Boruvka's MST Algorithm** - Minimum Spanning Tree computation.
2. **Topological Sort** - Performing a topological sort on a directed graph.
3. **Transitive Closure Algorithm** - Computing the transitive closure of a directed graph.
4. **List Ranking** - Parallel list ranking for hierarchical structures.

**Getting Started**

**Prerequisites**

- Java Development Kit (JDK)
- Git (for cloning the repository)

**Installation**

1. Clone the repository to your local machine:
   git clone https://github.com/ashma01/parallelLLP.git

2. Compile the Java program:

   javac Driver.java

4. Run the program:

   java Driver
   
   Here are some examples of how to use the program:

To run the ListRanking algorithm, select option 1 and it will run the specified input file in the Driver class, from the repository.

To perform a topological sort, choose option 2 and it will run the specified input file in the Driver class, from the repository.

To find the Minimum Spanning Tree using Boruvka's Algorithm, select option 3 and it will run the specified input file in the Driver class, from the repository.

To compute the transitive closure of a directed graph, choose option 4 and it will run the specified input file in the Driver class, from the repository.


You may choose a different file for the algorithm, by changing the file name in the Driver class. We have two input files for each algorithm type.

**Input Formats**

**Boruvka's MST Algorithm**

- **Input File:** `BoruvkaMst.txt`
- **Format:** The input consists of the following elements:
    - Number of Nodes: The first line represents the number of vertices or nodes in the graph.
    - Edges with Weights: Each subsequent line represents an edge in the graph, formatted as follows:
        ```
        NodeA NodeB Weight
        ```
    Where `NodeA` and `NodeB` are the two nodes connected by the edge, and `Weight` is the weight of the edge.

**Topological Sort**

- **Input File:** `TopoSort.txt`
- **Format:** The input is structured as follows:
    - The first line specifies the root node.
    - Each subsequent line represents a node and its predecessors, with the format:
        ```
        Node:Predecessor1,Predecessor2,...
        ```
    If a node has multiple predecessors, they are comma-separated.

**Transitive Closure Algorithm**

- **Input File:** `TransitiveInput.txt`
- **Format:** The input represents a matrix in which each line corresponds to a row in the matrix. Values are separated by commas, and they are either `0` or `1`, indicating the existence of a path between nodes in the graph. The number of rows and columns in the matrix reflects the number of nodes in the graph.

**List Ranking**

- **Input File:** `ListRanking.txt`
- **Format:** The input is organized as follows:
    - The first line contains the root node. In list ranking, the root node serves as the starting point of the hierarchical structure. The root node typically has no parent.
    - The subsequent lines represent individual nodes in the list. Each line includes a single number, which is the unique identifier (or value) of the node. The order of these lines determines the order of the nodes in the list, with each node being connected to the node that appears above it in the list.
    - The unique identifier of each node represents its position in the list, and the unique identifiers of the nodes above it represent its parent or predecessors in the list.
    - The total number of lines in the input file corresponds to the number of nodes in the list.

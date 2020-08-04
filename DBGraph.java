import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Represents a De Bruijn Graph.
 * 
 * Limitations: - Alphabet input is limited to 0-9 - Alphabet must start at 0, increment from there
 * 
 * - The only bottleneck on output appears to be stack size (or stack consumption)
 * 
 * @author Reed Nelson
 */
public class DBGraph {

  private int[][] adjMatrix; // [x][] refers to node as decimal, [][x] refers to where index points
  private int k; // Alphabet size
  private int n; // window size
  private int numNodes; // The number of nodes in the tree
  private BufferedWriter bufferedWriter;

  private int seqLimit; // Max number of sequences outputed
  private int o; // Number of sequences outputed

  /**
   * Sets up the DBGraph object
   * 
   * @param A alphabet
   * @param n window length
   */
  DBGraph(String A, int n) {

    this.n = n;
    k = A.length();
    numNodes = (int) Math.pow(k, n - 1);

    adjMatrix = new int[numNodes][k];

    generate();

  }

  /**
   * Generates an adjacency matrix representation of the De Bruijn graph for the given n and k
   * 
   * A bit wacky, I'll admit, but efficient: [explanation here]
   * 
   * O(nodes * k) aka O(edges-ish)
   */
  private void generate() {

    String num;

    for (int i = 0; i < numNodes; i++) {
      for (int j = 0; j < k; j++) {

        num = toStringBase(i, k); // get value of this index
        num = fillZeros(num); // add appropriate number of 0s to the left
        num = num.substring(1); // cut out the first character
        num += Integer.toString(j); // append appropriate char to the end

        adjMatrix[i][j] = Integer.parseInt(num, k); // store in this location as decimal int

      }
    }

  }

  /**
   * Converts inputed decimal to a string of the inputed base
   * 
   * @param dec  decimal number to be converted
   * @param base the base to convert to from decimal
   * @return String of base base
   */
  private String toStringBase(int dec, int base) {

    String str = "";

    while (dec > 0) {

      str = Integer.toString(dec % base) + str;
      dec /= base;

    }

    return str;

  }

  /**
   * Prints the adjacency matrix representing this graph
   */
  public void printMatrix() {

    System.out.println("Vertex: 0 edge, 1...");

    for (int i = 0; i < numNodes; i++) {

      System.out.print(fillZeros(toStringBase(i, k)) + ": ");

      for (int j = 0; j < k; j++) {

        System.out.print(fillZeros(toStringBase(adjMatrix[i][j], k)) + " ");

      }

      System.out.println();

    }

  }

  /**
   * Fills in the most significant places with 0s.
   * 
   * @param str the string to be padded with 0s
   * @return the padded string
   */
  private String fillZeros(String str) {

    int z = (n - 1) - str.length();

    for (int i = 0; i < z; i++)
      str = "0" + str;

    return str;

  }

  /**
   * Prints all paths
   */
  public void printEulerian() {

    boolean[][] isVisited = new boolean[numNodes][numNodes];

    for (int i = 0; i < numNodes; i++)
      for (int j = 0; j < numNodes; j++)
        isVisited[i][j] = false;

    String path = "0";


    printEulerianUtil(0, 0, isVisited, path);

  }

  /**
   * A recursive function to print all paths from the current vertex (u) to the starting vertex.
   * 
   * @param u         current (unvisited) vertex
   * @param v         previous (visited) vertex
   * @param isVisited keeps track of vertices in current path
   * @param path      String of edges traversed on current path
   */
  private void printEulerianUtil(int u, int v, boolean[][] isVisited, String path) {

    isVisited[u][v] = true;

    // Base-ish case
    if (u == 0 && path.length() == Math.pow(k, n)) {

      System.out.println(path);

      isVisited[u][v] = false;

      return;

    }

    // Recurse for all the vertices adjacent to current vertex
    for (int i = 0; i < k; i++) {

      if (!isVisited[adjMatrix[u][i]][u]) {

        path += i; // store current edge in path
        printEulerianUtil(adjMatrix[u][i], u, isVisited, path);

        path = path.substring(0, path.length() - 1); // remove current edge from the path

      }

    }

    isVisited[u][v] = false;

  }

  /**
   * Write all paths to text file.
   * 
   * Warning: program terminates if the inputed maximum file size is reached.
   * 
   * @param mb the desired maximum size of the output file, in MB (-1 for unlimited)
   */
  public void writeEulerian(double mb) {

    seqLimit = (int) ((1024 * 1024 * mb) / Math.pow(k, n));
    o = 0;

    String fileName = k + "-" + n + ".txt";
    int bufferSize = 1024 * 1024; // (1MB) (Best to multiples of 1024B)

    String path = "0";

    boolean[][] isVisited = new boolean[numNodes][numNodes];

    for (int i = 0; i < numNodes; i++)
      for (int j = 0; j < numNodes; j++)
        isVisited[i][j] = false;

    try {

      bufferedWriter = new BufferedWriter(new FileWriter(fileName), bufferSize);

      writeEulerianUtil(0, 0, isVisited, path); // Calls recursive traversal

      bufferedWriter.close(); // Flushes/closes the stream

    } catch (IOException e) {
      e.printStackTrace();
    }



  }

  /**
   * A recursive function to print all paths from the current vertex (u) to the starting vertex.
   * 
   * @param u         current (unvisited) vertex
   * @param v         previous (visited) vertex
   * @param isVisited keeps track of vertices in current path
   * @param path      String of edges traversed on current path
   */
  private void writeEulerianUtil(int u, int v, boolean[][] isVisited, String path) {

    isVisited[u][v] = true;

    // Base-ish case
    if (u == 0 && path.length() == Math.pow(k, n)) {

      if (o == seqLimit) {
        System.exit(0);
      }

      o++;

      path += "\n";

      try {
        bufferedWriter.write(path); // send to buffer stream
      } catch (IOException e) {
        e.printStackTrace();
      }

      isVisited[u][v] = false;

      return;

    }

    // Recurse for all the vertices adjacent to current vertex
    for (int i = 0; i < k; i++) {

      if (!isVisited[adjMatrix[u][i]][u]) {

        path += i; // store current edge in path
        writeEulerianUtil(adjMatrix[u][i], u, isVisited, path);

        path = path.substring(0, path.length() - 1); // remove current edge from the path

      }

    }

    isVisited[u][v] = false;

  }


}



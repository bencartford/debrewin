import java.util.ArrayList;
import java.util.List;

/**
 * Represents a De Bruijn Graph 
 * 
 * @author Reed Nelson
 */
public class DBGraph {

  private int[][] adjMatrix; // [x][] refers to node as decimal, [][x] refers to where index points
  private int k; // Alphabet size
  private int n; // window size
  private int numNodes; // The number of nodes in the tree

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

    String bin; // change name, not necessarily binary anymore (it's 2020)

    for (int i = 0; i < numNodes; i++) {
      for (int j = 0; j < k; j++) {

        bin = toStringBase(i, k); // get value of this index
        bin = fillZeros(bin); // add appropriate number of 0s to the left
        bin = bin.substring(1); // cut out the first character
        bin += Integer.toString(j); // append appropriate char to the end

        adjMatrix[i][j] = Integer.parseInt(bin, k); // store in this location as decimal int

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
  public void printAllPaths() {

    int[] isVisited = new int[numNodes];

    for (int i = 0; i < numNodes; i++) // this is probably unnecessary
      isVisited[i] = 0;

    ArrayList<Integer> pathList = new ArrayList<>();

    pathList.add(0);

    printAllPathsUtil(0, isVisited, pathList);

  }

  /**
   * A recursive function to print all paths from the current vertex (u) to the starting vertex.
   * 
   * The fat fucking problem: The edges don't have unique identifiers, so I track the nodes instead.
   * And hitting each vertex twice (for k = 2) does NOT have the same implications for traversal as 
   * hitting each edge once (quite obvious when I type it out loud). So the cleanest solution will
   * probably be to start tracking which edges have been traversed, but I've yet to come up with a 
   * simple way to uniquely represent each edge in a way that fits with the adjacency matrix 
   * implementation this is using.
   * 
   * - Probably wanna switch from List<> to String or something, make for more a manageable print
   * 
   * @param u             current (unvisited) vertex
   * @param isVisited     keeps track of vertices in current path
   * @param localPathList list of edges traversed on current path
   */
  private void printAllPathsUtil(int u, int[] isVisited, List<Integer> localPathList) {

    isVisited[u]++;

    // Base-ish case
    if (u == 0 && localPathList.size() == Math.pow(k, n)) {

      System.out.println(localPathList);

      isVisited[u]--;

      return;

    }

    // Recurse for all the vertices adjacent to current vertex
    for (int i = 0; i < k; i++) {

      if (isVisited[adjMatrix[u][i]] != k) {

        localPathList.add(i); // store current node in path[]
        printAllPathsUtil(adjMatrix[u][i], isVisited, localPathList);

        localPathList.remove(localPathList.size() - 1); // remove current node in the path

      }

    }

    isVisited[u]--;
  }


}



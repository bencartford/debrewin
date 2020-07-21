// De Bruijn sequence generator and other things

/**
 * Program Notes:
 * - consider passing k as argument to dfs() and numP...() so it's not recalculated on every loop
 * - be pretty cool if it took a generic, but that's not really what I'm going for in this program
 * - ^ shuffles in particular
 * 
 * TODO: 
 * - Write inShuffle()
 * - Write isDeBruijn()
 * - Think about a BST based approach to generating not just 1, but all De Bruijn sequences 
 */


import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * yes
 * 
 * @author Reed Nelson, 2 time champion for most creative class names
 * @author ...
 */
public class Main {

  /**
   * Driver for the DeBruijn program
   * 
   * @param args
   */
  public static void main(String[] args) {

    final String A = "01"; // Alphabet
    int n = 5 ; // Window length

    System.out.println("For an alphabet \"" + A + "\" and window of " + n + ":");
    System.out.println(
        "There are " + numPossibleDBSequences(n, A.length()) + " valid De Bruijn sequences");
    System.out.println("One such sequence: " + deBruijn(n, A));

  }

  /**
   * Calculates the number of possible De Bruijn sequences considering the given window size and
   * alphabet length. Not very meaningful for even larger single digit values of n and k, due to 
   * the limited maximum integer length in Java, and the growth rate for this function.
   * 
   * @param n the window length
   * @param k the alphabet length
   * @return the number of possible sequences or roughly the largest possible value.
   */
  public static String numPossibleDBSequences(int n, int k) {

    long l = (long) (Math.pow(factorial(k), Math.pow(k, n - 1)) / Math.pow(k, n));

    if (l == Long.MAX_VALUE) {
      return "over 9 pentillion";
    }

    return Long.toString(l);

  }

  static Set<String> seen = new HashSet<String>();
  static Vector<Integer> edges = new Vector<Integer>();

  /**
   * Generates a single De Bruijn sequence, given a window size and an alphabet
   * 
   * @param n window size
   * @param A alphabet, as String
   * @return a valid De Bruijn sequence, as a String 
   * 
   * Adapted from https://www.geeksforgeeks.org/de-bruijn-sequence-set-1/
   */
  public static String deBruijn(int n, String A) {

    int k = A.length(); // the length of the alphabet

    // [use .clear() method on seen and edges if will be used multiple times in this program]

    String startingNode = string(n - 1, A.charAt(0));
    dfs(startingNode, A);

    String S = "";

    int l = (int) Math.pow(k, n); // length of sequence/number of edges

    for (int i = 0; i < l; ++i)
      S += A.charAt(edges.get(i));

    return S;

  }

  /**
   * Recursive helper method to deBruijn(), 
   * Literally just a depth-first search where no edge is traversed twice (Euler Circuit style)
   * Constructs graph of k^(n-1) nodes, k edges leaving each node
   * 
   * O(nodes + edges) I think --could be better 
   * 
   * @param node
   * @param A
   * 
   * Adapted from https://www.geeksforgeeks.org/de-bruijn-sequence-set-1/
   */
  private static void dfs(String node, String A) {

    int k = A.length();

    // iteratively add the alphabet to the string 
    // check each time if the string is now novel, if so, add it to the set of seen strings
    // then make recursive call, passing in string just added to seen, but missing its first char

    for (int i = 0; i < k; ++i) {

      String str = node + A.charAt(i);

      if (!seen.contains(str)) {

        seen.add(str);
        dfs(str.substring(1), A);
        edges.add(i);

      }

    }

  }

  /**
   * Helper method to deBruijn. Simply returns a string of the inputed character n times. 
   * 
   * @param n the number of times to copy the given char
   * @param charAt the character to be copied
   * @return a string with only the char charAt, copied n times.
   * 
   * Adapted from https://www.geeksforgeeks.org/de-bruijn-sequence-set-1/
   */
  private static String string(int n, char charAt) {

    String str = "";
    for (int i = 0; i < n; i++)
      str += charAt;

    return str;

  }

  /**
   * Determines whether the given sequence is a valid De Bruijn, for the given window
   * Not yet existent, obviously. 
   * 
   * See Olivia's (Albertsofc) Python implementation. (Perhaps I'll get around to stealing it, 
   * adapting it to Java)
   * 
   * @param sequence the sequence to check
   * @param n the window size
   * @return true if the inputed sequence is a De Bruijn, false otherwise
   */
  public static boolean isDeBruijn(String sequence, int n) {

    return false;

  }

  /**
   * A humble helper for numPossibleDBSequences(), as j.l.Math doesn't have a function for this
   * 
   * Also the hello world of recursive functions, in case you wanna learn about that, Jamin
   * 
   * @param k the integer to be operated on
   * @return k!, eventually
   */
  private static int factorial(int k) {

    if (k == 0)
      return 1;

    return k * factorial(k - 1);

  }

  /**
   * Performs a perfect out shuffle, following the formula (length k, index i) :
   * if (i < k/2): i -> 2i
   * if (i >= k/2): i -> |k - 2i| + 1
   * 
   * Whether the exception is necessary at all, and what the inShuffle() would look like, are 
   * questions that will be answered soon.
   * 
   * @param str the set to be shuffled
   * @return the shuffled set, as a String
   * @throws Exception in case an invalid String is passed
   */
  private static String outShuffle(String str) throws Exception {
    
    // Honest to god not sure if this is true yet
    if (str.length() % 2 != 0)
      throw new Exception("Set must be even for an out shuffle");
    
    int n = str.length() / 2;
    char[] newStr = new char[str.length()];
    String retStr = "";
    
    // Handle first half of domain
    for (int i = 0; i < n; i++) 
      newStr[2*i] = str.charAt(i);
   
    // Handle second half of domain
    for (int i = n; i <= 2 * n; i++) 
      newStr[(2 * n) - (2 * i) + 1] = str.charAt(i);
    
    // Convert from char Array to String 
    for (int i = 0; i < newStr.length; i++)
      retStr += newStr[i];
    
    return retStr;
    
  }
  
}

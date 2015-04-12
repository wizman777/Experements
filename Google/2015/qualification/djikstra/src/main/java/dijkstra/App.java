package dijkstra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

/**
 * Problem

The Dutch computer scientist Edsger Dijkstra made many important contributions to the field, including the shortest path finding algorithm that bears his name. This problem is not about that algorithm.

You were marked down one point on an algorithms exam for misspelling "Dijkstra" -- between D and stra, you wrote some number of characters, each of which was either i, j, or k. You are prepared to argue to get your point back using quaternions, an actual number system (extended from complex numbers) with the following multiplicative structure:



To multiply one quaternion by another, look at the row for the first quaternion and the column for the second quaternion. For example, to multiply i by j, look in the row for i and the column for j to find that the answer is k. To multiply j by i, look in the row for j and the column for i to find that the answer is -k.

As you can see from the above examples, the quaternions are not commutative -- that is, there are some a and b for which a * b != b * a. However they are associative -- for any a, b, and c, it's true that a * (b * c) = (a * b) * c.

Negative signs before quaternions work as they normally do -- for any quaternions a and b, it's true that -a * -b = a * b, and -a * b = a * -b = -(a * b).

You want to argue that your misspelling was equivalent to the correct spelling ijk by showing that you can split your string of is, js, and ks in two places, forming three substrings, such that the leftmost substring reduces (under quaternion multiplication) to i, the middle substring reduces to j, and the right substring reduces to k. (For example, jij would be interpreted as j * i * j; j * i is -k, and -k * j is i, so jij reduces to i.) If this is possible, you will get your point back. Can you find a way to do it?
Input

The first line of the input gives the number of test cases, T. T test cases follow. Each consists of one line with two space-separated integers L and X, followed by another line with L characters, all of which are i, j, or k. Note that the string never contains negative signs, 1s, or any other characters. The string that you are to evaluate is the given string of L characters repeated X times. For instance, for L = 4, X = 3, and the given string kiij, your input string would be kiijkiijkiij.
Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is either YES or NO, depending on whether the string can be broken into three parts that reduce to i, j, and k, in that order, as described above.
Limits

1 ≤ T ≤ 100.
1 ≤ L ≤ 10000.
Small dataset

1 ≤ X ≤ 10000.
1 ≤ L * X ≤ 10000.
Large dataset

1 ≤ X ≤ 1012.
1 ≤ L * X ≤ 1016.
Sample

Input
  	
Output
 

5
2 1
ik
3 1
ijk
3 1
kji
2 6
ji
1 10000
i

	

Case #1: NO
Case #2: YES
Case #3: NO
Case #4: YES
Case #5: NO

In Case #1, the string is too short to be split into three substrings.

In Case #2, just split the string into i, j, and k.

In Case #3, the only way to split the string into three parts is k, j, i, and this does not satisfy the conditions.

In Case #4, the string is jijijijijiji. It can be split into jij (which reduces to i), iji (which reduces to j), and jijiji (which reduces to k).

In Case #5, no matter how you choose your substrings, none of them can ever reduce to a j or a k.


 * @author dima
 *
 */

public class App {
	public static final String INPUT_FILE_NAME = "input.txt";
	public static final String OUTPUT_FILE_NAME = "output.txt";
	
	public static final int MAX_THREADS = 10;

	
	public static void main(String[] args) {
		long beginTime = System.currentTimeMillis();
		
		try {
			String inputName = INPUT_FILE_NAME;
			String outputName = OUTPUT_FILE_NAME;
		
			if (args.length > 0)
				inputName = args[0];
		
			if (args.length > 1)
				outputName = args[1];
			
			String[] vector = new String[4];
			vector[0] = "1";
			vector[1] = "i";
			vector[2] = "j";
			vector[3] = "k";
			
			String[][] matix = new String[4][4];
			matix[0][0] = "1";
			matix[0][1] = "i";
			matix[0][2] = "j";
			matix[0][3] = "k";
			matix[1][0] = "i";
			matix[1][1] = "-1";
			matix[1][2] = "k";
			matix[1][3] = "-j";
			matix[2][0] = "j";
			matix[2][1] = "-k";
			matix[2][2] = "-1";
			matix[2][3] = "i";
			matix[3][0] = "k";
			matix[3][1] = "j";
			matix[3][2] = "-i";
			matix[3][3] = "-1";
			
			Semaphore semaphore = new Semaphore(MAX_THREADS);
		
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputName)))) {
				try (PrintWriter writer = new PrintWriter(outputName)) {
					int nCases = Integer.parseInt(reader.readLine());

					List<WorkThread> threads = new ArrayList<WorkThread>();

					boolean[] results = new boolean[nCases];
					
					for (int nCase = 0; nCase < nCases; ++nCase) {
						String[] data = reader.readLine().split(" ");

						BigInteger len = new BigInteger(data[0]);
						BigInteger repeat = new BigInteger(data[1]);
						BigInteger total = len.multiply(repeat);
						String str = reader.readLine();
						
						semaphore.acquire(); 
						WorkThread thread = new WorkThread(semaphore, results, nCase, len, total, str);
						threads.add(thread);
						thread.start();
						
					}
					
					for (WorkThread thread : threads)
						thread.join();
					
					for (int nCase = 0; nCase < nCases; ++nCase) 
						writer.println("Case #" + (nCase + 1) + ": " + (results[nCase] ? "YES" : "NO"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		long endTime = System.currentTimeMillis();
		
		System.out.println(String.format("Done. Spend %d ms", endTime - beginTime));
	}
}

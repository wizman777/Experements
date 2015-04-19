package dijkstra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

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
	
	private static final byte C_ONE = 0;
	private static final byte C_I = 1;
	private static final byte C_J = 2;
	private static final byte C_K = 3;
	private static final byte C_N_ONE = 4;
	private static final byte C_N_I = 5;
	private static final byte C_N_J = 6;
	private static final byte C_N_K = 7;
	
	public static final byte[][] MATRIX = new byte[8][8];

	static {
		MATRIX[C_ONE][C_ONE] = C_ONE;
		MATRIX[C_ONE][C_I] = C_I;
		MATRIX[C_ONE][C_J] = C_J;
		MATRIX[C_ONE][C_K] = C_K;
		MATRIX[C_I][C_ONE] = C_I;
		MATRIX[C_I][C_I] = C_N_ONE;
		MATRIX[C_I][C_J] = C_K;
		MATRIX[C_I][C_K] = C_N_J;
		MATRIX[C_J][C_ONE] = C_J;
		MATRIX[C_J][C_I] = C_N_K;
		MATRIX[C_J][C_J] = C_N_ONE;
		MATRIX[C_J][C_K] = C_I;
		MATRIX[C_K][C_ONE] = C_K;
		MATRIX[C_K][C_I] = C_J;
		MATRIX[C_K][C_J] = C_N_I;
		MATRIX[C_K][C_K] = C_N_ONE;
		
		for (int i = 0; i < 4; ++i)
			for (int j = 0; j < 4; ++j) {
				MATRIX[i+4][j] = inverse(MATRIX[i][j]);
				MATRIX[i][j+4] = inverse(MATRIX[i][j]);
				MATRIX[i+4][j+4] = MATRIX[i][j];
			}
	}
	
	public static byte inverse(byte value) {
		return (byte) (value >= C_N_ONE ? value - 4 : value + 4);
	}
	
	public static byte getValue(char value) throws Exception {
		switch(value) {
		case 'i':
			return C_I;
		case 'j':
			return C_J;
		case 'k':
			return C_K;
		}
		
		throw new Exception("Invalid character"); 
	}

	public static byte getProduct(byte value1, byte value2) throws Exception {
		return MATRIX[value1][value2];
	}
	
	public static byte[] convert(String str, long len) throws Exception {
		byte[] data = new byte[(int) len];
		for (int i = 0; i < len; ++i)
			data[i] = getValue(str.charAt(i));
		
		return data;
	}
	
	public static byte calcProduct(byte[] data, long len) throws Exception {
		byte result = data[0];
		for (int i = 1; i < len; ++i) 
			result = getProduct(result, data[i]);
		
		return result;			
	}
	
	public static boolean isCompute(byte product, long repeat) {
		if (product == C_ONE)
			return false;
		if (product == C_N_ONE)
			return repeat % 2 != 0;
		
		return repeat % 2 == 0 && repeat % 4 != 0;
	}

	public static Long findProduct(byte[] data, long len, long from, long to, byte product) throws Exception {
		byte result = C_ONE;
		for (long i = from; i < to; ++i) {
			result = getProduct(result, data[(int) (i % len)]);
			if (result == product) 
				return i;
		}
		
		return null;	
	}
	
	public static Long findProductReverse(byte[] data, long len, long from, long to, byte product) throws Exception {
		byte result = C_ONE;
		for (long i = to-1; i >= from; --i) {
			result = getProduct(data[(int) (i % len)], result);
			if (result == product) 
				return i;
		}
		
		return null;			
	}

	
	public static void main(String[] args) {
		long beginTime = System.currentTimeMillis();
		
		try {
			String inputName = INPUT_FILE_NAME;
			String outputName = OUTPUT_FILE_NAME;
		
			if (args.length > 0)
				inputName = args[0];
		
			if (args.length > 1)
				outputName = args[1];
			
			try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputName)))) {
				try (PrintWriter writer = new PrintWriter(outputName)) {
					int nCases = Integer.parseInt(reader.readLine());

					for (int nCase = 0; nCase < nCases; ++nCase) {
						String[] data = reader.readLine().split(" ");

						long len = Long.parseLong(data[0]);
						long repeat = Long.parseLong(data[1]);
						String str = reader.readLine();
								
						boolean output = false;
						
						// convert characters to bytes
						byte[] data1 = convert(str, len);
						// evaluate total product of data
						byte product = calcProduct(data1, len);
						// check what total product will be equal to -1
						if (isCompute(product, repeat)) {
							// find first occurrence of i
							long total = len * repeat;
							Long part1 = findProduct(data1, len, 0, total, C_I);
							if (null != part1) {
								// find last occurrence of k
								Long part2 = findProductReverse(data1, len, part1 + 2, total, C_K);
								if (null != part2 && part1 + 1 < part2) 
									// the last of string should sum to j, no future tests requires
									output = true;
							}
						}
						
						System.out.println("Case #" + (nCase + 1) + ": " + (output ? "YES" : "NO"));
						writer.println("Case #" + (nCase + 1) + ": " + (output ? "YES" : "NO"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		long endTime = System.currentTimeMillis();
		
		System.out.println(String.format("Done. Spend %d ms", endTime - beginTime));
	}
}

package omnio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 Problem

An N-omino is a two-dimensional shape formed by joining N unit cells fully along their edges in some way. More formally, a 1-omino is a 1x1 unit square, and an N-omino is an (N-1)omino with one or more of its edges joined to an adjacent 1x1 unit square. For the purpose of this problem, we consider two N-ominoes to be the same if one can be transformed into the other via reflection and/or rotation. For example, these are the five possible 4-ominoes:



And here are some of the 108 possible 7-ominoes:



Richard and Gabriel are going to play a game with the following rules, for some predetermined values of X, R, and C:

1. Richard will choose any one of the possible X-ominoes.
2. Gabriel must use at least one copy of that X-omino, along with arbitrarily many copies of any X-ominoes (which can include the one Richard chose), to completely fill in an R-by-C grid, with no overlaps and no spillover. That is, every cell must be covered by exactly one of the X cells making up an X-omino, and no X-omino can extend outside the grid. Gabriel is allowed to rotate or reflect as many of the X-ominoes as he wants, including the one Richard chose. If Gabriel can completely fill in the grid, he wins; otherwise, Richard wins.

Given particular values X, R, and C, can Richard choose an X-omino that will ensure that he wins, or is Gabriel guaranteed to win no matter what Richard chooses?
Input

The first line of the input gives the number of test cases, T. T lines follow. Each contains three space-separated integers: X, R, and C.
Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is either RICHARD (if there is at least one choice that ensures victory for Richard) or GABRIEL (if Gabriel will win no matter what Richard chooses).
Limits
Small dataset

T = 64.
1 ≤ X, R, C ≤ 4.
Large dataset

1 ≤ T ≤ 100.
1 ≤ X, R, C ≤ 20.
Sample

Input
  	
Output
 

4
2 2 2
2 1 3
4 4 1
3 2 3

	

Case #1: GABRIEL
Case #2: RICHARD
Case #3: RICHARD
Case #4: GABRIEL

In case #1, Richard only has one 2-omino available to choose -- the 1x2 block formed by joining two unit cells together. No matter how Gabriel places this block in the 2x2 grid, he will leave a hole that can be exactly filled with another 1x2 block. So Gabriel wins.

In case #2, Richard has to choose the 1x2 block, but no matter where Gabriel puts it, he will be left with a single 1x1 hole that he cannot fill using only 2-ominoes. So Richard wins.

In case #3, one winning strategy for Richard is to choose the 2x2 square 4-omino. There is no way for Gabriel to fit that square into the 4x1 grid such that it is completely contained within the grid, so Richard wins.

In case #4, Richard can either pick the straight 3-omino or the L-shaped 3-omino. In either case, Gabriel can fit it into the grid and then use another copy of the same 3-omino to fill in the remaining hole.
*/

public class App {
	public static final String INPUT_FILE_NAME = "input.txt";
	public static final String OUTPUT_FILE_NAME = "output.txt";
	
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
						int x = Integer.parseInt(data[0]);
						int r = Integer.parseInt(data[1]);
						int c = Integer.parseInt(data[2]);
						
						String reason = "";
						System.out.println("x: " + x + ", r: " + r + ", c: " + c);
						
						boolean richardWon = x > 1;
						if (richardWon) {
							if (r > c && c > 1) {
								if ((r % c) == 0)
									r = r / c;
							} else if (c > r && r > 1) {
								if ((c % r) == 0)
									c = c / r;
							}
							
							if (x < 7) { // 7 omnion - richard will win automatically no matter that
								if ((r * c) % x == 0) { // total space should be equal n * x, or richard will win 
									if (x <= r || x <= c) { // check for I omniom
										int L = x / 2; 
										int T = x - L;
										if (x % 2 != 0)
											++L;
										if ((L < r || L == r && T <= c ) && (L < c || L == c && T < r)) { // check for L omniom
	/*										int T = x - L; // check for T omnion
											if (L == r && T * 2 > c || L == c && T * 2 > r)
												richardWon = true;
											else*/
												richardWon = false;
										}
										else
											reason = "L-omniom is too big";
									} else
										reason = "I-omniom is too long";								
								} else
									reason = "space is not enough";
								
							} else
								reason = "too big omniom";
						}
						
						System.out.println("Case #" + (nCase + 1) + ": " + (richardWon ? ("RICHARD" + ", reason: " + reason) : "GABRIEL"));
						writer.println("Case #" + (nCase + 1) + ": " + (richardWon ? "RICHARD" : "GABRIEL"));
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

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
						int n = Integer.parseInt(data[0]);
						int x = Integer.parseInt(data[1]);
						int y = Integer.parseInt(data[2]);
						
						System.out.println("x: " + n + ", r: " + x + ", c: " + y);
						
						// x must be >= y, transpone the game field if needed
						if (x < y) { 
							int t = x;
							x = y;
							y = t;
						}

						boolean richardWon;
						if ((x * y) % n != 0) 		// Richard will always win on any disproportional game field
							richardWon = true;
						else if (n == 1 || n == 2) 	// with N=1 or 2 Garcia will always win
							richardWon = false;
						else if (n == 3)			// with N=3, Garcia wins if Y > 1 
							richardWon = (y == 1);
						else if (n == 4)			// with N=4, Garcia wins if Y > 2 
							richardWon = (y <= 2);
						else if (n == 5)			// with N=5, Garcia wins if Y > 2 and X,Y != 5,3 
							richardWon = (y <= 2 || x == 5 && y == 3);
						else if (n == 6)			// with N=6, Garcia wins if Y > 3
							richardWon = (y <= 3);
						else 						// with N>=7, Richard will always wins
							richardWon = true;

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

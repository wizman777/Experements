package pankakes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Problem

At the Infinite House of Pancakes, there are only finitely many pancakes, but there are infinitely many diners who would be willing to eat them! When the restaurant opens for breakfast, among the infinitely many diners, exactly D have non-empty plates; the ith of these has Pi pancakes on his or her plate. Everyone else has an empty plate.

Normally, every minute, every diner with a non-empty plate will eat one pancake from his or her plate. However, some minutes may be special. In a special minute, the head server asks for the diners' attention, chooses a diner with a non-empty plate, and carefully lifts some number of pancakes off of that diner's plate and moves those pancakes onto one other diner's (empty or non-empty) plate. No diners eat during a special minute, because it would be rude.

You are the head server on duty this morning, and it is your job to decide which minutes, if any, will be special, and which pancakes will move where. That is, every minute, you can decide to either do nothing and let the diners eat, or declare a special minute and interrupt the diners to make a single movement of one or more pancakes, as described above.

Breakfast ends when there are no more pancakes left to eat. How quickly can you make that happen?
Input

The first line of the input gives the number of test cases, T. T test cases follow. Each consists of one line with D, the number of diners with non-empty plates, followed by another line with D space-separated integers representing the numbers of pancakes on those diners' plates.
Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is the smallest number of minutes needed to finish the breakfast.
Limits

1 ≤ T ≤ 100.
Small dataset

1 ≤ D ≤ 6.
1 ≤ Pi ≤ 9.
Large dataset

1 ≤ D ≤ 1000.
1 ≤ Pi ≤ 1000.
Sample

Input
  	
Output
 

3
1
3
4
1 2 1 2
1
4

	

Case #1: 3
Case #2: 2
Case #3: 3

In Case #1, one diner starts with 3 pancakes and everyone else's plate is empty. One optimal strategy is:

Minute 1: Do nothing. The diner will eat one pancake.

Minute 2 (special): Interrupt and move one pancake from that diner's stack onto another diner's empty plate. (Remember that there are always infinitely many diners with empty plates available, no matter how many diners start off with pancakes.) No pancakes are eaten during an interruption.

Minute 3: Do nothing. Each of those two diners will eat one of the last two remaining pancakes.

In Case #2, it is optimal to let the diners eat for 2 minutes, with no interruptions, during which time they will finish all the pancakes.

In Case #3, one diner starts with 4 pancakes and everyone else's plate is empty. It is optimal to use the first minute as a special minute to move two pancakes from the diner's plate to another diner's empty plate, and then do nothing and let the diners eat for the second and third minutes. 

 * @author dima
 *
 */

public class App {
	public static final String INPUT_FILE_NAME = "input.txt";
	public static final String OUTPUT_FILE_NAME = "output.txt";
	
	public static void outPancakes(List<Integer> list) {
		System.out.print("Pancakes :");
		for (Integer p : list) 
			System.out.print(" " + p);
		
		System.out.println();
	}
	
	public static int getMinMinutes(List<Integer> pancakes, int n) {
		int splitMinutes = 0;
		int eatMinutes = 0;
		for (int p : pancakes) {
			if (p > n) {
				splitMinutes += p / n + ((p % n) != 0 ? 1 : 0) - 1;
				eatMinutes = n;
			} else {
				if (0 == eatMinutes)
					eatMinutes = p;
				break;
			}
		}
		
		return splitMinutes + eatMinutes;
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
						int plates = Integer.parseInt(reader.readLine());
						String[] data = reader.readLine().split(" ");
						
						if (plates != data.length)
							throw new Exception("The data is invalid");
						
						List<Integer> pancakes = new ArrayList<Integer>();
						for (int plate = 0; plate < plates; ++plate) 
							pancakes.add(Integer.parseInt(data[plate]));
						
						Collections.sort(pancakes, Collections.reverseOrder());
						outPancakes(pancakes);
						
						int minutes = 0;
						int maxPancakes = pancakes.get(0);
						if (maxPancakes <= 2)
							minutes =  maxPancakes;
						else 
							for (int n = 2; n <= maxPancakes; ++n) {
								int minMinutes = getMinMinutes(pancakes, n);
								if (minutes == 0 || minutes > minMinutes)
									minutes = minMinutes;
							}
						
						System.out.println("Case #" + (nCase + 1) + ": " + minutes);
						writer.println("Case #" + (nCase + 1) + ": " + minutes);
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

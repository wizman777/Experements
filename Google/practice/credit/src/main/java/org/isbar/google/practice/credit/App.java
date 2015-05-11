package org.isbar.google.practice.credit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Qualification Round Africa 2010
 * https://code.google.com/codejam/contest/351101/dashboard#s=p0
 * 
 * Problem A. Store Credit
 * 
 * Small Sample: Solved
 * Large Sample: Solved
 * 
 * @author dima
 *
 *
 */

public class App {

	public static final String INPUT_FILE_NAME = "input.txt";
	public static final String OUTPUT_FILE_NAME = "output.txt";
	
	public static class Value implements Comparable<Value> {
		private final int position;
		private final int value;
		
		public Value(int position, int value) {
			this.position = position;
			this.value = value;
		}
		
		public int getPosition() {
			return position;
		}

		public int getValue() {
			return value;
		}

		@Override
		public int compareTo(Value value) {
			return value.value - this.value;
		}
		
		@Override
		public String toString() {
			return "Value [position=" + position + ", value=" + value + "]";
		}

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
						int credit = Integer.parseInt(reader.readLine());
						int nItems = Integer.parseInt(reader.readLine());
						
						String[] itemsString = reader.readLine().split(" ");
						Value[] items = new Value[nItems];

						if (itemsString.length != nItems)
							throw new Exception("Error, the number of items is invalid. Expecting " + nItems + " but got only " + items.length);
						
						for (int nItem = 0; nItem < nItems; ++nItem) 
							items[nItem] = new Value(nItem + 1, Integer.parseInt(itemsString[nItem]));
						
						Arrays.sort(items);
						
						Integer sum;
						Boolean solved = false;
						for (int nItem = 0; nItem < nItems; ++nItem) 
							if (items[nItem].getValue() < credit) 
								for (int nItem2 = nItems-1; nItem2 > nItem; --nItem2) {
									sum = items[nItem].getValue() + items[nItem2].getValue();
									if (sum == credit) {
										int position1 = items[nItem].getPosition();
										int position2 = items[nItem2].getPosition();
										if (position1 > position2) {
											int tmp = position1;
											position1 = position2;
											position2 = tmp;
										}
										
										writer.println(String.format("Case #%d: %d %d", 
												nCase + 1, position1, position2));
										solved = true;
										
										break;
									}
									else if (sum > credit)
										break;
								}
						
						if (!solved)
							throw new Exception("The Case " + (nCase + 1) + " has not been solved");
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

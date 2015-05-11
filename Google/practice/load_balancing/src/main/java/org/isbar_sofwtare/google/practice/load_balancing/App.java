package org.isbar_sofwtare.google.practice.load_balancing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Round 1C 2010
 * https://code.google.com/codejam/contest/619102/dashboard#s=p1
 * 
 * Problem B. Load Testing
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
						String[] items = reader.readLine().split(" ");
						
						long total = 0;
						
						long min = Integer.parseInt(items[0]);
						long max = Integer.parseInt(items[1]);
						long factor = Integer.parseInt(items[2]);
					
						//System.out.println("min: " + min + ", max: " + max + ", factor: " + factor);
						
						if (min * factor < max) {
							long counter = 0;
						
							for (long test = min * factor; test < max; test = test * factor) 
								++counter;
						
							if (counter > 0) {
								++total;
							
								while (counter > 1) {
									counter = (long) ((float) counter / 2.0);
									++total;
								}
							}
						}
						
						writer.println("Case #" + (nCase + 1) + ": " + total);
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

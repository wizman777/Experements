package org.isbar_software.google.practice.reverse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Qualification Round Africa 2010
 * https://code.google.com/codejam/contest/351101/dashboard#s=p1
 * 
 * Problem B. Reverse Words
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

						writer.print("Case #" + (nCase + 1) + ":");
						for (int n = items.length - 1; n >= 0; --n) 
							writer.print(" " + items[n]);
						
						writer.println();
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

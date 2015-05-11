package org.isbar_software.google.practice.t9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Qualification Round Africa 2010
 * https://code.google.com/codejam/contest/351101/dashboard#s=p2
 * 
 * Problem C. T9 Spelling
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
		
		Map<Integer, String> t1 = new HashMap<Integer, String>();
		t1.put(2, "abc");
		t1.put(3, "def");
		t1.put(4, "ghi");
		t1.put(5, "jkl");
		t1.put(6, "mno");
		t1.put(7, "pqrs");
		t1.put(8, "tuv");
		t1.put(9, "wxyz");

		Map<Byte, String> t2 = new HashMap<Byte, String>();
		t2.put((byte) ' ', "0");
		for (Entry<Integer, String> entry : t1.entrySet()) {
			if (entry.getKey() >  0) {
				String value = entry.getValue();
				for (int i = 0; i < value.length(); ++i) {
					int key = 0;
					for (int r = 0; r < i + 1; ++r)
						key = key * 10 + entry.getKey();
					
					t2.put((byte) value.charAt(i), Integer.toString(key));
				}
			}
		}

		
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
						String line = reader.readLine();

						StringBuilder sb = new StringBuilder();
						String input, last = null;
						for (int i = 0; i < line.length(); ++i) {
							byte c = (byte) line.charAt(i);
							input = t2.get(c);
							if (last != null && last.charAt(0) == input.charAt(0))
								sb.append(" ");
							sb.append(input);
							last = input;
						}
						
						writer.println("Case #" + (nCase + 1) + ": " + sb.toString());
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

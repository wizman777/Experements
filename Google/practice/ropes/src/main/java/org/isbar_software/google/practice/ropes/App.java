package org.isbar_software.google.practice.ropes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Round 1C 2010
 * https://code.google.com/codejam/contest/619102/dashboard#s=p0
 * 
 * Problem A. Rope Intranet
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
	
	public static class Rope {
		private final int x1;
		private final int x2;
		
		public Rope(int x1, int x2) {
			this.x1 = x1;
			this.x2 = x2;
		}

		public int getX1() {
			return x1;
		}

		public int getX2() {
			return x2;
		}
		
		public boolean isIntercept(Rope rope) {
			return ((x1 - rope.x1) * (x2 - rope.x2)) < 0;
		}

		@Override
		public String toString() {
			return "Rope [x1=" + x1 + ", x2=" + x2 + "]";
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
						int nRopes = Integer.parseInt(reader.readLine());
						
						Rope[] ropes = new Rope[nRopes];
						for (int nRope = 0; nRope < nRopes; ++nRope) {
							String[] items = reader.readLine().split(" ");
							ropes[nRope] = new Rope(Integer.parseInt(items[0]), Integer.parseInt(items[1]));
						}
						
						int intercept = 0;
						
						for (int nRope1 = 0; nRope1 < nRopes; ++nRope1) 
							for (int nRope2 = nRope1; nRope2 < nRopes; ++nRope2) {
								if (ropes[nRope1].isIntercept(ropes[nRope2]))
									++intercept;
							}
						
						writer.println("Case #" + (nCase + 1) + ": " + intercept);
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

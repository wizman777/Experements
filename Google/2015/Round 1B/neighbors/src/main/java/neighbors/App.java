package neighbors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigInteger;

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
						
						int result;
						int r = Integer.parseInt(data[0]);
						int c = Integer.parseInt(data[1]);
						int n = Integer.parseInt(data[2]);
						
						if (r > c) {
							int t = r;
							r = c;
							c = t;
						}
						
						// total rooms
						int t = r * c;
						// empty rooms
						int e = t - n;
						// total walls
						int w = (r - 1) * c + (c - 1) * r;
						
						// if there is empty apartments
						if (e > 0) {
							if (r >= 3) {
								int w4 = (r - 2) * (c - 2);
								if (w4 > 1)
									w4 /= 2;
								
								if (e <= w4) {
									w -= e * 4;
								} else {
									w -= w4 *4;
									e -= w4;
									
									int w3 = 
								}
								
								if (w4 >= e) {
									w -= e * 4;
								} else {
									int w3 = (r - 1) * (c - 1) / 2;
								}
							}
						}
							
						
						System.out.println("Case #" + (nCase + 1) + ": " + counter);
						writer.println("Case #" + (nCase + 1) + ": " + counter);
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

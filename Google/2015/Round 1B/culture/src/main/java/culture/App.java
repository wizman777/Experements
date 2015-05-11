package culture;

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
						BigInteger n = new BigInteger(reader.readLine());
						BigInteger i = BigInteger.ONE;
						long counter = 1;
						
						while (i.compareTo(n) < 0) {
							
							
							
						}
						
						while (n > 11) {
							String r = new StringBuilder(Integer.toString(n)).reverse().toString();
							if (r.charAt(0) != '0') {
								int n1 = Integer.parseInt(r);
								
								if (n1 < n) {
									n = n1;
								} else
									--n;
							} else
								--n;
							
							++counter; 							
						}
						
						counter += n;
						
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

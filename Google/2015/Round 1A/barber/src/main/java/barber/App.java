package barber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class App {
	public static final String INPUT_FILE_NAME = "input.txt";
	public static final String OUTPUT_FILE_NAME = "output.txt";

	public static long countSitted(int n, int[] minutes, long time) {
		long counter = 0;
		for (int i = 0; i < n; ++i)
			counter += time / minutes[i] + 1;
		
		return counter;
	}
	
	public static int findFree(int n, int[] minutes, long skip, long time) {
		for (int i = 0; i < n; ++i)
			if ((time % minutes[i]) == 0) {
				if (skip > 0)
					--skip;
				else
					return i;
			}
		
		return -1;
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
						String[] data = reader.readLine().split(" ");

						int nBarbers = Integer.parseInt(data[0]);
						long nPlace = Long.parseLong(data[1]);

						data = reader.readLine().split(" ");
						
						int[] minutes = new int[nBarbers];
						double customerPerMinute = 0;
						for (int i = 0; i < nBarbers; ++i) {
							minutes[i] = Integer.parseInt(data[i]);
							customerPerMinute += 1.0 / (double) minutes[i];
						}

						long time = (long) ((double) nPlace / customerPerMinute); 
						long sitted = countSitted(nBarbers, minutes, time);
						while (sitted >= nPlace) {
							--time;
							sitted = countSitted(nBarbers, minutes, time);
						}
						
						++time;
						
						int freeBarber;
						for(;;)	 {
							if (nPlace - sitted <= nBarbers) 
								if ((freeBarber = findFree(nBarbers, minutes, nPlace -1 - sitted, time)) != -1)
									break;
								
							++time;
							sitted = countSitted(nBarbers, minutes, time);
						}
						
						String out = "Case #" + (nCase + 1) + ": " + (freeBarber + 1);
						System.out.println(out);
						writer.println(out);
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

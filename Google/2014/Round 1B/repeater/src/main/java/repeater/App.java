package repeater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;

// Problem A. The Repeater
// from https://code.google.com/codejam/contest/2994486/dashboard
// Small Data sample: Solved
// Large Data sample: Solved

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
						int nStrings = Integer.parseInt(reader.readLine());
						String[] strings = new String[nStrings];
						int[] idx = new int[nStrings];
						int[] size = new int[nStrings];
						int counter = 0;
						
						for (int n = 0; n < nStrings; ++n) {
							strings[n] = reader.readLine();
							idx[n] = 0;
						}
								
						for(;;) {
							
							char c = 0x0;
							for (int n = 0; n < nStrings; ++n) {
								size[n] = 0;
								while (idx[n] < strings[n].length()) {
									if (c == 0x0)
										c = strings[n].charAt(idx[n]);
									else if (c != strings[n].charAt(idx[n]))
										break;
									++size[n];
									++idx[n];									
								}
															
								if (c != 0x0 && size[n] == 0) {
									counter = -1;
									break; // Fegla Won
								}
							}
							
							if (c == 0x0 || counter < 0)
								break; // Fegla Won
							
							Arrays.sort(size);
							for (;;) {
								int a = 0;
								int s = 0;
								
								for (int x : size) {
									if (x == size[0])
										++a;
									else if (x == size[size.length -1])
										++s;
								}
								
								if (a == size.length)
									break; // all erquals
								if (a <= s) {
									counter += a;
									for (int i = 0; i < a; ++i)
										++size[i];
								} else {
									counter +=s;
									for (int i = 0; i < s; ++i)
										--size[size.length - i - 1];
								}
							}	
 						}

						/*
						int counter2 = 0;
						if (strings[0].charAt(0) != strings[1].charAt(0))
							counter2 = -1;
						else {
							char c = strings[0].charAt(0);
							int i = 1;
							for(;;) {
								if (i < strings[0].length() && i < strings[1].length()) {
									if (strings[0].charAt(i) != strings[1].charAt(i)) {
										if (strings[0].charAt(i) == c) {
											strings[0] = strings[0].substring(0, i) + strings[0].substring(i+1, strings[0].length());
											++counter2;		
										} else if (strings[1].charAt(i) == c) {
											strings[1] = strings[1].substring(0, i) + strings[1].substring(i+1, strings[1].length());
											++counter2;		
										} else {
											counter2 = -1;
											break;	
										}
									} else {
										c = strings[0].charAt(i);
										++i;
									}
								} else {
									if (i < strings[0].length()) {
										if (strings[0].charAt(i) == c) {
											strings[0] = strings[0].substring(0, i) + strings[0].substring(i+1, strings[0].length());
											++counter2;		
										} else {
											counter2 = -1;
											break;
										}
									} else if (i < strings[1].length()) {
										if (strings[1].charAt(i) == c) {
											strings[1] = strings[1].substring(0, i) + strings[1].substring(i+1, strings[1].length());
											++counter2;		
										} else {
											counter2 = -1;
											break;
										}
									} else
										break;
								} 
							}
						}
						
						if (counter != counter2) {
							int gotcha = 1;
							++gotcha;
							
							
						}*/
						
						System.out.println("Case #" + (nCase + 1) + ": " + (counter >= 0 ? Integer.toString(counter) : "Fegla Won"));
						writer.println("Case #" + (nCase + 1) + ": " + (counter >= 0 ? Integer.toString(counter) : "Fegla Won"));
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

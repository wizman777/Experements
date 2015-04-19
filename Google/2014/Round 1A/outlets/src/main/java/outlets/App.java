package outlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class App {
	public static final String INPUT_FILE_NAME = "input.txt";
	public static final String OUTPUT_FILE_NAME = "output.txt";
	
	public static int charToValue(char c) throws Exception {
		if (c == '0')
			return -1;
		else if (c == '1')
			return 1;
		else
			throw new Exception("Invalid character " + c);
	}
	
	public static String switchBit(String mask, int bit) throws Exception {
		char[] chars = mask.toCharArray();
		if (chars[bit] == '0')
			chars[bit] = '1';
		else if (chars[bit] == '1')
			chars[bit] = '0';
		else
			throw new Exception("Invalid character " + chars[bit]);
		
		return String.valueOf(chars);
	}
	
	public static void switchMaks(String[] masks, int n, int bit) throws Exception {
		for (int i = 0; i < n; ++i) 
			masks[i] = switchBit(masks[i], bit);
	}
	
	public static boolean check(String[] outlets, String[] devices, int n) {
		for (int i = 0; i < n; ++i) {
			boolean found = false;
			for (int j = 0; j < n; ++j) 
				if (devices[i].equals(outlets[j])) {
					found = true;
					break;
				}
			
			if (!found)
				return false;
		}
		
		return true;
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
						int nDevices = Integer.parseInt(data[0]);
						int lBits = Integer.parseInt(data[1]);
						
						System.out.println("nDevices: " + nDevices + ", lBits: " + lBits);
						
						String[] outlets = reader.readLine().split(" ");
						String[] devices = reader.readLine().split(" ");
						
						boolean solutionPossible = true;
						int[] maskDevices = new int[lBits];
						int[] maskOutlets = new int[lBits];
						
						int counter = 0;
						
						List<Integer> possible = new ArrayList<Integer>(); 
						
						for (int i = 0; i < lBits; ++i) {
							maskDevices[i] = 0;
							maskOutlets[i] = 0;
							
							for (int j = 0; j < nDevices; ++j) {
								maskDevices[i] += charToValue(devices[j].charAt(i));
								maskOutlets[i] += charToValue(outlets[j].charAt(i));
							}
							
							if (maskOutlets[i] == 0 && maskDevices[i] == 0) {
								possible.add(i);
							} else if (maskOutlets[i] + maskDevices[i] == 0) {
								switchMaks(outlets, nDevices, i);
								++counter;
							} else if (maskDevices[i] != maskOutlets[i]) {
								solutionPossible = false;
								break;
							}
						}
						
						if (solutionPossible) {
							if (!check(outlets, devices, nDevices)) {
								int solutions = (int) Math.pow(2, possible.size());
								int minOperations = 0;
								solutionPossible = false;
								for (int i = 1; i < solutions; ++i) {
									String[] outletSolution = outlets.clone();
									
									int counter2 = 0;
									for (int bit = 0; bit < possible.size(); ++bit) {
										if ((i & (1 << bit)) != 0) {
											switchMaks(outletSolution, nDevices, possible.get(bit));
											++counter2;
										}
									}
									
									if ((!solutionPossible || counter < minOperations) && 
										check(outletSolution, devices, nDevices)) {
										solutionPossible = true;
										minOperations = counter2;
									}										
								}
								
								if (solutionPossible)
									counter += minOperations;
									
							}
						}

						String out = "Case #" + (nCase + 1) + ": " + (solutionPossible ? Integer.toString(counter) : "NOT POSSIBLE");
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

package org.isbar_software.benchmarks.dynamodb;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class App {
	private static final String PROPERTIES_FILE = "properties/benchmark_dynamodb.properties";
	
	public static void main(String[] args) {
		String propertiesFile = PROPERTIES_FILE;
		if (args.length > 0 && !args[0].isEmpty())
			propertiesFile = args[0];
		
		try {
			Properties properties = new Properties();
			try (InputStream in = new FileInputStream(propertiesFile)) {
	            properties.load(in);
			}
			
			Benchmark bm = new Benchmark(properties);
			bm.process();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

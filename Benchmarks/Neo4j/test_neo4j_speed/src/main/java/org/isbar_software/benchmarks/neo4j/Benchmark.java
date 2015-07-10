package org.isbar_software.benchmarks.neo4j;

import java.util.Properties;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Benchmark {
	
	private static final String NEO4J_CONF = "/conf/neo4j.properties";
	private static final String NEO4J_DB = "/data/graph.db";
	
	private final String dbPath;
	private final int count;
	
	public Benchmark(Properties properties) {
		dbPath = properties.getProperty("neo4j");
		count = Integer.parseInt(properties.getProperty("count"));
	}

	public void process() {
		GraphDatabaseService graphDb = getGraphDb(dbPath);
		
	    long beginTime = System.currentTimeMillis();
		
		try ( Transaction tx = graphDb.beginTx() ) {
			for (int i = 0; i < count; ++i) {
				Node node = graphDb.createNode();
				node.setProperty("id", i);
			}
			
			tx.success();
		}
		
		long endTime = System.currentTimeMillis();
        System.out.println(String.format("Done. Imported %d nodes over %d ms. Average %f ms per node", 
        		count, endTime - beginTime, (float)(endTime - beginTime) / (float)count));
		
		graphDb.shutdown();
	}
	
	private GraphDatabaseService getGraphDb( final String graphDbPath ) {
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
			.newEmbeddedDatabaseBuilder( graphDbPath + NEO4J_DB )
			.loadPropertiesFromFile( graphDbPath + NEO4J_CONF )
			.newGraphDatabase();
		
		registerShutdownHook( graphDb );
		
		return graphDb;
	}
	
	private void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
}

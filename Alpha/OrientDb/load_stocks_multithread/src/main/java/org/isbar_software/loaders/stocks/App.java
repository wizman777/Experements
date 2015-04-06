package org.isbar_software.loaders.stocks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.orientechnologies.orient.core.exception.OStorageException;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class App {

	public static final String CLASS_SYMBOL_MINUTE = "SymbolMinute";
	public static final String CLASS_SYMBOL_DAY = "SymbolDay";
	
	public static void main(String[] args) {
		// OPEN THE DATABASE
		OObjectDatabaseTx db;
		try {
			db = OObjectDatabasePool.global().acquire("plocal:./databases/stocks", "admin", "admin");
		} catch (OStorageException exc) {
			db = new OObjectDatabaseTx("plocal:./databases/stocks");
		    if (!db.exists())
		    	db.create();
		    else
		    	db.open("admin", "admin");
		}
		 
		try {
			OSchema schema = db.getMetadata().getSchema();
			OClass symbolMinute;
			if (!schema.existsClass(CLASS_SYMBOL_MINUTE)) {
				// create class for storing minute data
				symbolMinute = schema.createClass(CLASS_SYMBOL_MINUTE);
					
				symbolMinute.createProperty("symbol", OType.STRING).setMandatory(true).setNotNull(true);
				symbolMinute.createProperty("time", OType.DATETIME).setMandatory(true).setNotNull(true);
				symbolMinute.createProperty("open", OType.FLOAT).setMandatory(true).setNotNull(true);
				symbolMinute.createProperty("low", OType.FLOAT).setMandatory(true).setNotNull(true);
				symbolMinute.createProperty("high", OType.FLOAT).setMandatory(true).setNotNull(true);
				symbolMinute.createProperty("close", OType.FLOAT).setMandatory(true).setNotNull(true);
				symbolMinute.createProperty("volume", OType.INTEGER).setMandatory(true).setNotNull(true);
					
			//	symbolMinute.createIndex("symbolTime", OClass.INDEX_TYPE.UNIQUE, "symbol", "time");
			}
			else
				symbolMinute = schema.getClass(CLASS_SYMBOL_MINUTE);
		} 
		finally {
			if (!db.isClosed())
				db.close();
		}

		long beginTime = System.currentTimeMillis();
		
		List<DbThread> threads = new ArrayList<DbThread>();
		for (int i = 0; i < 5; ++i) {
			DbThread thread = new DbThread("AAL-" + (i+1) + ".txt", "AAL");
			thread.start();
			threads.add(thread);
		}
		
		for (DbThread thread : threads)
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println(String.format("Done. Run 5 threads over %d ms", endTime - beginTime));
	}
}

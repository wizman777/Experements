package org.isbar_software.loaders.stocks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;

import com.orientechnologies.orient.core.exception.OStorageException;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class App {
	
	public static final String CLASS_SYMBOL_MINUTE = "SymbolMinute";
	public static final String CLASS_SYMBOL_DAY = "SymbolDay";
	
	private static final DateFormat format = new SimpleDateFormat("YYYY-mm-dd HH:MM:SS", Locale.ENGLISH);

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
		long counter = 0;
		
		try {
			db = OObjectDatabasePool.global().acquire("plocal:./databases/stocks", "admin", "admin");
			
			System.out.println("Succussfully acquired database connection");
		} catch (OStorageException exc) {
			db = new OObjectDatabaseTx("plocal:./databases/stocks");
	    	db.open("admin", "admin");
	    	
	    	System.out.println("Succussfully created new database connection and open the db");
		}
		 
		try {
			db.getEntityManager().registerEntityClass(SymbolMinute.class);
			
			CSVReader reader = new CSVReader(new FileReader("AAL.txt"));
			String[] minuteData;
			
			long txCounter = 0;
			
			db.begin();
			
			while ((minuteData = reader.readNext()) != null) 
            {
				if (minuteData.length >= 7) {
					SymbolMinute minute = db.newInstance(SymbolMinute.class);
					minute.setSymbol("AAL");
					minute.setTime(format.parse(minuteData[0]));
					minute.setOpen(Double.parseDouble(minuteData[1]));
					minute.setLow(Double.parseDouble(minuteData[2]));
					minute.setHigh(Double.parseDouble(minuteData[3]));
					minute.setClose(Double.parseDouble(minuteData[4]));
					minute.setVolume(Long.parseLong(minuteData[6]));
					
					db.save( minute );
					
					++counter;
					
					if (++txCounter >= 1024)
					{
						db.commit();
						db.begin();
						txCounter = 0;
					}
				}
            }
			
			db.commit();
			
			reader.close();
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		finally {
			if (!db.isClosed())
				db.close();
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.println(String.format("Done. Imporded %d rows over %d ms. Average %f ms per row", 
				 counter, endTime - beginTime, (float)(endTime - beginTime) / (float)counter));
		
	}

}

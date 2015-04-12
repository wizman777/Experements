package dijkstra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class WorkThread extends Thread {

	private final Semaphore semaphore;
	private final boolean[] results;
	private final int resultId;
	private final BigInteger len;
	private final BigInteger total;
	private final String str;
		
	public static final String[] VECTOR = new String[8];
	public static final String[][] MATRIX = new String[8][8];

	static {
		VECTOR[0] = "1";
		VECTOR[1] = "i";
		VECTOR[2] = "j";
		VECTOR[3] = "k";
		VECTOR[4] = "-1";
		VECTOR[5] = "-i";
		VECTOR[6] = "-j";
		VECTOR[7] = "-k";
		
		MATRIX[0][0] = "1";
		MATRIX[0][1] = "i";
		MATRIX[0][2] = "j";
		MATRIX[0][3] = "k";
		MATRIX[1][0] = "i";
		MATRIX[1][1] = "-1";
		MATRIX[1][2] = "k";
		MATRIX[1][3] = "-j";
		MATRIX[2][0] = "j";
		MATRIX[2][1] = "-k";
		MATRIX[2][2] = "-1";
		MATRIX[2][3] = "i";
		MATRIX[3][0] = "k";
		MATRIX[3][1] = "j";
		MATRIX[3][2] = "-i";
		MATRIX[3][3] = "-1";
		
		for (int i = 0; i < 4; ++i)
			for (int j = 0; j < 4; ++j) {
				MATRIX[i+4][j] = inverse(MATRIX[i][j]);
				MATRIX[i][j+4] = inverse(MATRIX[i][j]);
				MATRIX[i+4][j+4] = MATRIX[i][j];
			}
	}
	
	public static boolean isNegative(String value) {
		return value.charAt(0) == '-';
	}
	
	public static String inverse(String value) {
		if (isNegative(value))
			return value.substring(1, 2);
		else
			return "-" + value;
	}
	
	public int getIndex(char value) throws Exception {
		switch(value) {
		case 'i':
			return 1;
		case 'j':
			return 2;
		case 'k':
			return 3;
		}
		
		throw new Exception("Invalid character");
	}
		
	public int getIndex(String value) throws Exception {
		for (int i = 0; i < 8; ++i)
			if (VECTOR[i].equals(value))
				return i;
		
		throw new Exception("Invalid string");
	}
	
	public String getProduct(String value1, char value2) throws Exception {
		if (null == value1)
			return "" + value2;
		
		int idx1 = getIndex(value1);
		int idx2 = getIndex(value2);
		
		return MATRIX[idx1][idx2];
	}

	public String getProductReverse(char value1, String value2) throws Exception {
		if (null == value2)
			return "" + value1;
		
		int idx1 = getIndex(value1);
		int idx2 = getIndex(value2);
		
		return MATRIX[idx1][idx2];
	}
	
	/*public boolean checkProduct(String str, long len, long from, long to, String product) throws Exception {
		String result = null;
		for (long i = from; i < to; ++i) 
			result = getProduct(result, str.charAt((int) (i%len)));
		return null != result && result.equals(product);
	}*/
	
	public BigInteger findProduct(String str, BigInteger len, BigInteger from, BigInteger to, String product, String result) throws Exception {
		for (BigInteger i = from; i.compareTo(to) < 0; i.add(BigInteger.ONE)) {
			result = getProduct(result, str.charAt(i.remainder(len).intValue()));
			if (result.equals(product)) 
				return i;
		}
		
		return null;			
	}
	
	public BigInteger findProductReverse(String str, BigInteger len, BigInteger from, BigInteger to, String product, String result) throws Exception {
		for (BigInteger i = from; i.compareTo(to) >= 0; i.subtract(BigInteger.ONE)) {
			result = getProductReverse(str.charAt(i.remainder(len).intValue()), result);
			if (result.equals(product)) 
				return i;
		}
		
		return null;			
	}
	
	public List<BigInteger> findProducts(String str, BigInteger len, BigInteger start, BigInteger total, String product) throws Exception {
		BigInteger idx = findProduct(str, len, start, total, product, null);
		if (null != idx) {
			List<BigInteger> results = new ArrayList<BigInteger>();
			results.add(idx);
			
			while (idx.compareTo(total) < 0) {
				idx = findProduct(str, len, idx.add(BigInteger.ONE), total, product, product);
				if (null != idx)
					results.add(idx);
				else
					break;
			}
			
			return results;
		}

		return null;
	}
	
	public List<BigInteger> findProductsReverse(String str, BigInteger len, BigInteger start, String product) throws Exception {
		BigInteger idx = findProductReverse(str, len, start, BigInteger.ZERO, product, null);
		if (null != idx) {
			List<BigInteger> results = new ArrayList<BigInteger>();
			results.add(idx);
			
			while (idx.compareTo(start) < 0) {
				idx = findProductReverse(str, len, idx.subtract(BigInteger.ONE), BigInteger.ZERO, product, product);
				if (null != idx)
					results.add(idx);
				else
					break;
			}
			
			return results;
		}

		return null;
	}
	
	public boolean checkString(String str, BigInteger len, BigInteger total) throws Exception {
		List<BigInteger> i = findProducts(str, len, BigInteger.ZERO, total, "i");
		if (null != i) {
			List<BigInteger> k = findProductsReverse(str, len, total.subtract(BigInteger.ONE), "k");
			if (null != k) {
				for (BigInteger iEnd : i) {
					List<BigInteger> j = findProducts(str, len, iEnd.add(BigInteger.ONE), total, "j");
					if (null != j) {
						for (BigInteger jEnd : j)
							for (BigInteger kStart : k) {
								if (jEnd.add(BigInteger.ONE).equals(kStart))
									return true;
								else if (jEnd.compareTo(kStart) >= 0)
									break;
							}
					}
				}
			}
				/*
				for (Long from : i)
					for (Long to : k) 
						if ((from+1) < (to-1))
							if (checkProduct(str, len, from+1, to-1, "j")) 
								return true;*/
		}
		
		return false;				
	}
	
	public WorkThread(Semaphore semaphore, boolean[] results, int resultId, 
			BigInteger len, BigInteger total, String str) {
		this.semaphore = semaphore;
		this.results = results;
		this.resultId = resultId;
		this.len = len;
		this.total = total;
		this.str = str;
	}
	
	@Override
	public void run() {
		try {
			results[resultId] = checkString(str, len, total);
			
			System.out.println("Case #" + (resultId + 1) + ": " + (results[resultId] ? "YES" : "NO"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		semaphore.release();
	}
}

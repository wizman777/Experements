package dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class WorkThread extends Thread {

	private final Semaphore semaphore;
	private final boolean[] results;
	private final int resultId;
	private final long len;
	private final long total;
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
	
	public Long findProduct(String str, long len, long from, long to, String product, String result) throws Exception {
		for (long i = from; i < to; ++i) {
			result = getProduct(result, str.charAt((int) (i % len)));
			if (result.equals(product)) 
				return i;
		}
		
		return null;			
	}
	
	public Long findProductReverse(String str, long len, long from, long to, String product, String result) throws Exception {
		for (long i = from; i >= to; --i) {
			result = getProductReverse(str.charAt((int) (i % len)), result);
			if (result.equals(product)) 
				return i;
		}
		
		return null;			
	}
	
	public List<Long> findProducts(String str, long len, long start, long total, String product) throws Exception {
		Long idx = findProduct(str, len, start, total, product, null);
		if (null != idx) {
			List<Long> results = new ArrayList<Long>();
			results.add(idx);
			
			while (idx.compareTo(total) < 0) {
				idx = findProduct(str, len, idx + 1, total, product, product);
				if (null != idx)
					results.add(idx);
				else
					break;
			}
			
			return results;
		}

		return null;
	}
	
	public List<Long> findProductsReverse(String str, long len, long start, String product) throws Exception {
		Long idx = findProductReverse(str, len, start, 0, product, null);
		if (null != idx) {
			List<Long> results = new ArrayList<Long>();
			results.add(idx);
			
			while (idx.compareTo(start) < 0) {
				idx = findProductReverse(str, len, idx - 1, 0, product, product);
				if (null != idx)
					results.add(idx);
				else
					break;
			}
			
			return results;
		}

		return null;
	}
	
	public boolean checkString(String str, long len, long total) throws Exception {
		List<Long> i = findProducts(str, len, 0, total, "i");
		if (null != i) {
			List<Long> k = findProductsReverse(str, len, total-1, "k");
			if (null != k) {
				for (Long iEnd : i) {
					List<Long> j = findProducts(str, len, iEnd + 1, total, "j");
					if (null != j) {
						for (Long jEnd : j)
							for (Long kStart : k) {
								if ((jEnd + 1) == kStart)
									return true;
								else if (jEnd >= kStart)
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
			long len, long total, String str) {
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

package org.isbar_software.loaders.stocks;

import java.util.Date;

public class SymbolMinute {
	private String symbol;
	private Date time;
	private double open;
	private double low;
	private double high;
	private double close;
	private long volume;
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "SymbolMinute [symbol=" + symbol + ", time=" + time + ", open="
				+ open + ", low=" + low + ", high=" + high + ", close=" + close
				+ ", volume=" + volume + "]";
	}
	
	
}

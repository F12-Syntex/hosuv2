package com.syntex.manga.utils;

public class StopWatch {

	public StopWatch() {
		this.start();
	}
	
	private long start;

	public void start() {
		this.start = System.currentTimeMillis();
	}
	public long stamp() {
		long time = System.currentTimeMillis() - start;
		return time;
	}
	
	public void reset() {
		this.start();
	}

}

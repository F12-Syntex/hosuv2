package com.hosu.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Queue {
	public static ExecutorService service = Executors.newFixedThreadPool(100);
}

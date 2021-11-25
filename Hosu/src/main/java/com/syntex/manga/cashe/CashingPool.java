package com.syntex.manga.cashe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CashingPool {
	public static ExecutorService pool = Executors.newFixedThreadPool(5);
}

package com.hosu.database;

public class Testing {

	public static void main(String[] args) {
		
		Database database = new Database();

		try {
			database.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Scrapper scrapper = new Scrapper();
		scrapper.run(database);
		
	}
}

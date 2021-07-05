package com.database;

public class Main {
	Window window;
	DatabaseManager databaseManager;
	public Main() {
		
		databaseManager = new DatabaseManager();
		window = new Window(this, databaseManager);
	}
	
	public static void main(String[] args) {
		
		new Main();
	}
}

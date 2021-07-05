package com.database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

	Date date = new Date();
	
	SimpleDateFormat dateNumber = new SimpleDateFormat("MM/dd/yy");
	SimpleDateFormat exactDate = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
	SimpleDateFormat weekday = new SimpleDateFormat("EEEEEEEE");
	SimpleDateFormat time = new SimpleDateFormat("hh:mm aa");
	
	public static void main(String[] args) {
		
		new DateManager();
	}
	
	public DateManager() {
		
	}
	
	public String GetDateNumber() {
		date = new Date();
		return dateNumber.format(date);
	}
	
	public String GetWeekday() {
		date = new Date();
		return weekday.format(date);
	}
	
	public String GetExactDate() {
		date = new Date();
		return exactDate.format(date);
	}
	
	public String GetTime() {
		Date date = new Date();
		return time.format(date);
	}
	
	public String GetTime2() {
		return time.format(date);
	}
}
